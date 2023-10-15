package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Rating {
    @Id
    @GeneratedValue()
    private long id;
}
