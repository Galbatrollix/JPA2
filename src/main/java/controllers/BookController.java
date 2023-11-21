package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import model.Book;
import mongoMappers.BookMapper;
import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Collections;

public class BookController extends AbstractController {
    public static Book addNewBook(Book book) {
        MongoCollection<Document> collection = BookController.repo.getBookCollection();
        Document bookDoc = BookMapper.toMongoBook(book);
        // creating new ID because we are adding new object
        bookDoc.put(BookMapper.ID, new ObjectId());
        bookDoc.append(BookMapper.RESERVATION_QUEUE, new ArrayList<Document>());
        bookDoc.append(BookMapper.EVENTS_CLOSED, new ArrayList<Document>());
        bookDoc.append(BookMapper.EVENTS_ACTIVE, new ArrayList<Document>());
        bookDoc.append(BookMapper.SLOTS, Collections.nCopies(book.getQuantity(), 1));

        InsertOneResult result = collection.insertOne(bookDoc);
        BsonValue insertedId = result.getInsertedId();
        //I didnt manage to find an easy way to map this value back to ObjectId...


        Book result_book = new Book(book);
        result_book.setId(bookDoc.getObjectId("_id"));
        return result_book;

    }

    public static Book getBook(ObjectId bookId){
        MongoCollection<Document> collection = BookController.repo.getBookCollection();
        Document retreived_doc = collection.find(Filters.eq("_id", bookId)).first();
        return BookMapper.fromMongoBook(retreived_doc);
    }

    public static void DEBUGPrintAllBooks(){
        MongoCollection<Document> collection = BookController.repo.getBookCollection();
        MongoCursor< Document > cursor = collection.find().iterator();
        // colors prinout cyan to find it easier
        System.out.println("\u001B[36m");
        while (cursor.hasNext()){
            System.out.println(cursor.next());
        }
        System.out.println("\u001B[0m");
    }

}