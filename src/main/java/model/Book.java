package model;


import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.List;

public class Book extends AbstractEntity{


    private BookData data;

    @BsonProperty("book_quantity")
    private int quantity;

    @BsonProperty("book_event_count")
    private int eventCount;

    private List<Rating> ratings;

    private List<BookEvent> bookEvents;

    private List<Catalog> catalogs;

    @BsonCreator
    public Book(@BsonProperty("_id") long id,
                @BsonProperty("book_title") String bookTitle,
                @BsonProperty("book_author") String bookAuthor,
                @BsonProperty("event_count") int eventCount) {
        super(id);
        this.data = new BookData();
        this.data.setTitle(bookTitle);
        this.data.setAuthor(bookAuthor);
        this.eventCount = eventCount;
    }


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
