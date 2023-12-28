package pl.nbd.cassandra.model;


import java.io.Serializable;


public abstract class AbstractEntity implements Serializable {


    private long id;

    public long getId() {
        return id;
    }

    public AbstractEntity(long id) {
        this.id = id;
    }

}
