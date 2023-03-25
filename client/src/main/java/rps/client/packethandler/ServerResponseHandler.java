package rps.client.packethandler;

import rps.client.network.PacketHandler;
import rps.networking.connection.Connection;
import rps.networking.packets.client.ClientPacket;
import rps.networking.packets.server.ServerResponse;

public class ServerResponseHandler implements PacketHandler<ServerResponse> {

  @Override
  public void handle(Connection connection, ServerResponse packet) {
    System.out.printf("[%s] %s\n", packet.isSuccess() ? "V" : "X", packet.getMessage());
  }
}
