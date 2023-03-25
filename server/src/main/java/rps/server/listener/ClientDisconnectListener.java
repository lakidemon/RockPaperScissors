package rps.server.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rps.server.event.ClientDisconnectedEvent;
import rps.server.service.UserService;

@Component
@RequiredArgsConstructor
public class ClientDisconnectListener {
  private final UserService userService;

  @EventListener
  public void onUserDisconnect(ClientDisconnectedEvent e) {
    if (e.getConnection().isLogged()) {
      userService.logoutUser(e.getConnection().getUser());
    }
  }
}
