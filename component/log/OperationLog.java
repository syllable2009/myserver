package com.neo.interceptor;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Data
public class OperationLog implements Serializable {

    private static final long serialVersionUID = -1L;

    private Long id;

    //如果多应用作为区分可以加上
    private String appId;

    // 业务类型,面试管理，日常报销，职位管理等
    private String businessType;

    // 操作类型:增加，删除，修改，查询
    private String operationType;

    // 操作描述，你可以自己定义，或者从swagger的注解上获取
    private String operationDesc;

    // 请求ip
    private String reqIp;

    // 请求方式
    private String reqMethod;

    // 请求uri
    private String reqUri;

    // 请求local-language
    private String reqLocale;

    // 请求url参数
    private String reqQueryString;

    // 请求json
    private String reqBody;

    // 匹配到的路径
    private String matchedPattern;

    // 请求方法
    private String classMethod;

    //reponseStatus
    private String respStatus;

    // 响应内容
    private String respBody;

    /**
     * 创建人即为操作人
     */
    private String createId;

    /**
     * 创建时间即为操作时间
     */
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
