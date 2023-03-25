package rps.networking.packets.server;

import lombok.Value;

@Value
public class ServerMessage implements ServerPacket {
    String message;
}
