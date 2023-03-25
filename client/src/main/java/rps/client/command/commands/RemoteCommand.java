package rps.client.command.commands;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import rps.client.ConsoleClient;
import rps.client.command.Command;
import rps.networking.model.CommandFormat;
import rps.networking.packets.client.ClientCommand;

@Getter
@RequiredArgsConstructor
public class RemoteCommand implements Command {
  private final ConsoleClient consoleClient;
  private final CommandFormat commandFormat;
  @Setter private boolean active = true;

  @Override
  public void execute(String[] args) throws Exception {
    if (args.length < commandFormat.getParams().size()) {
      System.err.println("Недостаточно аргументов");
      return;
    }
    Map<String, Object> params = new HashMap<>();
    var formatParams = commandFormat.getParams();
    for (int i = 0; i < formatParams.size(); i++) {
      params.put(formatParams.get(i).getName(), args[i]);
    }

    consoleClient.getConnection().sendPacket(new ClientCommand(commandFormat.getName(), params));
  }

  public boolean isActive() {
    return active && consoleClient.getConnection().isConnected();
  }
}
