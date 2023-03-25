package rps.server.network.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rps.networking.packets.client.ClientPacket;
import rps.server.network.ClientConnection;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PacketHandlerManager {
    private final Map<Class, PacketHandler> packetHandlers = new HashMap<>();

    public void handlePacket(ClientConnection sender, ClientPacket packet) {
        Optional.ofNullable(packetHandlers.get(packet.getClass()))
                .orElseThrow(() -> new IllegalArgumentException("Unknown packet " + packet))
                .handlePacket(sender, packet);
    }

    public Map<Class, PacketHandler> getPacketHandlers() {
        return Collections.unmodifiableMap(packetHandlers);
    }

    @Autowired
    void bakePacketHandlers(Collection<PacketHandler> handler) {
        for (var packetHandler : handler) {
            var handlingType = packetHandler.getClass().getDeclaredMethods()[0].getParameterTypes()[1];
            packetHandlers.put(handlingType, packetHandler);
        }
    }
}
