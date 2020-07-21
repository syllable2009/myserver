package com.xkcoding.service.impl;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.xkcoding.bean.JwtUser;

/**
 * @author jiaxiaopeng
 * 2020-07-20 20:12
 **/
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

  //  @Autowired
  //  private UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //    User user = userService.findByUsername(username);
    JwtUser user = new JwtUser();
    if (!"zhangsan".equals(username) && !"lisi".equals(username)) {
      throw new UsernameNotFoundException(String.format("%s.这个用户不存在", username));
    }
    //    List<SimpleGrantedAuthority> authorities =
    //      user.getRoles().stream().map(Role::getRolename).map(SimpleGrantedAuthority::new).collect(Collectors
    // .toList());
    //    return new JwtUser(user.getUsername(), user.getPassword(), user.getState(), authorities);
    JwtUser jwtUser = new JwtUser();
    //    user.getUsername(), user.getPassword(), "123", Collections.EMPTY_LIST
    jwtUser.setUsername(username);
    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    jwtUser.setPassword(bCryptPasswordEncoder.encode("password"));

    List<SimpleGrantedAuthority> list = Lists.newArrayList();

    if ("zhangsan".equals(username)) {
      SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_USER");
      list.add(sga);
      jwtUser.setAuthorities(list);
    } else if ("lisi".equals(username)) {
      SimpleGrantedAuthority sga = new SimpleGrantedAuthority("ROLE_ADMIN");
      list.add(sga);
    } else {
      jwtUser.setAuthorities(Lists.newArrayList());
    }


    return jwtUser;
  }
}
