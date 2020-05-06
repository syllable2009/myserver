package com.xkcoding.swagger.beauty.rule.req;

import java.util.List;

import com.google.common.collect.Lists;
import com.xkcoding.swagger.beauty.rule.RuleDetail;

import lombok.Builder;
import lombok.Data;

/**
 * @author jiaxiaopeng
 * 2020-05-06 15:17
 **/
@Builder
@Data
public class FactDTO {

  private String value;

  private Long ruleId;

  @Builder.Default
  List<RuleDetail> matchs = Lists.newArrayList();
}
