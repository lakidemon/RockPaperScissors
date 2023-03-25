package rps.client.packethandler;

import rps.client.network.PacketHandler;
import rps.networking.connection.Connection;
import rps.networking.packets.client.ClientPacket;
import rps.networking.packets.server.ServerMessage;

public class ServerMessageHandler implements PacketHandler<ServerMessage> {
  @Override
  public void handle(Connection connection, ServerMessage packet) {
    System.out.printf("[*] %s\n", packet.getMessage());
  }
}
