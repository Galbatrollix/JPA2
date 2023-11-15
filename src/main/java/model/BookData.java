package model;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Embeddable;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class BookData {
    @BsonProperty("book_title")
    private String title;
    @BsonProperty("book_author")
    private String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
