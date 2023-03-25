package rps.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import rps.client.command.CommandManager;
import rps.client.command.commands.AuthCommand;
import rps.client.command.commands.ExitCommand;
import rps.client.command.commands.ServerCommand;
import rps.client.network.ConnectionFactory;
import rps.client.packethandler.DynamicCommandRegistry;
import rps.client.packethandler.KickHandler;
import rps.client.packethandler.ServerMessageHandler;
import rps.client.packethandler.ServerResponseHandler;
import rps.networking.connection.Connection;
import rps.networking.packets.server.ServerCommands;
import rps.networking.packets.server.ServerDisconnect;
import rps.networking.packets.server.ServerMessage;
import rps.networking.packets.server.ServerResponse;

@Slf4j
@Getter
@RequiredArgsConstructor
public class ConsoleClient implements Runnable {
  private final ConnectionFactory connectionFactory;
  private final AtomicBoolean running = new AtomicBoolean(true);
  @Setter private String host = "localhost";
  @Setter private int port = 54555;
  private Connection connection;
  private CommandManager commandManager;

  public void setup() {
    commandManager = new CommandManager();
    commandManager.registerCommand("server", new ServerCommand(this));
    commandManager.registerCommand("signin", new AuthCommand(this, "signin"));
    commandManager.registerCommand("signup", new AuthCommand(this, "signup"));
    commandManager.registerCommand("exit", new ExitCommand(this));

    connectionFactory.registerHandler(
        ServerCommands.class, new DynamicCommandRegistry(this, commandManager));
    connectionFactory.registerHandler(ServerDisconnect.class, new KickHandler());
    connectionFactory.registerHandler(ServerMessage.class, new ServerMessageHandler());
    connectionFactory.registerHandler(ServerResponse.class, new ServerResponseHandler());
  }

  public void setupConnection(Connection connection) {
    this.connection = connection;
  }

  @SneakyThrows
  public Thread start() {
    System.out.println("Клиентские команды:");
    System.out.println(" server=IP=port - указать адрес подключения(по умолчанию задан localhost:54555");
    System.out.println(" signin=login=password - подключиться и авторизоваться");
    System.out.println(" signup=login=password - подключиться и зарегистрироваться");
    System.out.println(" exit - закрыть клиент");
    System.out.println();
    var thread = new Thread(this);
    thread.start();
    return thread;
  }

  @Override
  public void run() {
    try {
      var scanner = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
      System.out.print("=> ");
      String line;
      while (running.get() && (line = scanner.readLine()) != null) {
        try {
          commandManager.executeCommand(line);
        } catch (Exception e) {
          log.error("Ошибка при выполнении команды", e);
          System.out.println();
        }
        System.out.print("=> ");
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      stop();
    }
  }

  public void stop() {
    if (running.get()) {
      running.set(false);
      Optional.ofNullable(connection).ifPresent(Connection::disconnect);
    }
  }
}
