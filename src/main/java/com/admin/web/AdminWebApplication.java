package com.admin.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"com.admin.web"})
public class AdminWebApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(AdminWebApplication.class, args);
	}
}
