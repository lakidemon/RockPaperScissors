package rps.networking.packets.server;

import lombok.Value;

@Value
public class ServerResponse implements ServerPacket {
    boolean success;
    String message;

    public static ServerResponse error(String message) {
        return new ServerResponse(false, message);
    }

    public static ServerResponse success(String message) {
        return new ServerResponse(true, message);
    }
}
