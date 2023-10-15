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
    private int quatityAvailable;

    @OneToMany(mappedBy = "book")
    private List<Rating> ratings;


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

    public int getQuatityAvailable() {
        return quatityAvailable;
    }

    public void setQuatityAvailable(int quatityAvailable) {
        this.quatityAvailable = quatityAvailable;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

}