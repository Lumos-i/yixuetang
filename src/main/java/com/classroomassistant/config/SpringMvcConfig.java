package com.classroomassistant.config;

import com.classroomassistant.componet.StringDateConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class SpringMvcConfig extends WebMvcConfigurationSupport {

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"
    };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**").addResourceLocations(
                    "classpath:/META-INF/resources/webjars/");

        }

        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/static/**").addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS);
        }

    }


    @Override
    protected void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringDateConverter());
    }
}
