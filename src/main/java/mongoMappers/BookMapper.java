package mongoMappers;

import model.Book;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;

public class BookMapper {
    public static final String ID = "_id";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK_QUANTITY = "quantity";

    public static final String RESERVATION_QUEUE = "reservation_queue";
    public static final String EVENTS_CLOSED = "events_closed";
    public static final String EVENTS_ACTIVE = "events_active";
    public static final String SLOTS = "is_available_slots";

    public static final String CATALOG = "catalog";


    public static Document toMongoBook(Book book) {
        Document bookDocument = new Document(ID, book.getId()).
                append(BOOK_AUTHOR, book.getAuthor()).
                append(BOOK_TITLE, book.getTitle())
                .append(BOOK_QUANTITY, book.getQuantity());

        return bookDocument;
    }

    public static Book fromMongoBook(Document bookDocument) {
        Book book = new Book(
                bookDocument.get(BOOK_TITLE, String.class),
                bookDocument.get(BOOK_AUTHOR, String.class),
                bookDocument.get(BOOK_QUANTITY, Double.class),
                bookDocument.get(ID, ObjectId.class));
        return book;
    }

    public static Book fromRedisBook(Document bookDocument) {
        Book book = new Book(
                bookDocument.get(BOOK_TITLE, String.class),
                bookDocument.get(BOOK_AUTHOR, String.class),
                bookDocument.get(BOOK_QUANTITY, Double.class),
                bookDocument.get(ID, String.class));
        return book;
    }

}
