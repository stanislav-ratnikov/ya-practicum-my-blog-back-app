package my.sts.ya_practicum.my_blog.back_app.test.config;

import my.sts.ya_practicum.my_blog.back_app.config.AppConfig;
import my.sts.ya_practicum.my_blog.back_app.config.WebAppConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppConfig.class, WebAppConfig.class})
public class ITConfig {
}
