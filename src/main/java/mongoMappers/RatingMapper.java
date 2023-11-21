package mongoMappers;

import model.LibraryUser;
import model.Rating;
import org.bson.Document;
import org.bson.types.ObjectId;

public class RatingMapper {
    public static final String ID = "_id";
    public static final String RATING_STARS = "stars";
    public static final String RATING_COMMENT = "comment";
    public static final String RATING_BOOK_ID = "book_id";



    public static Document toMongoRating(Rating rating) {
        Document ratingDocument = new Document(ID, rating.getId()).
                append(RATING_STARS, rating.getStars()).
                append(RATING_COMMENT, rating.getComment()).
                append(RATING_BOOK_ID, rating.getBookId());

        return ratingDocument;
    }

    public static Rating fromMongoRating(Document ratingDocument, Document userDocument) {
        Rating rating = new Rating(
                ratingDocument.get(RATING_STARS, Integer.class),
                ratingDocument.get(RATING_COMMENT, String.class),
                ratingDocument.get(RATING_BOOK_ID, ObjectId.class),
                userDocument.get(ID, ObjectId.class),
                ratingDocument.get(ID, ObjectId.class));
        return rating;
    }
}
