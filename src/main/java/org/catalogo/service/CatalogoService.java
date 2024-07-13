package org.catalogo.service;

import org.catalogo.controller.ConsoleController;

import org.catalogo.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogoService implements CommandLineRunner {

    @Autowired
    private LibroRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(CatalogoService.class, args);
    }

    @Override
    public void run ( String... args ) throws Exception {
        var data = new ConsoleController(repository);
        data.mostrarMenu();
    }
}