package com.example.test_opentelemrtry;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class TestOpentelemrtryApplication {

	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(TestOpentelemrtryApplication.class);
		application.setBannerMode(Banner.Mode.OFF);
		application.run(args);
		// SpringApplication.run(TestOpentelemrtryApplication.class, args);
	}

}
