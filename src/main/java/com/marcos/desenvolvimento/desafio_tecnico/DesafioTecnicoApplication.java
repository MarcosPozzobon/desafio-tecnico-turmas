package com.marcos.desenvolvimento.desafio_tecnico;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DesafioTecnicoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioTecnicoApplication.class, args);
	}

}
