package com.objective.conta_bancaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.objective.conta_bancaria")
public class ContaBancariaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContaBancariaApplication.class, args);
	}

}
