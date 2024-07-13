package org.catalogo.model;

public enum Idioma {
    INGLES("en"),
    ESPANOL("es"),
    FRANCES("fr"),
    ITALIANO("it"),
    PORTUGUES("pt");

    private String idiomasAlura;

    Idioma ( String idiomasAlura ) {
        this.idiomasAlura = idiomasAlura;
    }

    public static Idioma desdeString(String texto) {
        for (Idioma idioma : Idioma.values())
            if ( idioma.idiomasAlura.equalsIgnoreCase(texto) ) {
                return idioma;
            }
        throw new IllegalArgumentException("Ninguna categoria encontrada: " + texto);
    }

}