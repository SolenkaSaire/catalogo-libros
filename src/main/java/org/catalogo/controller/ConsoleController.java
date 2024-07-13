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
    private List<Libro> books;
    private String bookSelected;

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
                    getBookData();
                    break;
                case 2:
                    showStoredBooks();
                    break;
                case 3:
                    authorsListStored();
                    break;
                case 4:
                    getAuthorYear();
                    break;
                case 5:
                    findBooksByLanguages();
                    break;
            }
        }
    }


    private String getDataFromUser () {
        System.out.println("Introduzca el nombre del libro que desea buscar");
        bookSelected = scanner.nextLine();
        return bookSelected;
    }

    // Función para obtener los datos del libro de la API
    private DatosResultado getBookDataFromAPI ( String bookTitle ) {
        var json = requestAPI.getData(BASE_URL + "?search=%20" + bookTitle.replace(" ", "+"));
        var data = dataConvert.getData(json, DatosResultado.class);

        return data;
    }

    private Optional<Libro> getBookInfo ( DatosResultado bookData, String bookTitle ) {
        Optional<Libro> books = bookData.resultados().stream()
                .filter(l -> l.titulo().toLowerCase().contains(bookTitle.toLowerCase()))
                .map(b -> new Libro(b.titulo(), b.idiomas(), b.descargas(), b.autores()))
                .findFirst();
        return books;
    }

    private void buscarSerieWeb () {
        String titulo = getDataFromUser();
        DatosResultado datos = getBookDataFromAPI(titulo);
        Libro book = new Libro(datos.resultados());
        repository.save(book);

        System.out.println(book);
    }


    // Función principal que utiliza las funciones anteriores
    private Optional<Libro> getBookData () {
        String bookTitle = getDataFromUser();
        DatosResultado bookInfo = getBookDataFromAPI(bookTitle);
        Optional<Libro> book = getBookInfo(bookInfo, bookTitle);

        if ( book.isPresent() ) {
            var b = book.get();
            repository.save(b);
            System.out.println(b);
        } else {
            System.out.println("\nLibro no encontrado\n");
        }

        return book;
    }

    // funcion para mostrar los libros registrados
    private void showStoredBooks () {
        books = repository.findAll();

        books.stream()
                .sorted(Comparator.comparing(Libro::getTitulo))
                .forEach(System.out::println);
    }

    // Lista de autores registrados

    private void authorsListStored () {
        List<DatosAutor> authors = repository.getAuthorsInfo();

        authors.stream()
                .sorted(Comparator.comparing(DatosAutor::getNombre))
                .forEach(a -> System.out.printf("Author: %s Born: %s Death: %s\n",
                        a.getNombre(), a.getAnoNacimiento(), a.getAnoMuerte()));
    }

    // obtener autores vivos despues de determinado año

    public void getAuthorYear () {
        System.out.println("Intorduzca el año a aprtir del cual desea saber que un author estaba vivo");
        int date = scanner.nextInt();
        scanner.nextLine();

        List<DatosAutor> authorInfos = repository.getAuthorLiveAfter(date);

        authorInfos.stream()
                .sorted(Comparator.comparing(DatosAutor::getNombre))
                .forEach(a -> System.out.printf("Author: %s Born: %s Death: %s\n",
                        a.getNombre(), a.getAnoNacimiento(), a.getAnoMuerte()));
    }

    // encontrar libros por idioma

    public void findBooksByLanguages () {
        String languagesList = """
                Elija entre las opciones del idioma del libro que desea buscar
                
                en - Inglés
                es - Español
                fr - Francés
                it - Italiano
                pt - Portugués
                
                """;
        System.out.println(languagesList);
        String text =  scanner.nextLine();

        var language = Idioma.desdeString(text);

        List<Libro> bookLanguage = repository.findByLanguages(language);

        bookLanguage.stream()
                .forEach(System.out::println);
    }
}