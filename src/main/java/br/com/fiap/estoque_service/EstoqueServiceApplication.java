package br.com.fiap.estoque_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class EstoqueServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstoqueServiceApplication.class, args);
	}

}

