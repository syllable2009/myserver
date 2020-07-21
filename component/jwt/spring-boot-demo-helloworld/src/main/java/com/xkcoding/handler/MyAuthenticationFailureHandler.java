package com.xkcoding.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.xkcoding.core.Result;

/**
 * @author jiaxiaopeng
 * 2020-07-20 20:31
 **/
@Component
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
  @Override
  public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
    AuthenticationException e) throws IOException, ServletException {

    httpServletResponse.setContentType("application/json;charset=UTF-8");
    ServletOutputStream out = httpServletResponse.getOutputStream();
    String str = JSONObject.toJSONString(Result.of(1, "fail", "fail"));
    out.write(str.getBytes("UTF-8"));
    out.flush();
    out.close();
  }
}
