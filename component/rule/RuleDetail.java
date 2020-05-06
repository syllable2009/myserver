package com.xkcoding.swagger.beauty.rule;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiaxiaopeng
 * 2020-05-06 11:00
 **/
@Builder
@Data
public class RuleDetail {

  private Long id;
  private String rule1;
  private String value1;
  private String rule2;
  private String value2;
}
