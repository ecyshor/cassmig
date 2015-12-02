package com.ecyshor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.ecyshor")
public class ServiceConfiguration {

	@Bean
	public String cassandraHost() {
		return "cassandra";
	}
}
