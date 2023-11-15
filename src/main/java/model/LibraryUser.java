package model;

import jakarta.persistence.*;

import java.util.List;


public class LibraryUser{

    private String email;

    private String username;

    private List<Rating> ratings;

    private List<BookEvent> bookEvents;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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


}
