package com.tech.libraryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.libraryapi.LibraryApiApplication;
import com.tech.libraryapi.model.Book;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.StringStartsWith.startsWith;

@SpringBootTest(classes = LibraryApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookDetailControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    WebTestClient webTestClient;

    private Book book;
    private Book book2;
    private Book updatedBook;
    private Book newBook;
    private List<Book> bookList;

    @BeforeEach
    void setUp() {

        book = new Book();
        book.setId(1L);
        book.setIsbn("0747549559");
        book.setName("Harry Potter and the Philosopher's Stone");
        book.setAuthor("J. K. Rowling");
        book.setPrice(new BigDecimal("6.99").setScale(2, RoundingMode.HALF_UP));
        book.setQuantity(10);
        book.setDate("June 26, 1997");

        book2 = new Book();
        book2.setId(2L);
        book2.setIsbn("0345339711");
        book2.setName("The Two Towers");
        book2.setAuthor("J. R. R. Tolkien");
        book2.setPrice(new BigDecimal("9.99").setScale(2, RoundingMode.HALF_UP));
        book2.setQuantity(20);
        book2.setDate("November 11, 1954");

        updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setIsbn("0345339711");
        updatedBook.setName("The Two Towers");
        updatedBook.setAuthor("J. R. R. Tolkien");
        updatedBook.setPrice(new BigDecimal("9.99").setScale(2, RoundingMode.HALF_UP));
        updatedBook.setQuantity(20);
        updatedBook.setDate("November 11, 1954");

        newBook = new Book();
        newBook.setId(3L);
        newBook.setIsbn("0446677450");
        newBook.setName("Rich Dad Poor Dad");
        newBook.setAuthor("Robert Kiyosaki and Sharon Lechter");
        newBook.setPrice(new BigDecimal("19.99").setScale(2, RoundingMode.HALF_UP));
        newBook.setQuantity(30);
        newBook.setDate("April 1, 2000");

        bookList = new ArrayList<>();
        bookList.add(book);
        bookList.add(book2);
    }

    @AfterEach
    void tearDown() {
        book = null;
        book2 = null;
        updatedBook = null;
        bookList = null;
    }

    @Test
    @Order(1)
    public void getAllBooks() {
        String url = createUrl("books");
        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(asJsonString(bookList))
                .consumeWith(System.out::println);
    }

    @Test
    @Order(2)
    public void getBookById1LShouldReturnABook() {
        long id = 1L;
        String url = createUrl("book/" + id);
        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(asJsonString(book))
                .consumeWith(System.out::println);
    }

    @Test
    @Order(3)
    public void getBookById5LShouldReturnNotFound() {
        long id = 5L;
        String url = createUrl("book/" + id);
        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).isEqualTo("Could not find the book by id: " + id)
                .consumeWith(System.out::println);
    }

    @Test
    @Order(4)
    public void addBookShouldReturnTheNewBook() {
        String url = createUrl("book");
        webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(newBook), Book.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(String.class).isEqualTo(asJsonString(newBook))
                .consumeWith(System.out::println);

        bookList.add(newBook);
        webTestClient.get().uri(createUrl("books"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(asJsonString(bookList))
                .consumeWith(System.out::println);
    }

    @Test
    @Order(5)
    public void getBookByIsbnShouldReturnABookWithThatIsbn() {
        String isbn = "0747549559";
        String url = createUrl("book/isbn/" + isbn);
        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(asJsonString(book))
                .consumeWith(System.out::println);
    }

    @Test
    @Order(6)
    public void getBookByAuthor() {
        String author = "J. K. Rowling";
        String url = createUrl("book/author/" + author);
        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(asJsonString(Collections.singletonList(book)))
                .consumeWith(System.out::println);
    }

    @Test
    @Order(7)
    public void addBookAlreadyExistsShouldReturnErrorMessage() {
        String url = createUrl("book");
        webTestClient.post().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(book), Book.class)
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(String.class).isEqualTo("Book with ISBN " + book.getIsbn() + " already exists.")
                .consumeWith(System.out::println);
    }

    @Test
    @Order(8)
    public void getBookWithInputStringShouldReturnMethodArgumentTypeMismatchAdvice() {
        String incorrectInput = "incorrectInput";
        String url = createUrl("book/" + incorrectInput);
        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).isEqualTo("'id' should be a valid 'Long' type and " +
                        "the input '" + incorrectInput + "' is not.")
                .consumeWith(System.out::println);
    }

    @Test
    @Order(9)
    public void getBookWithInvalidIsbnShouldReturnError() {
        String incorrectIsbn = "12345";
        String url = createUrl("book/isbn/" + incorrectIsbn);
        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isEqualTo(500)
                .expectBody(String.class).isEqualTo("[\"Isbn size must be between 10 and 13.\"]")
                .consumeWith(System.out::println);
    }

    @Test
    @Order(10)
    public void addBookWithEmptyBodyShouldReturnError() {
        String url = createUrl("book");
        webTestClient.post().uri(url)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class).value(startsWith("Required request body is missing"))
                .consumeWith(System.out::println);
    }

    @Test
    @Order(11)
    public void endpointBooksDoesNotSupportsPostMethodShouldReturnError() {
        String url = createUrl("books");
        webTestClient.post().uri(url)
                .exchange()
                .expectStatus().isEqualTo(405)
                .expectBody(String.class).isEqualTo("Request method 'POST' not supported")
                .consumeWith(System.out::println);
    }

    @Test
    @Order(12)
    public void updateBookId1LWithBookId2LShouldReturnBookId1L() {
        long id = 1L;
        String url = createUrl("book/" + id);
        webTestClient.put().uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(book2), Book.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(asJsonString(updatedBook))
                .consumeWith(System.out::println);
    }

    @Test
    @Order(13)
    public void deleteBookById() {
        long id = 1L;
        String url = createUrl("book/" + id);
        webTestClient.delete().uri(url)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class).isEqualTo(asJsonString(updatedBook))
                .consumeWith(System.out::println);

        webTestClient.get().uri(url)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class).isEqualTo("Could not find the book by id: " + id)
                .consumeWith(System.out::println);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String createUrl(String uri) {
        return "http://localhost:" + port + "/api/v1/" + uri;
    }
}