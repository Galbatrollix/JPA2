package model;

import jakarta.persistence.*;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;


@BsonDiscriminator(key = "_clazz")
public class BookEvent extends AbstractEntity{
    @BsonProperty("begin_date")
    private Date beginDate;

    @BsonProperty("expected_end_date")
    private Date expectedEndDate;

    @BsonProperty("close_date")
    private Date closeDate;

    public BookEvent(@BsonProperty("_id") ObjectId id,
                     @BsonProperty("begin_date") Date beginDate,
                     @BsonProperty("expected_end_date") Date expectedEndDate,
                     @BsonProperty("close_date") Date closeDate) {
        super(id);
        this.beginDate = beginDate;
        this.expectedEndDate = expectedEndDate;
        this.closeDate = closeDate;
    }


    public Date getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(Date expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }


}
