package com.xkcoding.bean;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jiaxiaopeng
 * 2020-07-20 18:58
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser implements UserDetails {

  private String username;

  @JsonIgnore
  private String password;

  private String validCode;

  private Date lastPasswordResetDate;

  private List<String> roleList;

  private String token;

  private Collection<? extends GrantedAuthority> authorities;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  // 账户是否未过期
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  // 账户是否未被锁
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  //  public static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
  //    return authorities.stream()
  //      .map(SimpleGrantedAuthority::new)
  //      .collect(Collectors.toList());
  //  }

  public void setRoleList(List<String> roleList) {
    this.roleList = roleList;
    this.authorities = roleList.stream()
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());
  }
}
