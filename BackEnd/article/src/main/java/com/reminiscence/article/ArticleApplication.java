package com.reminiscence.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ArticleApplication {

	public static void main(String[] args) {

		SpringApplication.run(ArticleApplication.class, args);
	}

}
