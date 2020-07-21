package com.xkcoding.service;

import com.xkcoding.bean.JwtUser;
import com.xkcoding.bean.User;

public interface AuthService {

  User register(User userToAdd);

  JwtUser login(String username, String password);

  String refresh(String oldToken);
}
