package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import model.Rating;
import model.Book;
import model.LibraryUser;
import mongoMappers.LibraryUserMapper;
import mongoMappers.RatingMapper;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;


public class RatingController extends AbstractController {

    private static boolean ratingAlreadyExist(Rating rating) {
        MongoCollection<Document> userCollection = RatingController.repo.getUserCollection();
        Bson filterBookId = Filters.eq(RatingMapper.RATING_BOOK_ID, rating.getBookId());
        Bson filterUser = Filters.and(Filters.eq(LibraryUserMapper.ID, rating.getUserId()), Filters.elemMatch(LibraryUserMapper.RATINGS, filterBookId));
        Document userDoc = userCollection.find(filterUser).first();
        System.out.println(rating.getComment());
        System.out.println(rating.getUserId());
        System.out.println(userDoc);
        return userDoc != null;
    }
    public static Rating addNewRating(Rating rating) {
        if (ratingAlreadyExist(rating)) {
            //TODO proper exception
            System.out.println("Could not add rating " + rating.getStars() + "-" + rating.getComment() + " : rating for this user/book pair already exists");
            return null;
        }
        MongoCollection<Document> userCollection = RatingController.repo.getUserCollection();

        Document ratingDoc = RatingMapper.toMongoRating(rating);
        ratingDoc.put(RatingMapper.ID, new ObjectId());

        Bson filter = Filters.eq(RatingMapper.ID, rating.getUserId());
        Bson update = Updates.push(LibraryUserMapper.RATINGS, ratingDoc);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER);

        userCollection.findOneAndUpdate(filter, update, options);

        Rating resultRating = new Rating(rating);
        resultRating.setId(ratingDoc.getObjectId(RatingMapper.ID));

        return resultRating;

    }

    public static Rating getRating(ObjectId ratingId){
        MongoCollection<Document> userCollection = RatingController.repo.getUserCollection();
        Bson filterRating = Filters.eq(RatingMapper.ID, ratingId);
        Bson filter = Filters.elemMatch(LibraryUserMapper.RATINGS, filterRating);
        Document userDoc = userCollection.find(filter).first();

        ArrayList<Document> retrievedRatings = (ArrayList<Document>)userDoc.get(LibraryUserMapper.RATINGS);

        for (Document ratingDoc : retrievedRatings) {
            if (ratingDoc.getObjectId(RatingMapper.ID).equals(ratingId)) {
                return RatingMapper.fromMongoRating(ratingDoc, userDoc);
            }
        }
        return null;
    }



}
