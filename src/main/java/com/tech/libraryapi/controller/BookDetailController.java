package com.tech.libraryapi.controller;

import com.tech.libraryapi.model.Book;
import com.tech.libraryapi.service.BookDetailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@RestController
@RequestMapping("api/v1")
@Validated
public class BookDetailController {
    private final BookDetailService bookDetailService;

    public BookDetailController(BookDetailService bookDetailService) {
        this.bookDetailService = bookDetailService;
    }

    @PostMapping("book")
    ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        Book addedBook = bookDetailService.addBook(book);
        return new ResponseEntity<>(addedBook, HttpStatus.CREATED);
    }

    @GetMapping("books")
    ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookDetailService.getAllBooks(), HttpStatus.OK);
    }

    @GetMapping("book/isbn/{isbn}")
    ResponseEntity<Book> getBookByIsbn(@Size(min = 10, max = 13, message = "{book.isbn.size}")
                                       @PathVariable(value="isbn") String isbn) {
        Book book = bookDetailService.getBookByIsbn(isbn);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PutMapping("book/{id}")
    public ResponseEntity<Book> updateBook(@Positive(message = "{book.id.positive}")
                                           @PathVariable(value="id") Long id,
                                           @Valid @RequestBody Book book) {
        Book updatedBook = bookDetailService.updateBook(id, book);
        return new ResponseEntity<>(updatedBook, HttpStatus.OK);
    }

    @DeleteMapping("book/{id}")
    public ResponseEntity<Book> deleteBookById(@Positive(message = "{book.id.positive}")
                                               @PathVariable(value="id") Long id) {
        Book deletedBook = bookDetailService.deleteBookById(id);
        return new ResponseEntity<>(deletedBook, HttpStatus.OK);
    }

    @GetMapping("book/{id}")
    ResponseEntity<Book> getBookById(@Positive(message = "{book.id.positive}")
                                     @PathVariable(value="id") Long id) {
        return new ResponseEntity<>(bookDetailService.getBookById(id), HttpStatus.OK);
    }

    @GetMapping("book/author/{author}")
    ResponseEntity<List<Book>> getBookByAuthor(@Size(min = 1, max = 80, message = "{book.author.size}")
                                               @PathVariable(value="author") String author) {
        return new ResponseEntity<>(bookDetailService.getBookByAuthor(author), HttpStatus.OK);
    }
}
