package rps.client.command;

import com.google.common.collect.ImmutableList;
import lombok.SneakyThrows;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
  private final Map<String, Command> commandMap = new ConcurrentHashMap<>();

  public void registerCommand(String command, Command commandExecutor) {
    commandMap.put(command.toLowerCase(), commandExecutor);
  }

  public void unregisterCommand(String command) {
    commandMap.remove(command.toLowerCase());
  }

  public Collection<Command> getCommands() {
    return ImmutableList.copyOf(commandMap.values());
  }

  public Optional<Command> getCommand(String command) {
    return Optional.ofNullable(commandMap.get(command.toLowerCase()));
  }

  public void executeCommand(String commandLine) throws Exception {
    var parts = commandLine.split("=");

    getCommand(parts[0])
        .ifPresentOrElse(
            command -> executeCommand(command, parts),
            () -> System.err.println("Неизвестная команда"));
  }

  @SneakyThrows
  private void executeCommand(Command command, String[] parts) {
    if (!command.isActive()) {
      System.err.println("Данная команда недоступна сейчас");
      return;
    }
    command.execute(Arrays.stream(parts).skip(1).toArray(String[]::new));
  }
}
