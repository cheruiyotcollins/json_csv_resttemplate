package com.interview.yoyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class YoyoApplication {

	public static void main(String[] args) {
		SpringApplication.run(YoyoApplication.class, args);
	}

}
