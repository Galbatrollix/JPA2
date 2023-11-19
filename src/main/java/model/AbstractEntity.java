package model;


import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.io.Serializable;

public abstract class AbstractEntity implements Serializable {

    @BsonProperty("_id")
    protected ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public AbstractEntity(ObjectId id) {
        this.id = id;
    }
}
