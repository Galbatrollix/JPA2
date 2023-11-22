package model;


import mongoMappers.BookEventMapper;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;


public class Lending extends BookEvent {
    public Lending(ObjectId id,
                   Date beginDate,
                   Date expectedEndDate,
                   Date closeDate,
                   ObjectId bookId,
                   ObjectId userId) {
        super(id, beginDate, expectedEndDate, closeDate, bookId, userId);

    }

}