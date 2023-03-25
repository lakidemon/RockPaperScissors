package rps.server.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import rps.networking.packets.server.ServerResponse;
import rps.server.entity.UserEntity;
import rps.server.model.UserInfo;
import rps.server.model.UserLoginResult;
import rps.server.model.UserRegisterResult;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

  ServerResponse toServerResponse(UserRegisterResult registerResult);

  ServerResponse toServerResponse(UserLoginResult loginResult);

  UserInfo toUserInfo(UserEntity userEntity);
}
