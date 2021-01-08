package com.neo.interceptor;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogOperation {

    // 操作中文名称
    String operationCnName() default "";

    // 操作对象
    NodeType type();

    // 操作类型
    OperationType operation();
}
