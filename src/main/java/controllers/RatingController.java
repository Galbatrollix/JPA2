package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import model.Rating;
import mongoMappers.LibraryUserMapper;
import mongoMappers.RatingMapper;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


public class RatingController extends AbstractController {

    private static boolean ratingAlreadyExist(Rating rating) {
        MongoCollection<Document> userCollection = RatingController.mongoRepo.getUserCollection();
        Bson filterBookId = Filters.eq(RatingMapper.RATING_BOOK_ID, rating.getBookId());
        Bson filterUser = Filters.and(Filters.eq("_id", rating.getUserId()), Filters.elemMatch(LibraryUserMapper.RATINGS, filterBookId));
        Document userDoc = userCollection.find(filterUser).first();
        return userDoc != null;
    }
    public static Rating addNewRating(Rating rating) {
        if (ratingAlreadyExist(rating)) {
            //TODO proper exception
            System.out.println("Could not add rating " + rating.getStars() + "-" + rating.getComment() + " : rating for this user/book pair already exists");
            return null;
        }
        MongoCollection<Document> userCollection = RatingController.mongoRepo.getUserCollection();

        Document ratingDoc = RatingMapper.toMongoRating(rating);
        ratingDoc.put(RatingMapper.ID, new ObjectId());

        Bson filter = Filters.eq("_id", rating.getUserId());
        Bson update = Updates.push(LibraryUserMapper.RATINGS, ratingDoc);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER);

        userCollection.findOneAndUpdate(filter, update, options);

        Rating resultRating = new Rating(rating);
        resultRating.setId(ratingDoc.getObjectId("_id"));

        System.out.println("Added rating " + rating.getStars() + "-" + rating.getComment());
        return resultRating;

    }

    public static Rating getRating(ObjectId ratingId){
        MongoCollection<Document> userCollection = RatingController.mongoRepo.getUserCollection();
        Bson filterRating = Filters.eq("_id", ratingId);
        Bson filter = Filters.elemMatch(LibraryUserMapper.RATINGS, filterRating);
        Document userDoc = userCollection.find(filter).first();

        ArrayList<Document> retrievedRatings = (ArrayList<Document>)userDoc.get(LibraryUserMapper.RATINGS);

        for (Document ratingDoc : retrievedRatings) {
            if (ratingDoc.getObjectId("_id").equals(ratingId)) {
                return RatingMapper.fromMongoRating(ratingDoc, userDoc);
            }
        }
        return null;
    }

    public static List<Rating> getAllRatingsOfUser(ObjectId userID){
        MongoCollection<Document> userCollection = RatingController.mongoRepo.getUserCollection();
        Bson filter = Filters.eq("_id", userID);
        Document userDoc = userCollection.find(filter).first();

        ArrayList<Document> retrievedRatings = (ArrayList<Document>)userDoc.get(LibraryUserMapper.RATINGS);

        List<Rating> ratings = new ArrayList<Rating>();
        for (Document ratingDoc : retrievedRatings) {
            ratings.add(RatingMapper.fromMongoRating(ratingDoc, userDoc));
        }
        return ratings;
    }

    public static void deleteRating(ObjectId ratingId) {
        MongoCollection<Document> userCollection = RatingController.mongoRepo.getUserCollection();
        Bson filterRating = Filters.eq("_id", ratingId);
        Bson filter = Filters.elemMatch(LibraryUserMapper.RATINGS, filterRating);
        Document userDoc = userCollection.find(filter).first();

        Rating ratingDoc = getRating(ratingId);
        Bson filterUser = Filters.eq("_id", userDoc.getObjectId("_id"));
        Bson update = Updates.pull(LibraryUserMapper.RATINGS, filterRating);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER);

        userCollection.findOneAndUpdate(filterUser, update, options);
    }



}
