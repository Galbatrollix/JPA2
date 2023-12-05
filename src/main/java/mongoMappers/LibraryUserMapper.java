package mongoMappers;

import model.Book;
import model.LibraryUser;
import model.Rating;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.lang.reflect.Array;
import java.util.List;

public class LibraryUserMapper {
    public static final String ID = "_id";
    public static final String LIBRARY_USER_EMAIL = "email";
    public static final String LIBRARY_USER_USERNAME = "username";
    public static final String RATINGS = "ratings";

    public static final String RATING_STARS = "stars";
    public static final String RATING_COMMENT = "comment";
    public static final String RATING_BOOK_ID = "bookId";



    public static Document toMongoLibraryUser(LibraryUser user) {
        Document libraryUserDocument = new Document(ID, user.getId()).
                append(LIBRARY_USER_USERNAME, user.getUsername()).
                append(LIBRARY_USER_EMAIL, user.getEmail());

        return libraryUserDocument;
    }

    public static LibraryUser fromMongoLibraryUser(Document document) {
        LibraryUser user = new LibraryUser(
                document.get(LIBRARY_USER_USERNAME, String.class),
                document.get(LIBRARY_USER_EMAIL, String.class),
                document.get(ID, ObjectId.class));
        return user;
    }

    public static LibraryUser fromRedisLibraryUser(Document document) {
        LibraryUser user = new LibraryUser(
                document.get(LIBRARY_USER_USERNAME, String.class),
                document.get(LIBRARY_USER_EMAIL, String.class),
                document.get(ID, String.class));
        return user;
    }
}
