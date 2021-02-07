package com.kyriewang.kkbbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.kyriewang.kkbbs.mapper")
public class KkbbsApplication {

	public static void main(String[] args) {
		SpringApplication.run(KkbbsApplication.class, args);
	}

}
