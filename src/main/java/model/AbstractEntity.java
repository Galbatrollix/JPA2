package model;


import org.bson.types.ObjectId;


public abstract class AbstractEntity {

    protected ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id){
        this.id = id;
    }

    public AbstractEntity(ObjectId id) {
        this.id = id;
    }
}
