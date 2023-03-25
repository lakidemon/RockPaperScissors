package rps.client.command;

public interface Command {
  void execute(String[] args) throws Exception;

  default boolean isActive() {
    return true;
  }
}
