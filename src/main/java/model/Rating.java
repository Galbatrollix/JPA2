package model;

import jakarta.persistence.*;



public class Rating{
    private int stars;
    private String comment;

    private Book book;


    private LibraryUser user;

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LibraryUser getUser() {
        return user;
    }

    public void setUser(LibraryUser user) {
        this.user = user;
    }

}
