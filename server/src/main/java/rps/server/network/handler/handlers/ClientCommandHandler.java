package rps.server.network.handler.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import rps.networking.packets.client.ClientCommand;
import rps.server.command.CommandManager;
import rps.server.network.ClientConnection;
import rps.server.network.handler.PacketHandler;
import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ClientCommandHandler implements PacketHandler<ClientCommand> {
  private final CommandManager commandManager;

  @Override
  public void handlePacket(ClientConnection connection, ClientCommand packet) {
    commandManager.executeCommand(
        connection,
        Optional.ofNullable(packet.getCommand()).map(String::toLowerCase).orElse(""),
        Optional.ofNullable(packet.getArgs()).orElse(Collections.emptyMap()));
  }
}
