package com.objective.conta_bancaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.objective.conta_bancaria")
@EnableJpaRepositories(basePackages = "com.objective.conta_bancaria.repository")
public class ContaBancariaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContaBancariaApplication.class, args);
	}

}
