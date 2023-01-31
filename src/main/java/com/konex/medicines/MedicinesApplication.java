package com.konex.medicines;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.konex.medicines.config.RedisConfig;

@SpringBootApplication
@Import(RedisConfig.class)
public class MedicinesApplication {
	public static void main(String[] args) {
		SpringApplication.run(MedicinesApplication.class, args);
	}

}
