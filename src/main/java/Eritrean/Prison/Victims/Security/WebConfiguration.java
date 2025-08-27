package Eritrean.Prison.Victims.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * In this class, we tell SpringBoot to render and return our Thymeleaf html document.
 */
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        registry.addViewController("/upload").setViewName("upload");
        registry.addViewController("/form").setViewName("UserForm");
    }
}