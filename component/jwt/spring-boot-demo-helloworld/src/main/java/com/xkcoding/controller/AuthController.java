package com.xkcoding.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.xkcoding.bean.JwtUser;
import com.xkcoding.bean.User;
import com.xkcoding.service.AuthService;

/**
 * @author jiaxiaopeng
 * 2020-07-20 20:55
 **/

@RestController
public class AuthController {
  @Value("${jwt.header}")
  private String tokenHeader;

  @Autowired
  private AuthService authService;

  @RequestMapping(value = "${jwt.route.authentication.path}", method = RequestMethod.POST)
  //  @RequestMapping(value = "/login", method = RequestMethod.POST)
  public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) throws AuthenticationException {
    JwtUser JwtUser = authService.login(user.getUsername(), user.getPassword());
    return ResponseEntity.ok(JwtUser);
  }

  @RequestMapping(value = "${jwt.route.authentication.refresh}", method = RequestMethod.GET)
  public ResponseEntity<?> refreshAndGetAuthenticationToken(
    HttpServletRequest request) throws AuthenticationException {
    String token = request.getHeader(tokenHeader);
    String refreshedToken = authService.refresh(token);
    if (refreshedToken == null) {
      return ResponseEntity.badRequest().body(null);
    } else {
      return ResponseEntity.ok(refreshedToken);
    }
  }

  @RequestMapping(value = "${jwt.route.authentication.register}", method = RequestMethod.POST)
  public User register(@RequestBody User addedUser) throws AuthenticationException {
    return authService.register(addedUser);
  }
}
