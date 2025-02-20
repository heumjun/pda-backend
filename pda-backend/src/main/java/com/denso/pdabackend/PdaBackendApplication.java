package com.denso.pdabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(nameGenerator = PdaBackendBeanNameGenerator.class)
@SpringBootApplication
public class PdaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PdaBackendApplication.class, args);
	}

}
