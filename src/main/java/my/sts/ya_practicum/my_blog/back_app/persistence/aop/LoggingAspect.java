package my.sts.ya_practicum.my_blog.back_app.persistence.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("dev")
@Slf4j
public class LoggingAspect {

    @Around("execution(* my.sts.ya_practicum.my_blog.back_app.persistence.repository..*.*(..))")
    public Object logDaoMethodsExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        log.info("{} executed in {}ms", joinPoint.getSignature().toShortString(), executionTime);

        return result;
    }
}
