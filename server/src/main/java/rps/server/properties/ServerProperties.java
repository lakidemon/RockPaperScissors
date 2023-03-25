package rps.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "game.server")
public record ServerProperties(String host, int tcpPort) {}
