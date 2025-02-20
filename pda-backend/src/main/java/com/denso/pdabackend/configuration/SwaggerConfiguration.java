package com.denso.pdabackend.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;


@Configuration
public class SwaggerConfiguration {
	
	@Bean
	  public OpenAPI springShopOpenAPI() {
	      return new OpenAPI()
	              .info(new Info().title("health")
	              .description("덴소 pda rest-api")
	              .version("v0.0.1")
	              .license(new License().name("덴소").url("")))
	              .externalDocs(new ExternalDocumentation()
	              .description("")
	              .url(""));
	}
	
	
}
