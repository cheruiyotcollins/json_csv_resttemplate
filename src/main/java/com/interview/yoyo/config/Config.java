package com.interview.yoyo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {



        @Bean
        public RestTemplate restTesmplate() {
            return new RestTemplate();
        }

    }

