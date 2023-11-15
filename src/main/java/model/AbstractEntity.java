package model;


import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.io.Serializable;

public abstract class AbstractEntity implements Serializable {

    @BsonProperty("_id")
    protected long id;

    public long getId() {
        return id;
    }

    public AbstractEntity(long id) {
        this.id = id;
    }
}
