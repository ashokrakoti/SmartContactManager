package com.smart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class SmartContactManagerApplication extends SpringBootServletInitializer {

	//this part is added because of the reason we wanted to run this project separately in Tomcat externally.
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SmartContactManagerApplication.class);
    }
	
	public static void main(String[] args) {
		SpringApplication.run(SmartContactManagerApplication.class, args);
	}

}
