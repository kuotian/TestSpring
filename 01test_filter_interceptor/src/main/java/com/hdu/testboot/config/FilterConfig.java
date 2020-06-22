package com.hdu.testboot.config;

import com.hdu.testboot.filter.TestFilter3;
import com.hdu.testboot.filter.TestFilter4;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author: xs
 * @since: 2020-06-22
 */
@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean testFilter3RegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new TestFilter3());
        registration.addUrlPatterns("/hello");
        registration.setOrder(2); // 值越小越靠前
        return registration;
    }

    @Bean
    public FilterRegistrationBean testFilter4RegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new TestFilter4());
        registration.addUrlPatterns("/hello");
        registration.setOrder(1);
        return registration;
    }
}
