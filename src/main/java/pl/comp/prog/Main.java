package pl.comp.prog;

import com.mongodb.DocumentToDBRefTransformer;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import controllers.*;
import model.*;
import mongoMappers.MGTestMapper;
import mongodbControllers.MongoRepository;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static Book book1;
    static Book book2;
    static Book book3;
    static LibraryUser user1;
    static LibraryUser user2;
    static LibraryUser user3;
    static LibraryUser user4;

//    public static void createExampleBooks() {
//        book1 = BookController.addBookTransaction("Pan Tadeusz", "A.Mickiewicz", 2);
//        book2 = BookController.addBookTransaction("Hunger Games", "S.Collins", 6);
//        book3 = BookController.addBookTransaction("Krzyżacy", "H.Sienkiewicz", 4);
//        Book bookToDelete = BookController.addBookTransaction("This should be deleted", "Author", 2);
//        BookController.deleteBookTransaction(bookToDelete.getId());
//    }

//    public static void createExampleUsers() {
//        user1 = LibraryUserController.addUserTransaction("bomba@email.com", "kapitan");
//        user2 = LibraryUserController.addUserTransaction("gamer@email.com", "gamer");
//        user3 = LibraryUserController.addUserTransaction("multiplier@email.com", "mark");
//        user4 = LibraryUserController.addUserTransaction("kowalski@gmanil.pl", "kowalski");
//        LibraryUser userToDelete = LibraryUserController.addUserTransaction("abcd@email.com", "iwillbedeleted");
//      //  LibraryUserController.deleteUserTransaction(userToDelete.getId());
//    }

//    public static void createExampleRatings() {
//        Rating rating1 = RatingController.addRatingTransaction(5, "Awesome", book1.getId(), user1.getId());
//        Rating ratingToBeDeleted = RatingController.addRatingTransaction(2, "I will be deleted", book3.getId(), user2.getId());
//        Rating ratingThatWillNotBeCreated1 = RatingController.addRatingTransaction(4, "I will not be added", book1.getId(), user1.getId());
//        Rating ratingThatWillNotBeCreated2 = RatingController.addRatingTransaction(5, "I will not be added", 99, 99);
//        RatingController.deleteRatingByIdTransaction(ratingToBeDeleted.getId());
//    }

//    public static void createExampleCatalog() {
//        List<Long> bookIds = new ArrayList<Long>() {{
//            add(1L);
//            add(2L);
//            add(3L);
//        }};
//        CatalogController.addCatalogTransaction("Catalog1", bookIds);
//    }

    public static void createExampleBookEvents() {
//        Reservation rv1 = BookEventQueueController.addReservationTransaction(user1.getId(), book1.getId());
//        Reservation rv2 = BookEventQueueController.addReservationTransaction(user2.getId(), book1.getId());
//        Reservation rv3_ = BookEventQueueController.addReservationTransaction(user2.getId(), book1.getId());
//        Reservation rv4 = BookEventQueueController.addReservationTransaction(user3.getId(), book1.getId());
//        Reservation rv5 = BookEventQueueController.addReservationTransaction(user4.getId(), book1.getId());
//        BookEventQueueController.deleteEventByIdTransaction(rv2.getId());
//        BookEventQueueController.addLendingTransaction(user3.getId(), book1.getId());
//
//        BookEventQueueController.addLendingTransaction(user3.getId(), book3.getId());
//        BookEventQueueController.addLendingTransaction(user3.getId(), book3.getId());
    }

    public static void main(String[] args) throws ClassNotFoundException {
        MongoRepository mongoRepo = new MongoRepository();
        mongoRepo.initDbConnection("bookDB");
        //mongoRepo.createBookCollection();
        //mongoRepo.createTestCollection();
        MongoCollection<Document> collection  = mongoRepo.getTestCollection();
        MGTestModel object1 = new MGTestModel(new ObjectId(),15,"test1");
        InsertOneResult result = collection.insertOne(MGTestMapper.toMongoTest(object1));
        System.out.println(result.getInsertedId());

        Document retreived_doc = collection.find(Filters.eq("_id", result.getInsertedId())).first();
        MGTestModel retreived_class = MGTestMapper.fromMongoTest(retreived_doc);
        System.out.println(retreived_class.test_int +" "+ retreived_class.test_string);
        mongoRepo.close();
//
//        Book book = new Book(0, "aaaa", "bbbb", 0);
//        mongoRepo.addBook(book);
    }
}