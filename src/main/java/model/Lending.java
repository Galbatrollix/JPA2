package model;


import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;


@BsonDiscriminator(key="_clazz", value = "lending")
public class Lending extends BookEvent {
    public Lending(@BsonProperty("_id") ObjectId id,
                       @BsonProperty("begin_date") Date beginDate,
                       @BsonProperty("expected_end_date") Date expectedEndDate,
                       @BsonProperty("close_date") Date closeDate) {
        super(id, beginDate, expectedEndDate, closeDate);

    }

}