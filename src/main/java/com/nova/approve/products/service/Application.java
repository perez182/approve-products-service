package com.nova.approve.products.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.nova.approve.products.service.config.ProvidersProperties;

@SpringBootApplication
@EnableConfigurationProperties(ProvidersProperties.class)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
