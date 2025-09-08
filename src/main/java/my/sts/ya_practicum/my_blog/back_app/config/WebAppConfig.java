package my.sts.ya_practicum.my_blog.back_app.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "my.sts.ya_practicum.my_blog.back_app")
@PropertySource("classpath:application.properties")
public class WebAppConfig {
}
