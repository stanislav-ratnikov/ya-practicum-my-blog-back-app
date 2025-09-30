package my.sts.ya_practicum.my_blog.back_app.persistence.util.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Profile("dev")
public class LoggingAspect {

    @Around("execution(* my.sts.ya_practicum.my_blog.back_app.persistence.repository..*.*(..))")
    public Object logDaoMethodsExecution(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("[" + joinPoint.getSignature().toShortString() + "] executing..");

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        System.out.println("[" + joinPoint.getSignature().toShortString() + "] execution finished in " + executionTime + "ms");

        return result;
    }
}
