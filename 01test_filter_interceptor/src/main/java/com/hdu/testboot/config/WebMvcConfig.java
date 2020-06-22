package com.hdu.testboot.config;

import com.hdu.testboot.interceptor.TestInterceptor1;
import com.hdu.testboot.interceptor.TestInterceptor2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * TODO
 *
 * @author: xs
 * @since: 2020-06-22
 */
@Component
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TestInterceptor1 testInterceptor1;

    @Autowired
    private TestInterceptor2 testInterceptor2;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(testInterceptor1)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")
                .addPathPatterns("/hello");

        registry.addInterceptor(testInterceptor2).addPathPatterns("/hello");
    }

}
