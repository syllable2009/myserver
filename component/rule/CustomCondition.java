package com.xkcoding.swagger.beauty.rule;

import org.jeasy.rules.api.Condition;
import org.jeasy.rules.api.Facts;

import lombok.AllArgsConstructor;

/**
 * @author jiaxiaopeng
 * 2020-04-30 16:25
 **/
@AllArgsConstructor
public class CustomCondition implements Condition {

  private RuleDetail ruleDetail;

  @Override
  public boolean evaluate(Facts facts) {
    System.out.println("------->condition start<-------");
    return RuleEnum.compare(ruleDetail, facts.get("value"));
  }

}
