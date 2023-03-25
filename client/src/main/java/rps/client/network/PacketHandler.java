package rps.client.network;

import rps.networking.connection.Connection;import rps.networking.packets.client.ClientPacket;import rps.networking.packets.server.ServerPacket;

public interface PacketHandler<T extends ServerPacket> {

    void handle(Connection connection, T packet);

}
