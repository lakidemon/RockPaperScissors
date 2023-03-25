package rps.server.event;

import lombok.Getter;
import rps.networking.packets.client.ClientCommand;
import rps.server.network.ClientConnection;

@Getter
public class ClientCommandEvent extends ClientEvent {
  private final ClientCommand command;
  private boolean handled = false;

  public ClientCommandEvent(Object source, ClientConnection client, ClientCommand command) {
    super(source, client);
    this.command = command;
  }
}
