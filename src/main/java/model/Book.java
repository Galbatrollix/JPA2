package model;

import jakarta.persistence.*;

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

}
