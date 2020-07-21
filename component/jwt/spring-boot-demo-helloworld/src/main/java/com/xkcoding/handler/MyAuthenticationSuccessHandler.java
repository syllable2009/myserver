package com.xkcoding.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.xkcoding.core.Result;
import com.xkcoding.utils.JwtTokenUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * 2020-07-20 20:26
 **/
@Component
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

  @Autowired
  private JwtTokenUtil jwtTokenUtil;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
    Authentication authentication) throws IOException, ServletException {
    UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String token = jwtTokenUtil.generateToken(userDetails);
    renderToken(httpServletResponse, token);
  }

  /**
   * 渲染返回 token 页面,因为前端页面接收的都是Result对象，故使用application/json返回
   */
  public void renderToken(HttpServletResponse response, String token) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    ServletOutputStream out = response.getOutputStream();
    String str = JSONObject.toJSONString(Result.succes(token));
    out.write(str.getBytes("UTF-8"));
    out.flush();
    out.close();
  }
}
