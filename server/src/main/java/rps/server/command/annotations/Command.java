package rps.server.command.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import rps.server.game.UserState;

@Repeatable(Commands.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {
  String value();
  
  String category();

  String description();

  CommandParam[] params() default {};

  boolean announceCommandsOnSuccess() default false;

  boolean announceToClient() default true;

  UserState[] allowedStates() default {};
}
