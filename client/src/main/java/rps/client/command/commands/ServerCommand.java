package rps.client.command.commands;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import rps.client.ConsoleClient;
import rps.client.command.Command;
import rps.networking.connection.Connection;

@Slf4j
@RequiredArgsConstructor
public class ServerCommand implements Command {
  private final ConsoleClient consoleClient;

  @Override
  public void execute(String[] args) {
    if (args.length < 1) {
      System.err.println("Нужно указать как минимум хост сервера");
      return;
    }
    consoleClient.setHost(args[0]);
    if (args.length > 1) {
      consoleClient.setPort(Integer.parseInt(args[1]));
    }

    System.out.printf("Адрес сервера - %s:%d\n", consoleClient.getHost(), consoleClient.getPort());
  }

  @Override
  public boolean isActive() {
    return Optional.ofNullable(consoleClient.getConnection())
        .filter(Connection::isConnected)
        .isEmpty();
  }
}
