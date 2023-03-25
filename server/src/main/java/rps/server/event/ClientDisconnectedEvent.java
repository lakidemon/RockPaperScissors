package rps.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rps.server.network.ClientConnection;

@Getter
public class ClientDisconnectedEvent extends ApplicationEvent {
    private final ClientConnection connection;

    public ClientDisconnectedEvent(Object source, ClientConnection connection) {
        super(source);
        this.connection = connection;
    }
}
