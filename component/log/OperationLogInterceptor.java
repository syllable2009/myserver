package com.neo.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Slf4j
@Component
@Order(22)
public class OperationLogInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            log.info("handler not instanceof HandlerMethod");
            return;
        }

        //                RepeatedlyRequestWrapper requestWrapper = (RepeatedlyRequestWrapper) request;
        //        RepeatedlyResponseWrapper reponseWrapper = (RepeatedlyResponseWrapper) response;

        ContentCachingRequestWrapper requestWrapper = (ContentCachingRequestWrapper) request;
        ContentCachingResponseWrapper reponseWrapper = (ContentCachingResponseWrapper) response;

        String requestBody = getRequestBody(requestWrapper);
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        log.info("enter OperationLogInterceptor postHandle");

        LogOperation operationLogAnnotation = handlerMethod.getMethodAnnotation(LogOperation.class);
        if (Objects.isNull(operationLogAnnotation)) {
            log.info("no @LogOperation at method");
            return;
        }

        String reqMethod = request.getMethod();
        int respStatus = response.getStatus();
        String matchedPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        log.info("matchedPattern:{}", matchedPattern);
        String queryString = request.getQueryString();
        String contextPath = request.getContextPath();
        String reqUri = request.getRequestURI().replace(contextPath, "");
        //                byte[] bytes = IOUtils.toByteArray(request.getInputStream());
        //        String body = new String(bytes, "UTF-8");

        // 获取用户名
        String username = "syllable";

        String operationCnName = getOperationCnName(handlerMethod, operationLogAnnotation, matchedPattern);

        // 获取真实的ip地址
        String clientIp = "127.0.0.1";
        String classMethod = handlerMethod.toString();

        //        StringBuilder urlBuilder = new StringBuilder();
        //        urlBuilder.append(reqUri);
        //        if (queryString != null) {
        //            urlBuilder.append("?");
        //            urlBuilder.append(queryString);
        //        }

        // 取出方法路径，url args, post body等等
        log.info(
                "OperationLogInterceptor username: {}, operationCnName {} clientIp: {}, method: {},respStatus: {}, "
                        + "path:"
                        + " {}, matchedPattern: {}, query: <{}>, body: {}, resp:{}",
                username, operationCnName, clientIp, reqMethod, respStatus, reqUri, matchedPattern,
                queryString, requestBody, getResponseBody(reponseWrapper));
        // 保存操作日志
        //        AcOperateLog operateLog = new AcOperateLog();
        //        operateLog.setOperator(username);
        //        operateLog.setIp(clientIp);
        //        operateLog.setOperationName(operationCnName);
        //
        //        operateLog.setHttpMethod(reqMethod);
        //        operateLog.setReqUrl(urlBuilder.toString());
        //        operateLog.setClassMethod(classMethod);
        //        operateLog.setReqBody(body);
        //        //        operateLog.setRespBody(response.);
        //
        //        acOperateLogService.save(operateLog);
        // 保存成OperationLog对象
        reponseWrapper.copyBodyToResponse();

    }

    private String getOperationCnName(HandlerMethod handlerMethod, LogOperation operationAnnotation,
                                      String matchedPattern) {
        //        String operationCnName = operationAnnotation.operationCnName();
        //        if (!StringUtils.isEmpty(operationCnName)) {
        //            return operationCnName;
        //        }
        //        // 查看是否有其他@ApiOperation
        //        ApiOperation apiOperationTag = handlerMethod.getMethodAnnotation(ApiOperation.class);
        //        if (apiOperationTag != null && !StringUtils.isEmpty(apiOperationTag.value())) {
        //            return apiOperationTag.value();
        //        }

        // 都没有，就得用匹配的path
        //        return matchedPattern;
        return "添加应用";
    }


    private static String getRequestBody(ContentCachingRequestWrapper request) throws JsonProcessingException {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request,
                ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            return byteToString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
        }
        return null;
    }

    private static String getResponseBody(ContentCachingResponseWrapper responseWrapper) throws IOException {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(responseWrapper,
                ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            return byteToString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
        }
        return null;
    }

    private static String byteToString(byte[] buf, String characterEncoding) {

        if (null != buf && buf.length > 0) {
            String payload;
            try {
                payload = new String(buf, 0, buf.length, characterEncoding);
            } catch (UnsupportedEncodingException ex) {
                payload = "[unknown]";
            }
            return payload.replaceAll("\r|\n|\t", "");
        }
        return null;
    }


}
