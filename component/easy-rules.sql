-- auto-generated definition
CREATE TABLE rule_info
(
  id                               BIGINT AUTO_INCREMENT
    PRIMARY KEY COMMENT '唯一自增id',
  rule_type                        VARCHAR(128)                       NULL
  COMMENT '规则归属业务',
  rule_name                        VARCHAR(128)                       NULL
  COMMENT '规则名称',
  skip_on_first_applied_rule       TINYINT(1)                         NOT NULL
  COMMENT '从第一条开始，匹配一条就会跳过后面规则匹配，不匹配则一直往下执行',
  skip_on_first_non_triggered_rule TINYINT(1)                         NOT NULL
  COMMENT '从第一条开始，匹配一条才会往下执行，不匹配则跳过后面',
  skip_on_first_failed_rule        TINYINT(1) DEFAULT '1'             NOT NULL
  COMMENT '如果执行条件判断时发生异常就会跳过后面规则匹配',
  rule_priority_threshold          INT                                NULL
  COMMENT '大于指定的优先级则不进行匹配,从0开始',
  create_id                        VARCHAR(11)                        NULL
  COMMENT '创建人id',
  create_time                      DATETIME                           NULL
  COMMENT '创建时间',
  update_id                        VARCHAR(11)                        NULL
  COMMENT '更新人id',
  update_time                      DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL
  COMMENT '更新时间',
  remark                           VARCHAR(1000)                      NULL
  COMMENT '备注'
)
  COMMENT '规则信息维护表'
  ENGINE = InnoDB;


-- auto-generated definition
CREATE TABLE rule_detail
(
  id                               BIGINT AUTO_INCREMENT
    PRIMARY KEY COMMENT '唯一自增id',
  pid BIGINT NOT NULL   COMMENT '父id',
  rule_1                        VARCHAR(128)                       NULL
  COMMENT '规则1，< == > <= >= equals',
  value_1                        VARCHAR(128)                       NULL
  COMMENT '规则值1',

  rule_2                        VARCHAR(128)                       NULL
  COMMENT '规则2，< == > <= >= equals',
  value_2                        VARCHAR(128)                       NULL
  COMMENT '规则值2',
  if_active        TINYINT(1) DEFAULT '1'             NOT NULL
  COMMENT '1-有效，0-无效',
  remark                           VARCHAR(1000)                      NULL
  COMMENT '备注'
)
  COMMENT '规则详细信息表'
  ENGINE = InnoDB;