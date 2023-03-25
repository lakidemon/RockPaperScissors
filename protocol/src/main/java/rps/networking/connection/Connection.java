package rps.networking.connection;

import rps.networking.packets.Packet;

public interface Connection {

  void sendPacket(Packet packet);

  void disconnect(String reason);

  default void disconnect() {
    disconnect(null);
  }

  boolean isConnected();
}
