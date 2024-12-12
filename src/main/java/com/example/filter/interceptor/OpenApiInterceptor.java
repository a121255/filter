package com.example.filter.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class OpenApiInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Object handler : 앞으로 가야 할 controller의 정보가 담김
        // 이것을 HandlerMethod로 캐스팅하고, 얘가 가지고 있는 HandlerMethod 중 메소드가 어노테이션을 가지고 있는가를 체크

        log.info("pre handle");
        // controller 전달, false 전달 안 함

        var handlerMethod = (HandlerMethod) handler;

        var methodLevel = handlerMethod.getMethodAnnotation(OpenApi.class); // 해당 컨트롤러 또는 메서드에 OpenApi 어노테이션을 가지고 있는가?
        if(methodLevel != null){
            log.info("method level");
            return true;
        }

        var classLevel = handlerMethod.getBeanType().getAnnotation(OpenApi.class); // 해당 class에 어노테이션이 있냐?
        if(classLevel != null){
            log.info("class level");
            return true;
        }

        log.info("open api 아닙니다 : {}", request.getRequestURI());
        return false;   // controller로 내용을 보내지 않겠다
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // ModelAndView가 특징, 리턴형이 void, 화면에 뷰가 연결 됐을 때 호출
        log.info("post handle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("after completion");
    }
}
