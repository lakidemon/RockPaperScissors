package rps.server.network.kryonet;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import rps.networking.packets.PacketRegistration;
import rps.networking.packets.client.ClientPacket;
import rps.server.event.ClientConnectedEvent;
import rps.server.event.ClientDisconnectedEvent;
import rps.server.model.UserInfo;
import rps.server.network.ClientConnection;
import rps.server.network.GameServer;
import rps.server.network.handler.PacketHandler;
import rps.server.network.handler.PacketHandlerManager;
import rps.server.properties.ServerProperties;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class KryonetServer implements GameServer, Listener {
  private final Map<String, ClientConnection> connections = new ConcurrentHashMap<>();
  private final ServerProperties serverProperties;
  private final ApplicationEventPublisher eventPublisher;
  private final PacketHandlerManager handlerManager;
  private Server server;

  @Override
  @EventListener(classes = ContextRefreshedEvent.class)
  public void start() throws IOException {
    server = new Server();
    PacketRegistration.registerPackets(server);
    server.start();
    server.bind(new InetSocketAddress(serverProperties.host(), serverProperties.tcpPort()), null);
    server.addListener(new Listener.ThreadedListener(this));
    log.info("Started server at {}:{}", serverProperties.host(), serverProperties.tcpPort());
  }

  @Override
  @EventListener(classes = ContextClosedEvent.class)
  public void stop() {
    log.info("Stopping server");
    server.stop();
  }

  @Override
  public Collection<ClientConnection> getConnections() {
    return ImmutableList.copyOf(connections.values());
  }

  @Override
  public Optional<ClientConnection> getConnection(String id) {
    return Optional.ofNullable(connections.get(id));
  }

  @Override
  public ClientConnection getConnectionByUser(UserInfo user) {
    return connections.values().stream()
        .filter(ClientConnection::isLogged)
        .filter(c -> c.getUser().equals(user))
        .findAny()
        .orElseThrow(
            () -> new IllegalStateException("Не найдено соединение для " + user.getName()));
  }

  @Override
  public void connected(Connection connection) {
    var knConnection = new KryonetConnection(connection, this);

    var typeListener = new TypeListener();
    handlerManager
        .getPacketHandlers()
        .forEach((c, h) -> typeListener.addTypeHandler(c, makeAdapter(knConnection, h)));
    connection.addListener(typeListener);

    connections.put(knConnection.getConnectionId(), knConnection);

    eventPublisher.publishEvent(new ClientConnectedEvent(this, knConnection));
  }

  @Override
  public void disconnected(Connection connection) {
    getConnection(Integer.toString(connection.getID()))
        .ifPresent(
            cc -> {
              connections.remove(cc.getConnectionId());
              eventPublisher.publishEvent(new ClientDisconnectedEvent(this, cc));
            });
  }

  private BiConsumer<Connection, Object> makeAdapter(
      KryonetConnection connection, PacketHandler handler) {
    return (c, p) -> handler.handlePacket(connection, (ClientPacket) p);
  }
}
