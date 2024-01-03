package pl.nbd.cassandra.model;


import com.datastax.oss.driver.api.mapper.annotations.ClusteringColumn;
import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import org.bson.types.ObjectId;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.UUID;


@Entity(defaultKeyspace = CassandraRepo.KEYSPACE_NAME)
@PropertyStrategy(mutable = false)
public class Rating {



    private UUID id;

    private int stars;

    private String comment;


    private UUID bookId;


    private UUID userId;

    public Rating(UUID id, int stars, String comment, UUID bookId, UUID userId) {
        this.id = id;
        this.stars = stars;
        this.comment = comment;
        this.bookId = bookId;
        this.userId = userId;
    }

    public Rating(int stars, String comment, UUID bookId, UUID userId) {
        this.id = UUID.randomUUID();
        this.stars = stars;
        this.comment = comment;
        this.bookId = bookId;
        this.userId = userId;
    }

    public UUID getId() {
        return id;
    }


    public int getStars() {
        return stars;
    }


    public String getComment() {
        return comment;
    }


    public UUID getBookId() {
        return bookId;
    }


    public UUID getUserId() {
        return userId;
    }



}
