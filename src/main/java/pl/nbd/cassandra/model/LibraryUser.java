package pl.nbd.cassandra.model;


import com.datastax.oss.driver.api.mapper.annotations.*;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.UUID;

@Entity(defaultKeyspace = CassandraRepo.KEYSPACE_NAME)
@PropertyStrategy(mutable = false)
@CqlName(CassandraRepo.TABLE_LIBRARY_USERS)
public class LibraryUser{

    @PartitionKey
    private UUID id;
    private String email;

    @ClusteringColumn
    private String username;


    public LibraryUser(
            UUID id,
            String username,
            String email
            ) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public LibraryUser(
            String username,
            String email
    ) {
        this.id = UUID.randomUUID();
        this.username = username;
        this.email = email;
    }




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


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
