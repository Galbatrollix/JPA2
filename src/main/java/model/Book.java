package model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Book extends AbstractEntity{

    @Embedded
    private BookData data;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int eventCount;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<Rating> ratings;

    @OneToMany(mappedBy = "book", cascade = CascadeType.REMOVE)
    private List<BookEvent> bookEvents;

    @ManyToMany(mappedBy = "books")
    private List<Catalog> catalogs;


    public BookData getData() {
        return data;
    }

    public void setData(BookData data) {
        this.data = data;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
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
