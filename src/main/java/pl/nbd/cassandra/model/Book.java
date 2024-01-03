package pl.nbd.cassandra.model;

import com.datastax.oss.driver.api.mapper.annotations.*;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.UUID;

@Entity(defaultKeyspace = CassandraRepo.KEYSPACE_NAME)
@PropertyStrategy(mutable = false)
@CqlName(CassandraRepo.TABLE_BOOKS)
public class Book {


    @PartitionKey
    private UUID id;

    @ClusteringColumn
    private String author;


    private String title;

    public Book(
            UUID id,
            String author,
            String title
            ) {
        this.id = id;
        this.title = title;
        this.author = author;
    }
    public Book(
            String author,
            String title
    ) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
