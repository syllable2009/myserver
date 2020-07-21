package com.xkcoding.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.xkcoding.filter.JwtAuthenticationTokenFilter;
import com.xkcoding.handler.EntryPointUnauthorizedHandler;
import com.xkcoding.handler.MyAuthenticationFailureHandler;
import com.xkcoding.handler.MyAuthenticationSuccessHandler;
import com.xkcoding.handler.RestAccessDeniedHandler;

/**
 * @author jiaxiaopeng
 * 2020-07-20 20:36
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
  @Autowired
  private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;
  @Autowired
  private RestAccessDeniedHandler restAccessDeniedHandler;
  @Autowired
  private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
  @Autowired
  private MyAuthenticationFailureHandler myAuthenticationFailureHandler;

  // 装载BCrypt密码编码器
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * 从容器中取出 AuthenticationManagerBuilder，执行方法里面的逻辑之后，放回容器
   */
  @Autowired
  public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }


  @Override
  protected void configure(HttpSecurity http) throws Exception {
    /**
     * 在 UsernamePasswordAuthenticationFilter 之前添加 JwtAuthenticationTokenFilter
     */
    http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
    //
    //    http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    //      .and().authorizeRequests()
    //      .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
    //      // 角色校验时，会自动拼接 "ROLE_"
    //      .antMatchers("/user/**").hasAnyRole("ADMIN", "USER")
    //      .antMatchers("/non-auth/**").permitAll()
    //      .anyRequest().authenticated()   // 任何请求,登录后可以访问
    //      .and().formLogin().loginProcessingUrl("/login")
    //      .successHandler(myAuthenticationSuccessHandler)
    //      .failureHandler(myAuthenticationFailureHandler)
    //      .and().headers().cacheControl();
    //
    //    ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry =
    //      http.authorizeRequests();
    //    //让Spring security 放行所有preflight request（cors 预检请求）
    //    registry.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
    //    // 处理异常情况：认证失败和权限不足
        http.exceptionHandling().authenticationEntryPoint(entryPointUnauthorizedHandler)
          .accessDeniedHandler(restAccessDeniedHandler);
      // 由于使用的是JWT，我们这里不需要csrf
    http.csrf().disable()
      // 基于token，所以不需要session
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

      .authorizeRequests()
      //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

      // 允许对于网站静态资源的无授权访问
      .antMatchers(
        HttpMethod.GET,
        "/",
        "/*.html",
        "/favicon.ico",
        "/**/*.html",
        "/**/*.css",
        "/**/*.js"
      ).permitAll()
      // 对于获取token的rest api要允许匿名访问
      //      .antMatchers("/**").permitAll()
      // 除上面外的所有请求全部需要鉴权认证
      .anyRequest().authenticated();

    // 禁用缓存
    http.headers().cacheControl();
  }

  //  @Bean
  //  public CorsFilter corsFilter() {
  //    UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
  //    CorsConfiguration cors = new CorsConfiguration();
  //    cors.setAllowCredentials(true);
  //    cors.addAllowedOrigin("*");
  //    cors.addAllowedHeader("*");
  //    cors.addAllowedMethod("*");
  //    configurationSource.registerCorsConfiguration("/**", cors);
  //    return new CorsFilter(configurationSource);
  //  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  public void configure(org.springframework.security.config.annotation.web.builders.WebSecurity web) {
    web.ignoring().antMatchers(
      "/api/v1/open/**",
      "/v2/api-docs",
      "/actuator/**",
      "/signin.html",
      "/static/**",
      "/assets/**",
      "/",
      "/swagger-ui.html",
      "/login",
      "/register"
    );
  }
}
