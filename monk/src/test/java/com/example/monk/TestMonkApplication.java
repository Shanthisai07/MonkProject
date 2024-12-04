package com.example.monk;

import org.springframework.boot.SpringApplication;

public class TestMonkApplication {

	public static void main(String[] args) {
		SpringApplication.from(MonkApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
