package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import model.Book;
import model.LibraryUser;
import mongoMappers.BookMapper;
import mongoMappers.LibraryUserMapper;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;

public class LibraryUserController extends AbstractController{

    public static LibraryUser addNewLibraryUser(LibraryUser user) {
        MongoCollection<Document> collection = LibraryUserController.repo.getUserCollection();
        Document userDoc = LibraryUserMapper.toMongoLibraryUser(user);
        // creating new ID because we are adding new object
        userDoc.put(LibraryUserMapper.ID, new ObjectId());
        userDoc.append(LibraryUserMapper.RATINGS, new ArrayList<Document>());

        InsertOneResult result = collection.insertOne(userDoc);
        BsonValue insertedId = result.getInsertedId();


        LibraryUser result_user = new LibraryUser(user);
        result_user.setId(userDoc.getObjectId("_id"));
        return result_user;

    }

    public static LibraryUser getLibraryUser(ObjectId userId){
        MongoCollection<Document> collection = LibraryUserController.repo.getUserCollection();
        Document retreived_doc = collection.find(Filters.eq("_id", userId)).first();
        return LibraryUserMapper.fromMongoLibraryUser(retreived_doc);
    }

    public static void deleteUser(ObjectId userId) {
        MongoCollection<Document> collection = LibraryUserController.repo.getUserCollection();
        Bson userDelete = Filters.eq("_id", userId);
        collection.deleteOne(userDelete);
    }


    public static void DEBUGPrintAllUsers(){
        MongoCollection<Document> collection = LibraryUserController.repo.getUserCollection();
        MongoCursor< Document > cursor = collection.find().iterator();
        // colors printout cyan to find it easier
        System.out.println("\u001B[36m");
        while (cursor.hasNext()){
            System.out.println(cursor.next());
        }
        System.out.println("\u001B[0m");
    }



}
