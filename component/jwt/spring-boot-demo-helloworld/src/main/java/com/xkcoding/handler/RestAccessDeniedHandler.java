package com.xkcoding.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.xkcoding.core.Result;

/**
 * @author jiaxiaopeng
 * 2020-07-20 20:34
 **/
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
    AccessDeniedException accessDeniedException) throws IOException, ServletException {
    response.setContentType("application/json;charset=UTF-8");
    ServletOutputStream out = response.getOutputStream();
    String str = JSONObject.toJSONString(Result.of(1, "Denied", "Denied"));
    out.write(str.getBytes("UTF-8"));
    out.flush();
    out.close();
  }


}
