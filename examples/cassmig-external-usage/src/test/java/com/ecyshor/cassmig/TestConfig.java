package com.ecyshor.cassmig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
	@Bean
	public String cassandraHost() {
		return "localhost";
	}
}
