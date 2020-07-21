package com.xkcoding.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.xkcoding.bean.JwtUser;
import com.xkcoding.bean.User;
import com.xkcoding.handler.MyAuthenticationSuccessHandler;
import com.xkcoding.service.AuthService;
import com.xkcoding.utils.JwtTokenUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * 2020-07-20 21:28
 **/
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

  @Resource
  private AuthenticationManager authenticationManager;
  @Resource
  private UserDetailsService userDetailsService;
  @Resource
  JwtTokenUtil jwtTokenUtil;

  @Autowired
  private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

  @Override
  public User register(User userToAdd) {
    final String username = userToAdd.getUsername();
    //    if(userRepository.findByUsername(username)!=null) {
    //      return null;
    //    }
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    final String rawPassword = userToAdd.getPassword();
    userToAdd.setPassword(encoder.encode(rawPassword));
    userToAdd.setLastPasswordResetDate(LocalDateTime.now());
    if ("zhangsan".equals(username)) {
      userToAdd.setRoles(Lists.newArrayList("ROLE_USER"));
    } else if ("lisi".equals(username)) {
      userToAdd.setRoles(Lists.newArrayList("ROLE_ADMIN"));
    } else {
      userToAdd.setRoles(Lists.newArrayList());
    }
    return userToAdd;
  }

  @Override
  public JwtUser login(String username, String password) {
    UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
    final Authentication authentication = authenticationManager.authenticate(upToken);
    if (Objects.isNull(authentication) || Objects.isNull(authentication.getPrincipal())) {
      throw new UsernameNotFoundException("登陆失败");
      // BadCredentialsException
      // BusinessException
    }
    log.error("authentication:{}", authentication);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    //    rememberMeServices.loginSuccess(request, response, authentication);
    //    myAuthenticationSuccessHandler.onAuthenticationSuccess(request, response,authentication);
    JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
    //    final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    final String token = jwtTokenUtil.generateToken(jwtUser);
    jwtUser.setToken(jwtTokenUtil.getTokenHead() + token);
    return jwtUser;
  }

  @Override
  public String refresh(String oldToken) {
    final String token = oldToken.substring(jwtTokenUtil.getTokenHead().length());
    String username = jwtTokenUtil.getUsernameFromToken(token);
    JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
    if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
      return jwtTokenUtil.refreshToken(token);
    }


    return null;
  }
}
