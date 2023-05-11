package com.example.hh99miniproject8.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Slf4j
@RequiredArgsConstructor
@Configuration
// JSON 직렬화에 사용자 지정 HtmlCharacterEscapes 클래스를 사용하도록 ObjectMapper 인스턴스를 설정하는 클래스.
// Spring MVC 프레임워크에서 JSON 응답을 변환하는 데 사용
public class WebMvcConfig {

    private final ObjectMapper objectMapper;

    // MappingJackson2HttpMessageConverter의 인스턴스를 반환
    @Bean
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }
}
