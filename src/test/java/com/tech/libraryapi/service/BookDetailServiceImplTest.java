package com.tech.libraryapi.service;

import com.tech.libraryapi.model.Book;
import com.tech.libraryapi.repository.BookDetailRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookDetailServiceImplTest {

    @Mock
    private BookDetailRepository bookRepository;

    @Autowired
    @InjectMocks
    private BookDetailServiceImpl bookDetailService;
    private Book book;
    private Book book2;
    private Book book3;
    private Book updatedBook;
    List<Book> bookList;


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

        book3 = new Book();
        book3.setId(3L);
        book3.setIsbn("0345339738");
        book3.setName("The Return of the King");
        book3.setAuthor("J. R. R. Tolkien");
        book3.setPrice(new BigDecimal("19.99").setScale(2, RoundingMode.HALF_UP));
        book3.setQuantity(20);
        book3.setDate("November 11, 1955");

        updatedBook = new Book();
        updatedBook.setId(1L);
        updatedBook.setIsbn("0345339711");
        updatedBook.setName("The Two Towers");
        updatedBook.setAuthor("J. R. R. Tolkien");
        updatedBook.setPrice(new BigDecimal("9.99").setScale(2, RoundingMode.HALF_UP));
        updatedBook.setQuantity(20);
        updatedBook.setDate("November 11, 1954");

        bookList = new ArrayList<>();
        bookList.add(book);
        bookList.add(book2);
        bookList.add(book3);
    }

    @AfterEach
    void tearDown() {
        book = book2 = book3 = null;
        bookList = null;
    }

    @Test
    void getAllBooks() {
        bookRepository.save(book);
        when(bookRepository.findAll()).thenReturn(bookList);
        List<Book> bookList1 = bookDetailService.getAllBooks();
        assertEquals(bookList1, bookList);
        verify(bookRepository,times(1)).save(book);
        verify(bookRepository,times(1)).findAll();
    }

    @Test
    void addBook() {
        when(bookRepository.save(any())).thenReturn(book);
        bookDetailService.addBook(book);
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void getBookByIsbn() {
        when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(Optional.ofNullable(book));
        assertThat(bookDetailService.getBookByIsbn(book.getIsbn())).isEqualTo(book);
    }

    @Test
    void getBookById() {
        when(bookRepository.findById(book.getId())).thenReturn(Optional.ofNullable(book));
        assertThat(bookDetailService.getBookById(book.getId())).isEqualTo(book);
        assertThat(bookDetailService.getBookById(book.getId())).isNotEqualTo(book2);
    }

    @Test
    void getBookByAuthor() {
        when(bookRepository.findBookByAuthor(book.getAuthor())).thenReturn(bookList);
        assertThat(bookDetailService.getBookByAuthor(book.getAuthor())).isEqualTo(bookList);
    }

}
