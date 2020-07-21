package com.xkcoding.controller;

/**
 * @author jiaxiaopeng
 * 2020-07-20 21:08
 **/

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

/**
 * 在 @PreAuthorize 中我们可以利用内建的 SPEL 表达式：比如 'hasRole()' 来决定哪些用户有权访问。
 * 需注意的一点是 hasRole 表达式认为每个角色名字前都有一个前缀 'ROLE_'。所以这里的 'ADMIN' 其实在
 * 数据库中存储的是 'ROLE_ADMIN' 。这个 @PreAuthorize 可以修饰Controller也可修饰Controller中的方法。
 **/
@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('USER')")
public class UserController {
  //  @Autowired
  //  private UserRepository repository;

  @RequestMapping(method = RequestMethod.GET)
  public List<String> getUsers() {

    ArrayList<String> dataList = Lists.newArrayList("张三", "李四", "用户");
    return dataList;
  }

  // 略去其它部分
}
