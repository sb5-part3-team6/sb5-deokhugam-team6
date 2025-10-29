package com.codeit.project.deokhugam.global.log;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class MDCLoggingInterceptor implements HandlerInterceptor {

  public static final String REQUEST_ID = "requestId";
  public static final String REQUEST_METHOD = "requestMethod";
  public static final String REQUEST_URI = "requestUri";
  public static final String CLIENT_IP = "ip";

  public static final String REQUEST_ID_HEADER = "Deokhugam-Request-ID";
  public static final String REQUEST_USER_ID_HEADER = "Deokhugam-Request-User-ID";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {

    String requestId = UUID.randomUUID()
                           .toString()
                           .replaceAll("-", "");

    String userId = request.getHeader(REQUEST_USER_ID_HEADER);

    String clientIp = getClientIp(request);

    MDC.put(REQUEST_ID, requestId);
    MDC.put(REQUEST_METHOD, request.getMethod());
    MDC.put(REQUEST_URI, request.getRequestURI());
    MDC.put(CLIENT_IP, clientIp != null ? clientIp : "UNKNOWN");
    MDC.put("userId", userId != null ? userId : "UNKNOWN");

    response.setHeader(REQUEST_ID_HEADER, requestId);

    log.debug("Request started");
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) {
    log.debug("Request completed");
    MDC.clear();
  }

  private String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("X-Forwarded-For");

    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }

    if (ip != null && ip.contains(",")) {
      ip = ip.split(",")[0];
    }

    return ip;
  }
}