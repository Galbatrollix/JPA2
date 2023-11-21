package model;


import mongoMappers.BookEventMapper;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;


@BsonDiscriminator(key=BookEventMapper.CLASS_DISCRIMINATOR, value = BookEventMapper.LENDING_DISCRIMINATOR)
public class Lending extends BookEvent {
    public Lending(@BsonProperty(BookEventMapper.ID) ObjectId id,
                   @BsonProperty(BookEventMapper.BEGIN) Date beginDate,
                   @BsonProperty(BookEventMapper.EXPECTED_END) Date expectedEndDate,
                   @BsonProperty(BookEventMapper.CLOSE) Date closeDate) {
        super(id, beginDate, expectedEndDate, closeDate);

    }

}