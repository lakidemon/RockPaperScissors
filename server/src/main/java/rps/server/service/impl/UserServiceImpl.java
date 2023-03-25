package rps.server.service.impl;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rps.server.entity.UserEntity;
import rps.server.mapper.UserMapper;
import rps.server.model.UserInfo;
import rps.server.model.UserLoginResult;
import rps.server.model.UserRegisterResult;
import rps.server.repository.UserRepository;
import rps.server.service.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private static final HashFunction SHA512 = Hashing.sha512();
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final Map<String, Long> loggedUsers = new ConcurrentHashMap<>();

  @Override
  public UserLoginResult loginUser(String login, String password) {
    if (loggedUsers.containsKey(login.toLowerCase())) {
      return new UserLoginResult(null, "Игрок уже авторизован");
    }
    var queryResult = userRepository.findByName(login);
    if (queryResult == null) {
      return new UserLoginResult(null, "Игрок не найден");
    }
    if (!SHA512
        .hashString(password, StandardCharsets.UTF_8)
        .equals(HashCode.fromString(queryResult.getPassword()))) {
      return new UserLoginResult(null, "Неверный пароль");
    }
    queryResult.setLastLoginTime(OffsetDateTime.now());
    userRepository.save(queryResult);
    loggedUsers.put(queryResult.getName().toLowerCase(), queryResult.getId());
    var user = userMapper.toUserInfo(queryResult);
    return new UserLoginResult(user, "Успешный вход");
  }

  @Override
  public UserRegisterResult registerUser(String login, String password) {
    if (login.isBlank() || password.isBlank()) {
      return new UserRegisterResult(null, "Логин или пароль не могут быть пустыми");
    }
    var userEntity =
        UserEntity.builder()
            .name(login)
            .password(SHA512.hashString(password, StandardCharsets.UTF_8).toString())
            .build();

    var byName = userRepository.findByName(login);
    if (byName != null) {
      return new UserRegisterResult(null, "Такой игрок уже существует");
    }
    userRepository.save(userEntity);
    return new UserRegisterResult(userMapper.toUserInfo(userEntity), "Успешная регистрация");
  }

  @Override
  public void logoutUser(UserInfo user) {
    loggedUsers.remove(user.getName().toLowerCase());
  }
}
