package my.sts.ya_practicum.my_blog.back_app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import({DataSourceConfig.class})
@EnableAspectJAutoProxy
@ComponentScan(
        basePackages = {
                "my.sts.ya_practicum.my_blog.back_app.service",
                "my.sts.ya_practicum.my_blog.back_app.persistence"
        }
)
@PropertySource("classpath:application.properties")
public class AppConfig {
}
