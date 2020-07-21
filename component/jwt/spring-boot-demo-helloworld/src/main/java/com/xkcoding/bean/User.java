package com.xkcoding.bean;

import java.time.LocalDateTime;
import java.util.Collection;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiaxiaopeng
 * 2020-07-20 21:28
 **/
@Data
@Builder
public class User {
  String username;
  String password;
  LocalDateTime lastPasswordResetDate;
  Collection<String> roles;
}
