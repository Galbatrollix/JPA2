package model;

import org.bson.types.ObjectId;

import java.util.Date;

public class BookEvent extends AbstractEntity{

    private Date beginDate;

    private Date expectedEndDate;

    private Date closeDate;

    private ObjectId userId;
    private ObjectId bookId;

    public BookEvent(ObjectId id,
                     Date beginDate,
                     Date expectedEndDate,
                     Date closeDate,
                     ObjectId bookId,
                     ObjectId userId) {
        super(id);
        this.beginDate = beginDate;
        this.expectedEndDate = expectedEndDate;
        this.closeDate = closeDate;
        this.bookId = bookId;
        this.userId = userId;
    }

    public BookEvent(Date beginDate,
                     Date expectedEndDate,
                     Date closeDate,
                     ObjectId bookId,
                     ObjectId userId) {
        super(null);
        this.beginDate = beginDate;
        this.expectedEndDate = expectedEndDate;
        this.closeDate = closeDate;
        this.bookId = bookId;
        this.userId = userId;
    }

    public BookEvent(BookEvent bookEvent) {
        super(bookEvent.id);
        this.beginDate = bookEvent.beginDate;
        this.expectedEndDate = bookEvent.expectedEndDate;
        this.closeDate = bookEvent.closeDate;
        this.bookId = bookEvent.bookId;
        this.userId = bookEvent.userId;
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


    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public ObjectId getBookId() {
        return bookId;
    }

    public void setBookId(ObjectId bookId) {
        this.bookId = bookId;
    }
}
