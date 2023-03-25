package rps.client;

import org.slf4j.simple.SimpleLogger;
import rps.client.network.KryonetConnectionFactory;

public class Main {

  static {
    System.setProperty(SimpleLogger.SHOW_THREAD_NAME_KEY, "false");
    System.setProperty(SimpleLogger.SHOW_THREAD_ID_KEY, "false");
    System.setProperty(SimpleLogger.SHOW_SHORT_LOG_NAME_KEY, "false");
    System.setProperty(SimpleLogger.SHOW_LOG_NAME_KEY, "false");
    System.setProperty(SimpleLogger.SHOW_DATE_TIME_KEY, "true");
    System.setProperty(SimpleLogger.DATE_TIME_FORMAT_KEY, "HH:mm:ss");
    System.setProperty(SimpleLogger.LEVEL_IN_BRACKETS_KEY, "true");
  }

  public static void main(String[] args) throws Exception {
    var consoleClient = new ConsoleClient(KryonetConnectionFactory.INSTANCE);
    Runtime.getRuntime().addShutdownHook(new Thread(consoleClient::stop));
    consoleClient.setup();
    var thread = consoleClient.start();
    thread.join();
    System.out.println("Goodbye");
  }
}
