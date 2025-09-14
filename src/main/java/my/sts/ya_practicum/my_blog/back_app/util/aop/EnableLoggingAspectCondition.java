package my.sts.ya_practicum.my_blog.back_app.util.aop;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class EnableLoggingAspectCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, @NonNull AnnotatedTypeMetadata metadata) {
        return Objects.equals(context.getEnvironment().getProperty("logging.aspect.enabled"), "true");
    }
}
