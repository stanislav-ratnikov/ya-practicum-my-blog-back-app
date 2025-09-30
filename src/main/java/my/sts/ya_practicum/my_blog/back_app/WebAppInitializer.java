package my.sts.ya_practicum.my_blog.back_app;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import my.sts.ya_practicum.my_blog.back_app.config.AppConfig;
import my.sts.ya_practicum.my_blog.back_app.config.WebAppConfig;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebAppConfig.class};
    }

    @Override
    @NonNull
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(new MultipartConfigElement(
                "",
                5242880,    // 5MB
                20971520,   // 20MB
                0
        ));
    }
}
