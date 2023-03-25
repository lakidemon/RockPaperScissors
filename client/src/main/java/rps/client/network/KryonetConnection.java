package rps.client.network;

import com.esotericsoftware.kryonet.Client;
import lombok.RequiredArgsConstructor;
import rps.networking.connection.Connection;
import rps.networking.packets.Packet;

@RequiredArgsConstructor
public class KryonetConnection implements Connection {
  private final Client client;

  @Override
  public void sendPacket(Packet packet) {
    client.sendTCP(packet);
  }

  @Override
  public void disconnect(String reason) {
    client.close();
  }

  @Override
  public boolean isConnected() {
    return client.isConnected();
  }
}
