package com.reminiscence.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableEurekaClient
@EnableScheduling
public class ArticleApplication {

	public static void main(String[] args) {

		SpringApplication.run(ArticleApplication.class, args);
	}

}
