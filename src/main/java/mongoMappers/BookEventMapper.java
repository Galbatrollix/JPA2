package mongoMappers;


import model.BookEvent;
import model.Lending;
import model.Reservation;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class BookEventMapper {

    public static final String ID = "_id";
    public static final String CLASS_DISCRIMINATOR = "_class";
    public static final String BEGIN = "begin_date";
    public static final String EXPECTED_END = "expected_end_date";
    public static final String CLOSE = "close_date";

    public static final String RESERVATION_DISCRIMIATOR = "reservation";
    public static final String LENDING_DISCRIMINATOR = "lending";
    public static final String USER_ID = "user_id";


    public static Document toMongoBookEvent(BookEvent event) {
        Document eventDocument = new Document(ID, event.getId());

        eventDocument.append(BEGIN, event.getBeginDate());
        eventDocument.append(EXPECTED_END, event.getExpectedEndDate());
        if (event.getCloseDate() != null){
            eventDocument.append(CLOSE, event.getCloseDate());
        }

        if (event instanceof Lending){
            eventDocument.append(CLASS_DISCRIMINATOR, LENDING_DISCRIMINATOR);
        }else if ( event instanceof Reservation){
            eventDocument.append(CLASS_DISCRIMINATOR, RESERVATION_DISCRIMIATOR);
        }


        return eventDocument;
    }

    public static BookEvent fromMongoBookEvent(Document eventDocument) {
        if (eventDocument.get(CLASS_DISCRIMINATOR, String.class).equals(LENDING_DISCRIMINATOR)){
            return new Lending(
                    eventDocument.get(ID, ObjectId.class),
                    eventDocument.get(BEGIN, Date.class),
                    eventDocument.get(EXPECTED_END, Date.class),
                    eventDocument.get(CLOSE, Date.class)
                    );
        }else{
            return new Reservation(
                    eventDocument.get(ID, ObjectId.class),
                    eventDocument.get(BEGIN, Date.class),
                    eventDocument.get(EXPECTED_END, Date.class),
                    eventDocument.get(CLOSE, Date.class)
            );
        }

    }



}
