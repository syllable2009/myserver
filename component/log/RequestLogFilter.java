package com.neo.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class RequestLogFilter extends OncePerRequestFilter implements Ordered {

    private int order = Ordered.LOWEST_PRECEDENCE - 8;

    //    private static final Logger logger = LoggerFactory.getLogger(RepeatedlyReadFilter.class);

    //    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    //            throws IOException, ServletException {
    //        //        logger.debug("复制request.getInputStream流");
    //        ServletRequest requestWrapper = null;
    //        ServletResponse responseWrapper = null;
    //        ContentCachingRequestWrapper wrappedRequest = null;
    //        ContentCachingResponseWrapper wrappedResponse = null;
    //        if (request instanceof HttpServletRequest) {
    //            wrappedRequest = new ContentCachingRequestWrapper(request);
    //            //            requestWrapper = new RepeatedlyRequestWrapper((HttpServletRequest) request);
    //        }
    //        if (response instanceof HttpServletResponse) {
    //            wrappedResponse = new ContentCachingResponseWrapper(response);
    //
    //            //            responseWrapper = new RepeatedlyResponseWrapper((HttpServletResponse) response);
    //        }
    //        chain.doFilter(wrappedRequest, wrappedResponse);
    //    }
    final static List<String> IGNORE_URLS = Lists.newArrayList();

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        StopWatch stopWatch = new StopWatch("api-cost-time");
        String uri = request.getRequestURI();
        stopWatch.start(uri);

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        chain.doFilter(wrappedRequest, wrappedResponse);
        String requestbody = null;
        if (IGNORE_URLS.contains(uri)) {
            requestbody = "request body ignore";
        } else {
            requestbody = getRequestBody(wrappedRequest);
        }
        stopWatch.stop();
        log.info(
                "method: {}, status: {}, path: {}, Locale-Language: {}, query: {}, requestbody: {}, cost:"
                        + " {}",
                request.getMethod(), response.getStatus(), uri,
                request.getLocale().getLanguage(), request.getQueryString(), requestbody,
                stopWatch.prettyPrint());
    }


    private static String getRequestBody(ContentCachingRequestWrapper request) {
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request,
                ContentCachingRequestWrapper.class);
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
