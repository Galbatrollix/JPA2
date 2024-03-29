package model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class LibraryUser extends AbstractEntity{

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Rating> ratings;

    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
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
