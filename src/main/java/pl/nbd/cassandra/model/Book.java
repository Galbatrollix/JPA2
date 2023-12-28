package pl.nbd.cassandra.model;

import com.datastax.oss.driver.api.mapper.annotations.*;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.UUID;

@Entity(defaultKeyspace = CassandraRepo.KEYSPACE_NAME)
@PropertyStrategy(mutable = false)
@CqlName("books")
public class Book {


    private String author;
    @PartitionKey
    private UUID id;
    private int quantity;

    @ClusteringColumn
    private String title;



    public Book(
            UUID id,
            String title,
            int quantity,
            String author
            ) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.quantity = quantity;
    }




    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

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


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
