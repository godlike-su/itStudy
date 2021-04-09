package com.itStudy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class SpringBootStartApplication extends SpringBootServletInitializer
{

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
    {
        return builder.sources(CustomerApplication.class);
    }
}
