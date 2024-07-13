package org.catalogo.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record Autor (
        @JsonAlias("name") String nombre,
        @JsonAlias("birth_year") Integer anoNacimiento,
        @JsonAlias("death_year") Integer anoMuerte
) {
}