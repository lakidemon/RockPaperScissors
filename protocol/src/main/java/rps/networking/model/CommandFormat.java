package rps.networking.model;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CommandFormat {
  String name;
  String category;
  String description;
  List<CommandParameter> params;
}
