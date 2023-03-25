package rps.client.packethandler;

import rps.client.network.PacketHandler;
import rps.networking.connection.Connection;
import rps.networking.packets.server.ServerDisconnect;

public class KickHandler implements PacketHandler<ServerDisconnect> {
  @Override
  public void handle(Connection connection, ServerDisconnect packet) {
    System.out.printf("[!] Отключен от сервера: %s\n", packet.getMessage());
    connection.disconnect();
  }
}
