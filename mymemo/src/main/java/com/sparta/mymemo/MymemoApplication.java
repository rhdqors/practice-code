package com.sparta.mymemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // Timestamped에 있는 @EntityListeners를 사용하기 위해 필요
public class MymemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MymemoApplication.class, args);
	}

}
