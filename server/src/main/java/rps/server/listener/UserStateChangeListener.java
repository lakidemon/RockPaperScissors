package rps.server.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rps.server.command.CommandManager;
import rps.server.event.UserStateChangedEvent;
import rps.server.network.GameServer;

@Component
@RequiredArgsConstructor
public class UserStateChangeListener {
  private final CommandManager commandManager;
  private final GameServer gameServer;

  @EventListener(condition = "event.newState.name() == 'MENU'")
  public void returnToMenu(UserStateChangedEvent event) {
    commandManager.broadcastAvailableCommands(gameServer.getConnectionByUser(event.getUserInfo()));
  }
}
