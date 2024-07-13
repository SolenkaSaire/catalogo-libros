package org.catalogo.repository;

import org.catalogo.model.DatosAutor;
import org.catalogo.model.Idioma;
import org.catalogo.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    @Query("SELECT a FROM Libro b JOIN b.autores a")
    List<DatosAutor> getAuthorsInfo ();

    @Query("SELECT a FROM Libro b JOIN b.autores a WHERE a.anoNacimiento > :date")
    List<DatosAutor> getAuthorLiveAfter ( Integer date );

    List<Libro> findByLanguages ( Idioma languages );
}