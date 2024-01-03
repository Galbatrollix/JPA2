package pl.nbd.cassandra.model;


import com.datastax.oss.driver.api.mapper.annotations.Entity;
import com.datastax.oss.driver.api.mapper.annotations.PropertyStrategy;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity(defaultKeyspace = CassandraRepo.KEYSPACE_NAME)
@PropertyStrategy(mutable = false)
public class Lending {
    private UUID id;
    private Date beginDate;

    private Date expectedEndDate;

    private Date closeDate;

    private UUID userId;
    private UUID bookId;

    private static Date daysFromNow(int days) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();

    }

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

    public Lending( UUID bookId, UUID userId) {
        this.id = UUID.randomUUID();
        this.userId = userId;
        this.bookId = bookId;
        this.beginDate = new Date();
        this.expectedEndDate = daysFromNow(30);
        this.closeDate = new Date(0);
    }

    public UUID getId() {
        return id;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public Date getExpectedEndDate() {
        return expectedEndDate;
    }

    public Date getCloseDate() {
        return closeDate;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getBookId() {
        return bookId;
    }

}