package com.shoppingcart.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;



@SpringBootApplication
@ComponentScan(basePackages = { "com.shoppingcart.admin.*"})
@EnableJpaRepositories(basePackages = { "com.shoppingcart.admin.*" })// scan packages same repository and execute them
@EntityScan({ "com.shoppingcart.admin.*" }) //scan packages storing @Enitity 
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
