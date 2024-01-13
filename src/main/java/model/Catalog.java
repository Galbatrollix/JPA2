package model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Catalog extends AbstractEntity{
    private String name;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Book> books;

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
