package mongoMappers;

import model.Book;
import org.bson.Document;
import org.bson.types.ObjectId;

public class BookMapper {
    public static final String ID = "_id";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK_QUANTITY = "quantity";

    public static final String RESERVATION_QUEUE = "reservation_queue";
    public static final String EVENTS_CLOSED = "events_closed";
    public static final String EVENTS_ACTIVE = "events_active";
    public static final String SLOTS = "is_available_slots";


    public static Document toMongoBook(Book book) {
        Document bookDocument = new Document(ID, book.getId()).
                append(BOOK_AUTHOR, book.getAuthor()).
                append(BOOK_TITLE, book.getTitle())
                .append(BOOK_QUANTITY, book.getQuantity());

        return bookDocument;
    }

    public static Book fromMongoBook(Document testDocument) {
        Book book = new Book(
                testDocument.get(BOOK_TITLE, String.class),
                testDocument.get(BOOK_AUTHOR, String.class),
                testDocument.get(BOOK_QUANTITY, Integer.class),
                testDocument.get(ID, ObjectId.class));
        return book;
    }
}
