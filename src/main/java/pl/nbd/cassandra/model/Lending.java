package pl.nbd.cassandra.model;


import org.bson.types.ObjectId;

import java.util.Date;

public class Lending {

    private Date beginDate;

    private Date expectedEndDate;

    private Date closeDate;

    private ObjectId userId;
    private ObjectId bookId;
//    public Lending(ObjectId id,
//                   Date beginDate,
//                   Date expectedEndDate,
//                   Date closeDate,
//                   ObjectId bookId,
//                   ObjectId userId) {
//        super(id, beginDate, expectedEndDate, closeDate, bookId, userId);
//
//    }


}