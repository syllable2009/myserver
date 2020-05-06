package com.xkcoding.swagger.beauty.rule;

import java.util.ArrayList;
import java.util.Objects;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.jeasy.rules.core.RulesEngineParameters;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.xkcoding.swagger.beauty.rule.req.FactDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiaxiaopeng
 * 2020-05-06 15:15
 **/
@Slf4j
@Service
public class RuleServiceImpl implements RuleService {


  @Override
  public FactDTO getRuleMatch(FactDTO dto) {
    if (Objects.isNull(dto)) {
      return null;
    }
    Long ruleId = dto.getRuleId();
    // 获取头表，构建规则引擎
    // 获取rules,按升序排列，构建规则集合
    ArrayList<RuleDetail> ruleList = Lists.newArrayList();
    // 模拟
    ruleList.add(RuleDetail.builder()
      .id(1L)
      .rule1(">=")
      .value1("20")
      .rule2("<")
      .value2("40")
      .build());
    ruleList.add(RuleDetail.builder()
      .id(2L)
      .rule1(">=")
      .value1("30")
      .rule2("<")
      .value2("50")
      .build());

    Rules rules = new Rules();
    for (RuleDetail rd : ruleList) {
      rules.register(new RuleBuilder()
        .name(rd.getId() + "")
        .when(new CustomCondition(rd))
        .then(new CustomAction(rd))
        .build());
    }

    Facts facts = new Facts();
    facts.put("value", dto.getValue());
    facts.put("match", dto.getMatchs());

    RulesEngineParameters parameters = new
      RulesEngineParameters().skipOnFirstAppliedRule(true)
      .skipOnFirstFailedRule(false)
      .skipOnFirstNonTriggeredRule(false);
    RulesEngine rulesEngine = new DefaultRulesEngine(parameters);
    //    RulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.fire(rules, facts);
    return dto;
  }
}
