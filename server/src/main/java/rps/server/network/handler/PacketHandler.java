package rps.server.network.handler;

import rps.networking.packets.client.ClientPacket;
import rps.server.network.ClientConnection;

public interface PacketHandler<T extends ClientPacket> {

    void handlePacket(ClientConnection connection, T packet);

}
