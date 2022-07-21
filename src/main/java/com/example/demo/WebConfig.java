package com.example.demo;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;

@Configuration
public class WebConfig implements WebMvcConfigurer{

	    //Lucy Xss filter 적용
	    @Bean
	    public FilterRegistrationBean xssFilterBean(){
	        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
	        registrationBean.setFilter(new XssEscapeServletFilter());
	        registrationBean.setOrder(Ordered.LOWEST_PRECEDENCE);
	        registrationBean.addUrlPatterns("/*");
	        return registrationBean;
	    }
}
