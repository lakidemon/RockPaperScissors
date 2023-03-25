package rps.server.network;

import rps.server.model.UserInfo;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

public interface GameServer {

  void start() throws IOException;

  void stop();

  Collection<ClientConnection> getConnections();

  Optional<ClientConnection> getConnection(String id);

  ClientConnection getConnectionByUser(UserInfo user);
}
