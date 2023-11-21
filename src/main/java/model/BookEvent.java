package model;

import mongoMappers.BookEventMapper;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.Date;


@BsonDiscriminator(key = BookEventMapper.CLASS_DISCRIMINATOR)
public class BookEvent extends AbstractEntity{
    @BsonProperty(BookEventMapper.BEGIN)
    private Date beginDate;

    @BsonProperty(BookEventMapper.EXPECTED_END)
    private Date expectedEndDate;

    @BsonProperty(BookEventMapper.CLOSE)
    private Date closeDate;

    public BookEvent(@BsonProperty(BookEventMapper.ID) ObjectId id,
                     @BsonProperty(BookEventMapper.BEGIN) Date beginDate,
                     @BsonProperty(BookEventMapper.EXPECTED_END) Date expectedEndDate,
                     @BsonProperty(BookEventMapper.CLOSE) Date closeDate) {
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
