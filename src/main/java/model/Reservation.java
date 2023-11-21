package model;

import mongoMappers.BookEventMapper;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;

@BsonDiscriminator(key=BookEventMapper.CLASS_DISCRIMINATOR, value = BookEventMapper.RESERVATION_DISCRIMIATOR)
public class Reservation extends BookEvent {
    public Reservation(@BsonProperty(BookEventMapper.ID) ObjectId id,
                       @BsonProperty(BookEventMapper.BEGIN) Date beginDate,
                       @BsonProperty(BookEventMapper.EXPECTED_END) Date expectedEndDate,
                       @BsonProperty(BookEventMapper.CLOSE) Date closeDate) {
        super(id, beginDate, expectedEndDate, closeDate);

    }

}
