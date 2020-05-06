package com.xkcoding.swagger.beauty.rule;


import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Getter
public enum RuleEnum {


  GREATER_THEN(">", "大于"),
  LESS_THEN("<", "小于"),
  EQUAL("=", "等于"),
  GREATER_THEN_EQUAL(">=", "大于等于"),
  LESS_THEN_EQUAL("<=", "小于等于"),
  EQUALS("equals", "字符等于");

  private String code;
  private String value;


  public static final Map<String, RuleEnum> MAP = Arrays.stream(values())
    .collect(Collectors.toMap(RuleEnum::getCode, Function.identity()));

  public static RuleEnum of(String code) {
    if (StringUtils.isEmpty(code)) {
      return null;
    }
    return MAP.get(code);
  }

  public static boolean compare(RuleDetail rd, String d) {
    boolean b = ruleCompare(rd.getRule1(), rd.getValue1(), d);
    if (StringUtils.isEmpty(rd.getRule2()) || StringUtils.isEmpty(rd.getValue2())) {
      return b;
    } else {
      return b && ruleCompare(rd.getRule2(), rd.getValue2(), d);
    }
  }

  private static boolean ruleCompare(String code, String value, String input) {

    RuleEnum ruleEnum = RuleEnum.of(code);
    log.error("{}-{}", code, ruleEnum);
    if (Objects.isNull(ruleEnum)) {
      return false;
    }
    switch (ruleEnum) {
      case GREATER_THEN:
        return Double.valueOf(input) > Double.valueOf(value);
      case LESS_THEN:
        return Double.valueOf(input) < Double.valueOf(value);
      case EQUAL:
        return Double.valueOf(input).equals(Double.valueOf(value));
      case GREATER_THEN_EQUAL:
        return Double.valueOf(input) >= Double.valueOf(value);
      case LESS_THEN_EQUAL:
        return Double.valueOf(input) <= Double.valueOf(value);
      case EQUALS:
        return value.equals(input);
      default:
        return false;
    }
  }

}
