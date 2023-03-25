package rps.client.command.commands;

import lombok.RequiredArgsConstructor;
import rps.client.ConsoleClient;
import rps.client.command.Command;

@RequiredArgsConstructor
public class ExitCommand implements Command {
  private final ConsoleClient client;

  @Override
  public void execute(String[] args) throws Exception {
    client.stop();
  }
}
