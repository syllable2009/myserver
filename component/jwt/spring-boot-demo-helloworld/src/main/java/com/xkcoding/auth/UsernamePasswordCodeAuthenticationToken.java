package com.xkcoding.auth;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * @author jiaxiaopeng
 * 2020-07-21 11:23
 **/
public class UsernamePasswordCodeAuthenticationToken extends UsernamePasswordAuthenticationToken {

  private Object validCode;

  public UsernamePasswordCodeAuthenticationToken(Object principal, Object credentials, Object validCode) {
    super(principal, credentials);
    this.validCode = validCode;
  }

  public UsernamePasswordCodeAuthenticationToken(Object principal, Object credentials, Object validCode,
    Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
    this.validCode = validCode;
  }
}
