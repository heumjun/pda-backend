package com.denso.pdabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(nameGenerator = PdaBackendBeanNameGenerator.class)
@SpringBootApplication
//public class PdaBackendApplication {
public class PdaBackendApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(PdaBackendApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(PdaBackendApplication.class);
	}

}
