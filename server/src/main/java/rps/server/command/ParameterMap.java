package rps.server.command;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParameterMap {
  private final Map<String, Object> parameters;

  public <T> T getParam(String name) {
    return (T) parameters.get(name);
  }
}
