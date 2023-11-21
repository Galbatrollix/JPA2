package mongoMappers;

import model.Book;
import model.MGTestModel;
import org.bson.Document;
import org.bson.types.ObjectId;

public class BookMapper {
    public static final String ID = "_id";
    public static final String BOOK_AUTHOR = "author";
    public static final String BOOK_TITLE = "title";
    public static final String BOOK_QUANTITY = "quantity";

    public static Document toMongoBook(Book book) {
        Document bookDocument = new Document(ID, book.getId()).
                append(BOOK_AUTHOR, book.getAuthor()).
                append(BOOK_TITLE, book.getTitle())
                .append(BOOK_QUANTITY, book.getQuantity());

        return bookDocument;
    }

    public static Book fromMongoBook(Document testDocument) {
        Book book = new Book(
                testDocument.get(ID, ObjectId.class),
                testDocument.get(BOOK_TITLE, String.class),
                testDocument.get(BOOK_AUTHOR, String.class),
                testDocument.get(BOOK_QUANTITY, Integer.class));
        return book;
    }
}
