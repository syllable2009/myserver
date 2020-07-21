package com.xkcoding.bean;

import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * 2020-07-21 14:04
 **/
@NoArgsConstructor
public class JwtUserFactory {

  public static JwtUser create(User user) {
    // 数据对象转返回对象
    return new JwtUser(
      //      user.getId(),
      //      user.getUsername(),
      //      user.getPassword(),
      //      user.getEmail(),
      //      mapToGrantedAuthorities(user.getRoles()),
      //      user.getLastPasswordResetDate()
    );
  }

  //  private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
  //    return authorities.stream()
  //      .map(SimpleGrantedAuthority::new)
  //      .collect(Collectors.toList());
  //  }
}
