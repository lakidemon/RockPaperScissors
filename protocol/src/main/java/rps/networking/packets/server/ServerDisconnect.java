package rps.networking.packets.server;

import lombok.Value;

@Value
public class ServerDisconnect implements ServerPacket {
  String message;
}
