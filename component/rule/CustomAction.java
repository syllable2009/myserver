package com.xkcoding.swagger.beauty.rule;

import java.util.ArrayList;

import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Facts;

import lombok.AllArgsConstructor;

/**
 * @author jiaxiaopeng
 * 2020-04-30 16:27
 **/
@AllArgsConstructor
public class CustomAction implements Action {

  private RuleDetail ruleDetail;

  @Override
  public void execute(Facts facts) throws Exception {
    System.out.println("------->action<--------");
    ArrayList match = (ArrayList) facts.get("match");
    match.add(ruleDetail);
  }

}
