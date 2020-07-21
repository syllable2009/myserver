package com.xkcoding.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.xkcoding.core.Result;

/**
 * @author jiaxiaopeng
 * 2020-07-20 20:37
 **/
@Component
public class EntryPointUnauthorizedHandler implements AuthenticationEntryPoint {
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
    throws IOException, ServletException {
    response.setContentType("application/json;charset=UTF-8");
    ServletOutputStream out = response.getOutputStream();
    String str = JSONObject.toJSONString(Result.of(1, "Unauthorized", "Unauthorized"));
    out.write(str.getBytes("UTF-8"));
    out.flush();
    out.close();
  }
}
