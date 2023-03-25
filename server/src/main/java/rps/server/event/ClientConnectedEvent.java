package rps.server.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import rps.server.network.ClientConnection;

@Getter
public class ClientConnectedEvent extends ApplicationEvent {
    private final ClientConnection connection;

    public ClientConnectedEvent(Object source, ClientConnection connection) {
        super(source);
        this.connection = connection;
    }
}
