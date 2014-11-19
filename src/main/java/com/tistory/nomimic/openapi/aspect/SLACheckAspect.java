package com.tistory.nomimic.openapi.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Lucas,Lee on 14. 11. 19..
 */
@Aspect
@Component
public class SLACheckAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(SLACheckAspect.class);

    private AtomicInteger count = new AtomicInteger(0);

    private static final int DEFAULT_MAX_COUNT = 10;

    public void setCount(AtomicInteger count) {
        this.count = count;
    }

    /**
     * com.tistory.nomimic.openapi.aspect.SLACheck 어노테이션이 선언된 함수에서 동작
     *
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */

    @Around("@annotation(com.tistory.nomimic.openapi.aspect.SLACheck)")
    public Object serviceLicenceCheck(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        int currentCount = count.incrementAndGet();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String client_id = authentication.getPrincipal().toString();

        if(currentCount > DEFAULT_MAX_COUNT) {
            //현재 카운터가 최대치를 초과한 경우에 오류 리턴
            throw new RuntimeException("Exceeded the number of times that can utilize the service");
        }

        return proceedingJoinPoint.proceed();
    }
}
