package com.xkcoding.swagger.beauty.rule;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.jeasy.rules.core.RulesEngineParameters;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * 2020-05-06 11:01
 **/

@Slf4j
public class Launcher {


  public static void main(String[] args) throws Exception {
    //    ArrayList<RuleDetail> ruleList = Lists.newArrayList();

    // 准备数据
    Facts facts = new Facts();
    facts.put("value", 30);
    facts.put("match", Lists.newArrayList());


    RuleDetail rt = RuleDetail.builder()
      .rule1("=>")
      .value1("20")
      .rule2("<")
      .value2("40")
      .build();

    Rule airConditioningRule = new RuleBuilder()
      .name("name")
      .when(new CustomCondition(rt))
      .then(new CustomAction(rt))
      .build();
    Rule airConditioningRule2 = new RuleBuilder()
      .name("name2")
      .when(new CustomCondition(rt))
      .then(new CustomAction(rt))
      .build();

    Rules rules = new Rules();
    rules.register(airConditioningRule);
    rules.register(airConditioningRule2);

    // fire rules on known facts
    RulesEngineParameters parameters = new
      RulesEngineParameters().skipOnFirstAppliedRule(true)
      .skipOnFirstFailedRule(false)
      .skipOnFirstNonTriggeredRule(false);
    RulesEngine rulesEngine = new DefaultRulesEngine(parameters);
    //    RulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.fire(rules, facts);
    log.info("{}", facts);
  }
}
