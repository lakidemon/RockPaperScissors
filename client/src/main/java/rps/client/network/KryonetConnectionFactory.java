package rps.client.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import rps.networking.connection.Connection;
import rps.networking.packets.PacketRegistration;
import rps.networking.packets.server.ServerPacket;

public class KryonetConnectionFactory implements ConnectionFactory {
  public static final ConnectionFactory INSTANCE = new KryonetConnectionFactory();
  private final Map<Class<? extends ServerPacket>, PacketHandler<? extends ServerPacket>>
      typeHandlers = new LinkedHashMap<>();
  private final List<ClientListener> listeners = new ArrayList<>();

  @Override
  public CompletableFuture<Connection> connectToServer(
      String host, int port) {
    CompletableFuture<Connection> future = new CompletableFuture<>();
    var client = new Client();
    PacketRegistration.registerPackets(client);
    var knConnection = new KryonetConnection(client);

    client.start();
    client.addListener(
        new Listener() {
          @Override
          public void connected(com.esotericsoftware.kryonet.Connection connection) {
            future.complete(knConnection);

            connection.addListener(makeTypeListener(knConnection));
            listeners.stream()
                .map(l -> makeClientListener(knConnection, l))
                .forEach(connection::addListener);
          }
        });
    
    var connectionThread =
        new Thread(
            () -> {
              try {
                client.connect(5000, host, port);
              } catch (IOException e) {
                future.completeExceptionally(e);
              }
            });
    connectionThread.setDaemon(true);
    connectionThread.setName("Connection holder thread");
    connectionThread.start();
    
    return future;
  }

  @Override
  public void registerListener(ClientListener listener) {
    listeners.add(listener);
  }

  @Override
  public <T extends ServerPacket> void registerHandler(
      Class<T> packetClass, PacketHandler<T> handler) {
    typeHandlers.put(packetClass, handler);
  }

  private Listener makeTypeListener(Connection connection) {
    var listener = new Listener.TypeListener();

    for (var typeHandler : typeHandlers.entrySet()) {
      var packetClass = (Class) typeHandler.getKey();
      var packetHandler = (PacketHandler) typeHandler.getValue();

      listener.addTypeHandler(
          packetClass,
          (con, packet) -> {
            packetHandler.handle(connection, (ServerPacket) packet);
          });
    }
    return listener;
  }

  private Listener makeClientListener(KryonetConnection connection, ClientListener clientListener) {
    return new Listener() {
      @Override
      public void connected(com.esotericsoftware.kryonet.Connection c) {
        clientListener.onConnect(connection);
      }

      @Override
      public void disconnected(com.esotericsoftware.kryonet.Connection c) {
        clientListener.onDisconnect(connection);
      }
    };
  }
}
