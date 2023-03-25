package rps.client.command.commands;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rps.client.ConsoleClient;
import rps.client.command.Command;
import rps.networking.connection.Connection;
import rps.networking.packets.client.ClientCommand;

@Slf4j
@RequiredArgsConstructor
public class AuthCommand implements Command {
  private final ConsoleClient consoleClient;
  private final String command;

  @Override
  public void execute(String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("Укажите логин и пароль");
      return;
    }

    performConnection(new ClientCommand(command, new HashMap<>(Map.of("login", args[0], "password", args[1]))));
  }

  public void performConnection(ClientCommand initialCommand) {
    var connectionFuture =
        consoleClient
            .getConnectionFactory()
            .connectToServer(consoleClient.getHost(), consoleClient.getPort())
            .whenComplete(
                (clientPacketConnection, throwable) -> {
                  if (clientPacketConnection != null) {
                    System.out.println("Подключение установлено");
                    System.out.println();
                    consoleClient.setupConnection(clientPacketConnection);
                    clientPacketConnection.sendPacket(initialCommand);
                  } else {
                    log.error("Не удалось подключиться к серверу: {}", throwable.getMessage());
                  }
                });
    while (!connectionFuture.isDone()) {}
  }

  @Override
  public boolean isActive() {
    return Optional.ofNullable(consoleClient.getConnection())
        .filter(Connection::isConnected)
        .isEmpty();
  }
}
