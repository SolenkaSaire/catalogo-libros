package org.catalogo.model;

import jakarta.persistence.*;
import org.catalogo.model.Libro;

@Entity
@Table(name = "authors")
public class DatosAutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private Integer anoNacimiento;
    private Integer anoMuerte;
    @ManyToOne
    private Libro libro;

    public DatosAutor () {}

    public DatosAutor( DatosAutor datosAutor ){}

    public DatosAutor(String nombre, Integer anoNacimiento, Integer anoMuerte, Libro libro) {
        this.nombre = nombre;
        this.anoNacimiento = anoNacimiento;
        this.anoMuerte = anoMuerte;
        this.libro = libro;
    }


    public Libro getLibro () {
        return libro;
    }

    public void setLibro ( Libro libro ) {
        this.libro = libro;
    }

    public Integer getAnoMuerte () {
        return anoMuerte;
    }

    public void setAnoMuerte ( Integer anoMuerte ) {
        this.anoMuerte = anoMuerte;
    }

    public Integer getAnoNacimiento () {
        return anoNacimiento;
    }

    public void setAnoNacimiento ( Integer anoNacimiento ) {
        this.anoNacimiento = anoNacimiento;
    }

    public String getNombre () {
        return nombre;
    }

    public void setNombre ( String nombre ) {
        this.nombre = nombre;
    }

    public Long getId () {
        return id;
    }

    public void setId ( Long id ) {
        this.id = id;
    }

    @Override
    public String toString () {
        return
                "nombre='" + nombre + '\'' +
                        ", anoNacimiento=" + anoNacimiento +
                        ", anoMuerte=" + anoMuerte;
    }
}