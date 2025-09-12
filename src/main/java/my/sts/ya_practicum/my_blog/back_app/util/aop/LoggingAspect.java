package my.sts.ya_practicum.my_blog.back_app.util.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* my.sts.ya_practicum.my_blog.back_app.dao..*.*(..))")
    public Object logDaoMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("[" + joinPoint.getSignature().toShortString() + "] executing..");

        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        System.out.println("[" + joinPoint.getSignature().toShortString() + "] execution finished in " + executionTime + "ms");

        return result;
    }
}
