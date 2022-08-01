package com.tech.libraryapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name="books")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "{book.isbn.notNull}")
    @Size(min = 10, max = 13, message = "{book.isbn.size}")
    private String isbn;

    @NotNull(message = "{book.name.notNull}")
    @Size(min = 1, max = 80, message = "{book.name.size}")
    private String name;

    @NotNull(message = "{book.author.notNull}")
    @Size(min = 1, max = 80, message = "{book.author.size}")
    private String author;

    @NotNull(message = "{book.quantity.notNull}")
    @Min(value = 1, message = "{book.quantity.min}")
    @Max(value = 10000, message = "{book.quantity.max}")
    private int quantity;

    @NotNull(message = "{book.price.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{book.price.DecimalMin}")
    @DecimalMax(value = "99999.9", inclusive = false, message = "{book.price.DecimalMax}")
    @Digits(integer=5, fraction=2, message = "{book.price.digits}")
    private BigDecimal price;

    @NotNull(message = "{book.date.notNull}")
    @Size(min = 1, max = 50, message = "{book.date.size}")
    private String date;
}
