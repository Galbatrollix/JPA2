package model;


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

    public Lending(
                   Date beginDate,
                   Date expectedEndDate,
                   Date closeDate,
                   ObjectId bookId,
                   ObjectId userId) {
        super(null, beginDate, expectedEndDate, closeDate, bookId, userId);

    }

}