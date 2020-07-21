package com.xkcoding.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alibaba.fastjson.JSON;
import com.xkcoding.utils.JwtTokenUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * 2020-07-20 19:53
 **/
@Slf4j
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private JwtTokenUtil jwtTokenUtil;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws
    ServletException, IOException {
    log.info("request:{}", JSON.toJSONString(request.getHeaderNames()));
    String token = request.getHeader(jwtTokenUtil.getHeader());
    log.info("JwtAuthenticationTokenFilter:token:{}", token);
    if (!StringUtils.isEmpty(token) && token.startsWith(jwtTokenUtil.getTokenHead())) {
      final String authToken = token.substring(jwtTokenUtil.getTokenHead().length());
      String username = jwtTokenUtil.getUsernameFromToken(authToken);
      log.info("checking authentication:{}" + username);
      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.validateToken(authToken, userDetails)) {
          // 将用户信息存入 authentication，方便后续校验
          UsernamePasswordAuthenticationToken
            authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          // 将 authentication 存入 ThreadLocal，方便后续获取用户信息
          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    }
    chain.doFilter(request, response);
  }
}
