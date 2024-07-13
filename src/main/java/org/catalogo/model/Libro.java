package org.catalogo.model;

import org.catalogo.model.Autor;
import org.catalogo.model.DatosLibro;
import org.catalogo.model.Idioma;
import org.catalogo.model.DatosAutor;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<DatosAutor> autores;
    @Enumerated(EnumType.STRING)
    private Idioma idiomas;
    private Double descargas;

    public Libro () {
    }

    public Libro ( List<DatosLibro> resultados ) {
    }

    public Libro ( String titulo, List<String> idiomas, Double descargas, List<Autor> autores ) {
        this.titulo = titulo;
        this.idiomas = Idioma.desdeString(idiomas.get(0));
        this.descargas = descargas;
        this.autores = new ArrayList<>();
        for ( Autor informacionAutor : autores ) {
            DatosAutor autor = new DatosAutor(informacionAutor.nombre(), informacionAutor.anoNacimiento(), informacionAutor.anoMuerte(), this);
            this.autores.add(autor);
        }
    }


    public Long getId () {
        return id;
    }

    public void setId ( Long id ) {
        this.id = id;
    }

    public Double getDescargas () {
        return descargas;
    }

    public void setDescargas ( Double descargas ) {
        this.descargas = descargas;
    }

    public Idioma getIdiomas () {
        return idiomas;
    }

    public void setIdiomas ( Idioma idiomas ) {
        this.idiomas = idiomas;
    }

    public String getTitulo () {
        return titulo;
    }

    public void setTitulo ( String titulo ) {
        this.titulo = titulo;
    }

    public List<DatosAutor> getAutores () {
        return autores;
    }

    public void setAutores ( List<DatosAutor> autores ) {
        autores.forEach(e -> e.setLibro(this));
        this.autores = autores;
    }

    @Override
    public String toString () {
        return
                "Titulo='" + titulo + '\'' + "\n" +
                        "Autores=" + autores + "\n" +
                        "Idiomas=" + idiomas + "\n" +
                        "Descargas=" + descargas + "\n";
    }
}