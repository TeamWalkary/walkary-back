package com.walkary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WalkaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalkaryApplication.class, args);
	}

}
