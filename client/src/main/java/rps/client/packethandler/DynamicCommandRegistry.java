package rps.client.packethandler;

import com.google.common.collect.Multimaps;
import java.util.HashSet;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import rps.client.ConsoleClient;
import rps.client.command.Command;
import rps.client.command.CommandManager;
import rps.client.command.commands.RemoteCommand;
import rps.client.network.PacketHandler;
import rps.networking.connection.Connection;
import rps.networking.model.CommandFormat;
import rps.networking.model.CommandParameter;
import rps.networking.packets.server.ServerCommands;

@RequiredArgsConstructor
public class DynamicCommandRegistry implements PacketHandler<ServerCommands> {
  private final ConsoleClient consoleClient;
  private final CommandManager commandManager;

  @Override
  public void handle(Connection connection, ServerCommands serverCommands) {
    var availableCommands = serverCommands.getAvailableCommands();
    var receivedCommands = new HashSet<>();
    for (var command : availableCommands) {
      commandManager
          .getCommand(command.getName())
          .map(RemoteCommand.class::cast)
          .ifPresentOrElse(
              c -> c.setActive(true),
              () -> {
                commandManager.registerCommand(
                    command.getName(), new RemoteCommand(consoleClient, command));
              });
      receivedCommands.add(command.getName());
    }
    
    commandManager.getCommands().stream()
        .filter(RemoteCommand.class::isInstance)
        .filter(Command::isActive)
        .filter(c -> !receivedCommands.contains(((RemoteCommand) c).getCommandFormat().getName()))
        .forEach(c -> ((RemoteCommand) c).setActive(false));

    var categorized = Multimaps.index(availableCommands, CommandFormat::getCategory);

    System.out.println("Доступные команды:");
    for (var category : categorized.keySet()) {
      System.out.println(" " + category + ":");
      for (var format : categorized.get(category)) {
        System.out.println("  ·" + formatCommand(format));
      }
      System.out.println();
    }
  }

  private static String formatCommand(CommandFormat commandFormat) {
    var builder = new StringBuilder(commandFormat.getName());
    if (!commandFormat.getParams().isEmpty()) {
      builder.append('=');
      builder.append(
          commandFormat.getParams().stream()
              .map(CommandParameter::getName)
              .collect(Collectors.joining("=")));
    }
    builder.append(" - ").append(commandFormat.getDescription());
    return builder.toString();
  }
}
