package br.edu.utfpr.td.tsi.agencia.noticias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {

	public static void main(String[] args) {
		System.setProperty("server.servlet.context-path", "/agencia.noticias");
		SpringApplication.run(Main.class, args);
	}
}
