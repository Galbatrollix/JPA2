package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Catalog {
    @Id
    @GeneratedValue()
    private long id;
}
