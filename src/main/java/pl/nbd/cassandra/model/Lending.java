package pl.nbd.cassandra.model;


import java.util.Date;
import java.util.UUID;

public class Lending {
    private UUID id;
    private Date beginDate;

    private Date expectedEndDate;

    private Date closeDate;

    private UUID userId;
    private UUID bookId;

    public Lending(UUID id, Date beginDate, Date expectedEndDate, Date closeDate, UUID userId, UUID bookId) {
        this.id = id;
        this.beginDate = beginDate;
        this.expectedEndDate = expectedEndDate;
        this.closeDate = closeDate;
        this.userId = userId;
        this.bookId = bookId;
    }

    public Lending(Date beginDate, Date expectedEndDate, Date closeDate, UUID userId, UUID bookId) {
        this.id = UUID.randomUUID();
        this.beginDate = beginDate;
        this.expectedEndDate = expectedEndDate;
        this.closeDate = closeDate;
        this.userId = userId;
        this.bookId = bookId;
    }

    public UUID getId() {
        return id;
    }


    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(Date expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public void setCloseDate(Date closeDate) {
        this.closeDate = closeDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

}