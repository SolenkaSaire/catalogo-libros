package org.catalogo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "libros")
@Getter
@Setter
@ToString(includeFieldNames=true, of={"id", "titulo", "idiomas", "descargas"})
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
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

    public void setAutores ( List<DatosAutor> autores ) {
        autores.forEach(e -> e.setLibro(this));
        this.autores = autores;
    }
}