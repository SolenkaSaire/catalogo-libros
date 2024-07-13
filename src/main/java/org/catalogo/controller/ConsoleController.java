package org.catalogo.controller;

import org.catalogo.model.*;

import org.catalogo.service.ApiService;
import org.catalogo.service.ConversorDatos;

import org.catalogo.repository.LibroRepository;

import java.util.*;

public class ConsoleController {

    private Scanner scanner = new Scanner(System.in);
    private ApiService requestAPI = new ApiService();
    private ConversorDatos dataConvert = new ConversorDatos();
    private LibroRepository repository;
    private final String BASE_URL = "https://gutendex.com/books/";
    private List<Libro> libros;
    private String libroSeleccionado;

    public ConsoleController ( LibroRepository repository ) {
        this.repository = repository;
    }

    public void mostrarMenu () {
        var option = - 1;
        while ( option != 0 ) {
            var menu = """
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    0 - Salir
                    """;
            System.out.println(menu);
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1:
                    obtenerDatosLibro();
                    break;
                case 2:
                    mostrarLibrosAlmacenados();
                    break;
                case 3:
                    listarAutoresAlmacenados();
                    break;
                case 4:
                    obtenerAutoresPorAno();
                    break;
                case 5:
                    encontrarLibrosPorIdioma();
                    break;
            }
        }
    }

    private String obtenerDatosDeUsuario () {
        System.out.println("Introduzca el nombre del libro que desea buscar");
        libroSeleccionado = scanner.nextLine();
        return libroSeleccionado;
    }

    private DatosResultado obtenerDatosLibroDeAPI ( String tituloLibro ) {
        var json = requestAPI.getData(BASE_URL + "?search=%20" + tituloLibro.replace(" ", "+"));
        var datos = dataConvert.getData(json, DatosResultado.class);

        return datos;
    }

    private Optional<Libro> obtenerInfoLibro ( DatosResultado datosLibro, String tituloLibro ) {
        Optional<Libro> libros = datosLibro.resultados().stream()
                .filter(l -> l.titulo().toLowerCase().contains(tituloLibro.toLowerCase()))
                .map(b -> new Libro(b.titulo(), b.idiomas(), b.descargas(), b.autores()))
                .findFirst();
        return libros;
    }

    private void buscarSerieWeb () {
        String titulo = obtenerDatosDeUsuario();
        DatosResultado datos = obtenerDatosLibroDeAPI(titulo);
        Libro libro = new Libro(datos.resultados());
        repository.save(libro);

        System.out.println(libro);
    }

    private Optional<Libro> obtenerDatosLibro () {
        String tituloLibro = obtenerDatosDeUsuario();
        DatosResultado datosLibro = obtenerDatosLibroDeAPI(tituloLibro);
        Optional<Libro> libro = obtenerInfoLibro(datosLibro, tituloLibro);

        if ( libro.isPresent() ) {
            var b = libro.get();
            repository.save(b);
            System.out.println(b);
        } else {
            System.out.println("\nLibro no encontrado\n");
        }

        return libro;
    }

    private void mostrarLibrosAlmacenados () {
        libros = repository.findAll();

        libros.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }

    private void listarAutoresAlmacenados () {
        List<DatosAutor> autores = repository.getAuthorsInfo();

        autores.stream()
                .sorted(Comparator.comparing(DatosAutor::getNombre))
                .forEach(a -> System.out.printf("Autor: %s Nacimiento: %s Muerte: %s\n",
                        a.getNombre(), a.getAnoNacimiento(), a.getAnoMuerte()));
    }

    public void obtenerAutoresPorAno () {
        System.out.println("Introduzca el año a partir del cual desea saber que un autor estaba vivo");
        int fecha = scanner.nextInt();
        scanner.nextLine();

        List<DatosAutor> informacionAutores = repository.getAuthorLiveAfter(fecha);

        informacionAutores.stream()
                .sorted(Comparator.comparing(DatosAutor::getNombre))
                .forEach(a -> System.out.printf("Autor: %s Nacimiento: %s Muerte: %s\n",
                        a.getNombre(), a.getAnoNacimiento(), a.getAnoMuerte()));
    }

    public void encontrarLibrosPorIdioma () {
        String listaIdiomas = """
                Elija entre las opciones del idioma del libro que desea buscar

                en - Inglés
                es - Español
                fr - Francés
                it - Italiano
                pt - Portugués

                """;
        System.out.println(listaIdiomas);
        String texto =  scanner.nextLine();

        var idioma = Idioma.desdeString(texto);

        List<Libro> librosIdioma = repository.findByLanguages(idioma);

        librosIdioma.stream()
                .forEach(System.out::println);
    }
}