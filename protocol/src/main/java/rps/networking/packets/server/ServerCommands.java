package rps.networking.packets.server;

import java.util.List;
import lombok.Builder;
import lombok.Value;
import rps.networking.model.CommandFormat;

@Value
@Builder
public class ServerCommands implements ServerPacket {
  List<CommandFormat> availableCommands;
}
