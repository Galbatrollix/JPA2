package model;

import mongoMappers.BookEventMapper;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;

@BsonDiscriminator(key=BookEventMapper.CLASS_DISCRIMINATOR, value = BookEventMapper.RESERVATION_DISCRIMIATOR)
public class Reservation extends BookEvent {
    public Reservation(ObjectId id,
                       Date beginDate,
                       Date expectedEndDate,
                       Date closeDate,
                       ObjectId bookId,
                       ObjectId userId) {
        super(id, beginDate, expectedEndDate, closeDate, bookId, userId);

    }
    public Reservation(Date beginDate,
                       Date expectedEndDate,
                       Date closeDate,
                       ObjectId bookId,
                       ObjectId userId) {
        super(beginDate, expectedEndDate, closeDate, bookId, userId);

    }

}
