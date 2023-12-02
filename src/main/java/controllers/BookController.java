package controllers;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import model.Book;
import mongoMappers.BookMapper;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import redisRepo.RedisRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookController extends AbstractController {
    public static Book addNewBook(Book book) {
        MongoCollection<Document> collection = BookController.mongoRepo.getBookCollection();
        Document bookDoc = BookMapper.toMongoBook(book);
        // creating new ID because we are adding new object
        bookDoc.put(BookMapper.ID, new ObjectId());
        bookDoc.append(BookMapper.RESERVATION_QUEUE, new ArrayList<Document>());
        bookDoc.append(BookMapper.EVENTS_CLOSED, new ArrayList<Document>());
        bookDoc.append(BookMapper.EVENTS_ACTIVE, new ArrayList<Document>());
        bookDoc.append(BookMapper.SLOTS, Collections.nCopies(book.getQuantity(), 1));

        collection.insertOne(bookDoc);
        BookController.addToCash(bookDoc, RedisRepository.bookHashPrefix, 300);

        Book result_book = new Book(book);
        result_book.setId(bookDoc.getObjectId(BookMapper.ID));
        return result_book;

    }

    public static Book getBook(ObjectId bookId){
        Document bookFromCasheDoc = BookController.redisRepo.getDocumentFromCashe(RedisRepository.bookHashPrefix, bookId);

        if (bookFromCasheDoc != null) {
            System.out.println("got book from cashe");
            return  BookMapper.fromMongoBook(bookFromCasheDoc);
        }
        MongoCollection<Document> collection = BookController.mongoRepo.getBookCollection();
        Document retreived_doc = collection.find(Filters.eq(BookMapper.ID, bookId)).first();
        return BookMapper.fromMongoBook(retreived_doc);
    }

    public static void deleteBook(ObjectId bookId) {
        MongoCollection<Document> collection = BookController.mongoRepo.getBookCollection();
        Bson bookDelete = Filters.eq(BookMapper.ID, bookId);
        collection.deleteOne(bookDelete);
    }

    public static void DEBUGPrintAllBooks(){
        MongoCollection<Document> collection = BookController.mongoRepo.getBookCollection();
        MongoCursor< Document > cursor = collection.find().iterator();
        // colors printout cyan to find it easier
        System.out.println("\u001B[36m");
        while (cursor.hasNext()){
            System.out.println(cursor.next());
        }
        System.out.println("\u001B[0m");
    }

    public static List<Book> getAllBooks() {
        MongoCollection<Document> collection = BookController.mongoRepo.getBookCollection();
        MongoCursor< Document > cursor = collection.find().iterator();
        List<Book> books = new ArrayList<Book>();

        while (cursor.hasNext()){
            books.add(BookMapper.fromMongoBook(cursor.next()));
        }
        return books;
    }

}