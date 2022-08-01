package com.tech.libraryapi.service;

import com.tech.libraryapi.exception.BookAlreadyExistsException;
import com.tech.libraryapi.exception.BookNotFoundException;
import com.tech.libraryapi.model.Book;
import com.tech.libraryapi.repository.BookDetailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookDetailServiceImpl implements BookDetailService {
    private final BookDetailRepository bookDetailRepository;

    public BookDetailServiceImpl(BookDetailRepository bookDetailRepository) {
        this.bookDetailRepository = bookDetailRepository;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> bookList = (List<Book>) bookDetailRepository.findAll();
        if (bookList.isEmpty()) throw new BookNotFoundException();
        return bookList;
    }

    @Override
    public Book addBook(Book book) throws BookAlreadyExistsException {
        String isbn = book.getIsbn();
        book.setId(null);
        if (bookDetailRepository.findBookByIsbn(isbn).isPresent()) {
            throw new BookAlreadyExistsException(isbn);
        }
        return bookDetailRepository.save(book);
    }

    @Override
    public Book getBookByIsbn(String isbn) throws BookNotFoundException {
        return bookDetailRepository.findBookByIsbn(isbn).orElseThrow(() -> new BookNotFoundException(isbn));
    }

    @Override
    public Book updateBook(Long id, Book newBook) {
        return bookDetailRepository.findById(id)
                .map(book -> {
                    book.setAuthor(newBook.getAuthor());
                    book.setIsbn(newBook.getIsbn());
                    book.setName(newBook.getName());
                    book.setPrice(newBook.getPrice());
                    book.setQuantity(newBook.getQuantity());
                    book.setDate(newBook.getDate());
                    return bookDetailRepository.save(book);
                })
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public Book getBookById(Long id) {
        return bookDetailRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public List<Book> getBookByAuthor(String author) {
        List<Book> bookList = bookDetailRepository.findBookByAuthor(author);
        if (bookList.isEmpty()) throw new BookNotFoundException(author);
        return bookList;
    }

    @Override
    public Book deleteBookById(Long id) {
        Book book = bookDetailRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        bookDetailRepository.deleteById(id);
        return book;
    }
}
