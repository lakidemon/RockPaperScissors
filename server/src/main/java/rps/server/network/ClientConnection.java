package rps.server.network;

import java.net.InetSocketAddress;
import java.time.Instant;
import javax.annotation.Nullable;
import rps.networking.connection.Connection;
import rps.server.model.UserInfo;

public interface ClientConnection extends Connection {

  String getConnectionId();

  InetSocketAddress getAddress();

  GameServer getServer();

  Instant getConnectedTime();

  void setUser(UserInfo user);

  @Nullable
  UserInfo getUser();

  default boolean isLogged() {
    return getUser() != null;
  }
}
