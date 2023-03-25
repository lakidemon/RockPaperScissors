package rps.server.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import rps.networking.model.CommandFormat;
import rps.networking.model.CommandParameter;
import rps.server.command.CommandExecutor;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface CommandMapper {

    @Mapping(source = "command", target = "name")
    @Mapping(source = "allowedParams", target = "params")
    CommandFormat toCommandFormat(CommandExecutor executor);

    @Mapping(source = "key", target = "name")
    @Mapping(source = "value", target = "expectedType")
    CommandParameter toCommandParameter(Map.Entry<String, Class> param);

    default List<CommandParameter> toCommandParameters(Map<String, Class> params) {
        return params.entrySet().stream().map(this::toCommandParameter).collect(Collectors.toList());
    }
}
