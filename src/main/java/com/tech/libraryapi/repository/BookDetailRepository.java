package com.tech.libraryapi.repository;

import com.tech.libraryapi.model.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookDetailRepository extends CrudRepository<Book, Long>  {
    @Query("SELECT a FROM Book a WHERE a.isbn =:isbn")
    Optional<Book> findBookByIsbn(@Param("isbn") String isbn);

    @Query("SELECT a FROM Book a WHERE a.author =:author")
    List<Book> findBookByAuthor(@Param("author") String author);
}
