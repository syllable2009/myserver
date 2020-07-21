package com.xkcoding.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.xkcoding.bean.JwtUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * 2020-07-20 19:01
 **/
@Slf4j
@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtTokenUtil {
  // 加密密钥
  private String secret;
  // 过期时间 毫秒
  private Long expiration;
  // 获取认证的头名称
  private String header;
  // 7位token前缀码
  private String tokenHead;


  /**
   * 从数据声明生成令牌
   *
   * @param claims 数据声明
   * @return 令牌
   */
  private String generateToken(Map<String, Object> claims) {
    Date expirationDate = new Date(System.currentTimeMillis() + expiration);
    return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, secret)
      .compact();
  }

  /**
   * 从令牌中获取数据声明
   *
   * @param token 令牌
   * @return 数据声明
   */
  private Claims getClaimsFromToken(String token) {
    Claims claims;
    try {
      claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    } catch (Exception e) {
      claims = null;
    }
    return claims;
  }


  /**
   * 生成令牌
   *
   * @param userDetails 用户
   * @return 令牌
   */
  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>(2);
    claims.put(Claims.SUBJECT, userDetails.getUsername());
    claims.put(Claims.ISSUED_AT, new Date());
    return generateToken(claims);
  }

  /**
   * 从令牌中获取用户名
   *
   * @param token 令牌
   * @return 用户名
   */
  public String getUsernameFromToken(String token) {
    String username;
    try {
      Claims claims = getClaimsFromToken(token);
      username = claims.getSubject();
    } catch (Exception e) {
      username = null;
    }
    return username;
  }

  /**
   * 判断令牌是否过期
   *
   * @param token 令牌
   * @return 是否过期
   */
  public Boolean isTokenExpired(String token) {
    try {
      Claims claims = getClaimsFromToken(token);
      Date expiration = claims.getExpiration();
      log.error("expiration:{}", expiration.toLocaleString());
      return expiration.before(new Date());
    } catch (Exception e) {
      log.error("isTokenExpired error{}", e);
    }
    return true;
  }

  /**
   * 刷新令牌
   *
   * @param token 原令牌
   * @return 新令牌
   */
  public String refreshToken(String token) {
    String refreshedToken;
    try {
      Claims claims = getClaimsFromToken(token);
      claims.put(Claims.ISSUED_AT, new Date());
      refreshedToken = generateToken(claims);
    } catch (Exception e) {
      refreshedToken = null;
    }
    return refreshedToken;
  }

  /**
   * 验证令牌
   *
   * @param token 令牌
   * @param userDetails 用户
   * @return 是否有效
   */
  public Boolean validateToken(String token, UserDetails userDetails) {
    JwtUser user = (JwtUser) userDetails;
    String username = getUsernameFromToken(token);
    log.info("username:{},db:{}", username, user);
    return (username.equals(user.getUsername()) && BooleanUtils.isFalse(isTokenExpired(token)));
  }


  public Date getCreatedDateFromToken(String token) {
    Date created;
    try {
      final Claims claims = getClaimsFromToken(token);
      created = new Date((Long) claims.get("created"));
    } catch (Exception e) {
      created = null;
    }
    return created;
  }


  public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
    final Date created = getCreatedDateFromToken(token);
    return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
      && !isTokenExpired(token);
  }

  private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
    return (lastPasswordReset != null && created.before(lastPasswordReset));
  }
}
