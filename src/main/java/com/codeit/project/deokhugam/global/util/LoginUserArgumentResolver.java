package com.codeit.project.deokhugam.global.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  private static final String USER_ID_HEADER = "Deokhugam-Request-User-ID";

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
    boolean isLongType = parameter.getParameterType().equals(Long.class);

    return hasLoginUserAnnotation && isLongType;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter,
                                ModelAndViewContainer mavContainer,
                                NativeWebRequest webRequest,
                                WebDataBinderFactory binderFactory) throws Exception {

    String header = webRequest.getHeader(USER_ID_HEADER);
    if(header == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_ID_HEADER + " Header에 Deokhugam-Request-User-ID가 누락되었습니다.");
    }

    try {
      return Long.parseLong(header);
    } catch(Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, USER_ID_HEADER + " 헤더가 유효한 형식이 아닙니다.");
    }
  }
}
