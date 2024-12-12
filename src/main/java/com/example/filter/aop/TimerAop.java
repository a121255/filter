package com.example.filter.aop;

import com.example.filter.model.UserRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Aspect
@Component
public class TimerAop {

    // userapicontroller의 메서드 전후
    @Pointcut(value = "within(com.example.filter.controller.UserApiController)")
    public void timerPointCut(){}

    @Before(value = "timerPointCut()")
    public void before(JoinPoint joinPoint){
        System.out.println("before");
    }

    @After(value = "timerPointCut()")
    public void after(JoinPoint joinPoint){
        System.out.println("after");
    }


    @AfterReturning(value = "timerPointCut()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result){
        System.out.println("after returning");
    }

    @AfterThrowing(value = "timerPointCut()", throwing = "tx")
    public void afterThrowing(JoinPoint joinPoint, Throwable tx){
        System.out.println("after throwing");
    }

    @Around(value = "timerPointCut()")  // 메서드 이름 지정, 이러한 pointcut 식을 사용해서 around 적용하겠다
    public void around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("메서도 실행 이전");

        joinPoint.getArgs(); // 메서드의 모든 매개변수 가져옴

        Arrays.stream(joinPoint.getArgs()).forEach(
                it -> {
                    System.out.println(it);
                    if(it instanceof UserRequest){
                        var tempUser = (UserRequest) it;
                        var phoneNumber = tempUser.getPhoneNumber().replace("-","");
                        tempUser.setPhoneNumber(phoneNumber);
                    }
                }
        );

        // 암/복호화 / 로깅 (개인정보...)
        var newObjs = Arrays.asList(
                new UserRequest()
        );
        var stopWatch = new StopWatch();
        stopWatch.start();

        joinPoint.proceed(newObjs.toArray());

        stopWatch.stop();

        System.out.println("총 소요된 시간 MS = "+stopWatch.getTotalTimeMillis());

        System.out.println("메서도 실행 이후");
    }

}
