package com.tech.libraryapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.libraryapi.model.Book;
import com.tech.libraryapi.service.BookDetailService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookDetailControllerTest {

    @Mock
    private BookDetailService bookDetailService;
    private ResponseEntity<Book> responseEntity;
    private ResponseEntity<List<Book>> listResponseEntity;
    private Book book;
    private Book book2;
    private Book updatedBook;
    private List<Book> bookList;

    @InjectMocks
    private BookDetailController bookDetailController;

    @Autowired
    private MockMvc mockMvc;

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

        bookList = new ArrayList<>();
        bookList.add(book);
        bookList.add(book2);

        mockMvc = MockMvcBuilders.standaloneSetup(bookDetailController).build();
    }

    @AfterEach
    void tearDown() {
        book = null;
        book2 = null;
        updatedBook = null;
        bookList = null;
    }

    @Test
    void addBook() throws Exception {
        when(bookDetailService.addBook(book)).thenReturn(book);
        mockMvc.perform(post("/api/v1/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(book)))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
        verify(bookDetailService, times(1)).addBook(any());
    }

    @Test
    void getAllBooks() throws Exception {
        when(bookDetailService.getAllBooks()).thenReturn(bookList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(bookDetailService).getAllBooks();
        verify(bookDetailService, times(1)).getAllBooks();
    }

    @Test
    void getBookByIsbn() throws Exception {
        when(bookDetailService.getBookByIsbn(book.getIsbn())).thenReturn(book);
        mockMvc.perform(get("/api/v1/book/isbn/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void updateBook() throws Exception {
        when(bookDetailService.updateBook(book.getId(), book2)).thenReturn(updatedBook);
        mockMvc.perform(put("/api/v1/book/{id}", book.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(book2)))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(bookDetailService, times(1)).updateBook(any(), any());
    }

    @Test
    void deleteBookById() throws Exception {
        when(bookDetailService.deleteBookById(book.getId())).thenReturn(book);
        mockMvc.perform(delete("/api/v1/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getBookById() throws Exception {
        when(bookDetailService.getBookById(book.getId())).thenReturn(book);
        mockMvc.perform(get("/api/v1/book/" + book.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getBookByIdShouldReturnHttpStatusCode400() throws Exception {
        String exceptionInput = "exceptionInput";
        mockMvc.perform(get("/api/v1/book/{id}", exceptionInput)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentTypeMismatchException))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    void getBookByAuthor() throws Exception {
        String input = "J. K. Rowling";
        mockMvc.perform(get("/api/v1/book/author/{author}", input)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
        verify(bookDetailService, times(1)).getBookByAuthor(any());
    }

    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
