package rps.networking.model;

import lombok.Value;

@Value
public class CommandParameter {
    String name;
    Class expectedType;
}
