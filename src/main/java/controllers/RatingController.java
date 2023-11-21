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
    public static Rating addNewRating(Rating rating) {
        System.out.println("addNewRating");
        MongoCollection<Document> userCollection = RatingController.repo.getUserCollection();

        //TODO add check if the rating for given book/user already exists
        Document ratingDoc = RatingMapper.toMongoRating(rating);
        ratingDoc.put(RatingMapper.ID, new ObjectId());

        Bson filter = Filters.eq("_id", rating.getUserId());
        Bson update = Updates.push(LibraryUserMapper.RATINGS, ratingDoc);
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions()
                .returnDocument(ReturnDocument.AFTER);

        userCollection.findOneAndUpdate(filter, update, options);

        Rating resultRating = new Rating(rating);
        resultRating.setId(ratingDoc.getObjectId("_id"));

        return resultRating;

    }

    public static Rating getRating(ObjectId ratingId){
        MongoCollection<Document> userCollection = RatingController.repo.getUserCollection();
        Bson filterRating = Filters.eq("_id", ratingId);
        Bson filter = Filters.elemMatch("ratings", filterRating);
        Document userDoc = userCollection.find(filter).first();

        ArrayList<Document> retrievedRatings = (ArrayList<Document>)userDoc.get(LibraryUserMapper.RATINGS);

        for (Document ratingDoc : retrievedRatings) {
            if (ratingDoc.getObjectId("_id").equals(ratingId)) {
                return RatingMapper.fromMongoRating(ratingDoc, userDoc);
            }
        }
        return null;
    }



}
