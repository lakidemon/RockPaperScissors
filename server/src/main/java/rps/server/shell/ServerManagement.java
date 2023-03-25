package rps.server.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import rps.server.repository.GameSessionRepository;

@ShellComponent
@RequiredArgsConstructor
public class ServerManagement {
  private final ApplicationContext application;

  @ShellMethod("Выключить сервер")
  public void stop() {
    Runtime.getRuntime().exit(SpringApplication.exit(application));
  }

  @ShellMethod("Очистить игры")
  public void cleanup() {
    application.getBean(GameSessionRepository.class).deleteAll();
  }
}
