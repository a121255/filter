package com.example.filter.config;

import com.example.filter.interceptor.OpenApiInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired  // RequiredArgsConstructor에서 private final로 선언해줘도 됨
    private OpenApiInterceptor openApiInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(openApiInterceptor)
                .addPathPatterns("/**")    // 모든주소에 openApiInterceptor 달아주겠다
                ;

        // registry.addInterceptor(openApiInterceptor) // 가장 먼저 add한 Interceptor부터 실행, 또는 order 줄 수 있음
    }
}
