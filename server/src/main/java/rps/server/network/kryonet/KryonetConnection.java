package rps.server.network.kryonet;

import com.esotericsoftware.kryonet.Connection;
import java.net.InetSocketAddress;
import java.time.Instant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import rps.networking.packets.Packet;
import rps.networking.packets.server.ServerDisconnect;
import rps.server.model.UserInfo;
import rps.server.network.ClientConnection;
import rps.server.network.GameServer;

@Slf4j
@Getter
@RequiredArgsConstructor
public class KryonetConnection implements ClientConnection {
  private final Connection connection;
  private final GameServer server;
  private final Instant connectedTime = Instant.now();
  @Setter private UserInfo user;

  @Override
  public void sendPacket(Packet packet) {
    if (packet instanceof ServerDisconnect kick) {
      disconnect(kick.getMessage());
      return;
    }
    _sendPacket(packet);
  }

  private void _sendPacket(Packet packet) {
    connection.sendTCP(packet);
  }

  @Override
  public void disconnect(String reason) {
    log.debug("Disconnecting {}. Reason: {}", connection.getID(), reason);
    _sendPacket(new ServerDisconnect(reason));
    connection.close();
  }

  @Override
  public boolean isConnected() {
    return connection.isConnected();
  }

  @Override
  public InetSocketAddress getAddress() {
    return connection.getRemoteAddressTCP();
  }

  @Override
  public String getConnectionId() {
    return Integer.toString(connection.getID());
  }
}
