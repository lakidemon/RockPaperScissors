package rps.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rps.server.network.ClientConnection;

@Getter
public abstract class ClientEvent extends ApplicationEvent {
  private final ClientConnection connection;

  public ClientEvent(Object source, ClientConnection connection) {
    super(source);
    this.connection = connection;
  }
}
