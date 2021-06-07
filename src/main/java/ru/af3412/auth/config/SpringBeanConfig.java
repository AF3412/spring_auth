package ru.af3412.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SpringBeanConfig {

    @Bean
    public RestTemplate getTemplate() {
        return new RestTemplate();
    }

}
