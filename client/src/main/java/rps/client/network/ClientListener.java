package rps.client.network;

import rps.networking.connection.Connection;
import rps.networking.packets.client.ClientPacket;

public interface ClientListener {
  default void onConnect(Connection connection) {}

  default void onDisconnect(Connection connection) {}
}
