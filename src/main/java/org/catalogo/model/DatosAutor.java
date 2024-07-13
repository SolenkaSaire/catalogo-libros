package org.catalogo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "authors")
@Getter
@Setter
@ToString(of = {"nombre", "anoNacimiento", "anoMuerte"})
public class DatosAutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer anoNacimiento;
    private Integer anoMuerte;
    @ManyToOne
    @ToString.Exclude
    private Libro libro;

    public DatosAutor () {}

    public DatosAutor( DatosAutor datosAutor ){}

    public DatosAutor(String nombre, Integer anoNacimiento, Integer anoMuerte, Libro libro) {
        this.nombre = nombre;
        this.anoNacimiento = anoNacimiento;
        this.anoMuerte = anoMuerte;
        this.libro = libro;
    }
}