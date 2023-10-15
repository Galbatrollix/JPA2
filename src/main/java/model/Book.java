package model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Book {
    @Id
    @GeneratedValue()
    private long id;

    @Embedded
    private BookData data;

    @Column(nullable = false)
    private int quantityTotal;

    @Column(nullable = false)
    private int quantityAvailable;

    @OneToMany(mappedBy = "book")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "book")
    private List<BookEvent> bookEvents;

    @ManyToMany(mappedBy = "books")
    private List<Catalog> catalogs;


    public BookData getData() {
        return data;
    }

    public void setData(BookData data) {
        this.data = data;
    }

    public int getQuantityTotal() {
        return quantityTotal;
    }

    public void setQuantityTotal(int quantityTotal) {
        this.quantityTotal = quantityTotal;
    }

    public int getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(int quatityAvailable) {
        this.quantityAvailable = quatityAvailable;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<BookEvent> getBookEvents() {
        return bookEvents;
    }

    public void setBookEvents(List<BookEvent> bookEvents) {
        this.bookEvents = bookEvents;
    }


    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }
}
