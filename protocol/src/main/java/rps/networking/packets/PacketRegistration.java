package rps.networking.packets;

import com.esotericsoftware.kryonet.EndPoint;
import lombok.experimental.UtilityClass;
import org.objenesis.strategy.StdInstantiatorStrategy;
import rps.networking.model.CommandFormat;import rps.networking.model.CommandParameter;import rps.networking.packets.client.ClientCommand;
import rps.networking.packets.server.ServerCommands;
import rps.networking.packets.server.ServerDisconnect;
import rps.networking.packets.server.ServerMessage;
import rps.networking.packets.server.ServerResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

@UtilityClass
public class PacketRegistration {

  public static void registerPackets(EndPoint endPoint) {
    var kryo = endPoint.getKryo();
    kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

    // Base structures
    kryo.register(ArrayList.class);
    kryo.register(HashMap.class);
    kryo.register(LinkedHashMap.class);
    kryo.register(Class.class);

    // Models
    kryo.register(CommandFormat.class);
    kryo.register(CommandParameter.class);

    // Client -> Server
    kryo.register(ClientCommand.class);

    // Server -> Client
    kryo.register(ServerCommands.class);
    kryo.register(ServerMessage.class);
    kryo.register(ServerResponse.class);
    kryo.register(ServerDisconnect.class);
  }
}
