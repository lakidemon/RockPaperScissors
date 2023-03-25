package rps.client.network;

import java.util.concurrent.CompletableFuture;
import rps.networking.connection.Connection;
import rps.networking.packets.server.ServerPacket;

public interface ConnectionFactory {

  CompletableFuture<Connection> connectToServer(String host, int port);

  void registerListener(ClientListener listener);

  <T extends ServerPacket> void registerHandler(Class<T> packetClass, PacketHandler<T> handler);
}
