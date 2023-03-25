package rps.server.service;

import rps.server.model.UserInfo;
import rps.server.model.UserLoginResult;
import rps.server.model.UserRegisterResult;

public interface UserService {
  UserLoginResult loginUser(String login, String password);

  UserRegisterResult registerUser(String login, String password);
  
  void logoutUser(UserInfo user);
}
