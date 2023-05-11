package com.example.hh99miniproject8.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Slf4j
@Configuration
@RequiredArgsConstructor
public class XssConfig implements WebMvcConfigurer  {

    private final ObjectMapper objectMapper;

    // Lucy Xss filter 적용(form-data 로 데이터 받을 때)
//    @Bean
//    public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
//        FilterRegistrationBean<XssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
//        filterRegistration.setFilter(new XssEscapeServletFilter());
//        filterRegistration.setOrder(1);
//        filterRegistration.addUrlPatterns("/*");
//        return filterRegistration;
//    }

    // JSON xss 필터 적용(JSON 으로 데이터 받을 때)
    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }
}
