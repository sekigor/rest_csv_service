package com.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages="com.rest")
@SpringBootApplication
public class TomcatStarter {

	public static void main(String[] args) {
		SpringApplication.run(TomcatStarter.class, args);
	}
}
