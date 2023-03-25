package rps.networking.packets.client;

import java.util.Map;
import lombok.Value;

@Value
public class ClientCommand implements ClientPacket {
  String command;
  Map<String, Object> args;
}
