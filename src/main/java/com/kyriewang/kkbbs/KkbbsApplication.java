package com.kyriewang.kkbbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "com.kyriewang.kkbbs.mapper")
@EnableScheduling
public class KkbbsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KkbbsApplication.class, args);
	}

}
