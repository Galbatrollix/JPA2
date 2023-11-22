package pl.comp.prog;

import controllers.AbstractController;
import controllers.BookController;
import controllers.LibraryUserController;
import controllers.RatingController;
import model.*;
import org.bson.types.ObjectId;

public class Main {
    static Book book1;
    static Book book2;
    static Book book3;
    static LibraryUser user1;
    static LibraryUser user2;
    static LibraryUser user3;
    static LibraryUser user4;

    public static void createExampleBooks() {
        book1 =  BookController.addNewBook(new Book("Pan Tadeusz", "A.Mickiewicz", 2));
        book2 = BookController.addNewBook(new Book("Hunger Games", "S.Collins", 6));
        book3 = BookController.addNewBook(new Book("Krzyżacy", "H.Sienkiewicz", 4));
        Book bookToDelete = BookController.addNewBook(new Book("This should be deleted", "Author", 2));
        BookController.DEBUGPrintAllBooks();

        BookController.deleteBook(bookToDelete.getId());
        BookController.DEBUGPrintAllBooks();
    }

    public static void createExampleUsers() {
        user1 = LibraryUserController.addNewLibraryUser(new LibraryUser("bomba@email.com", "kapitan"));
        user2 = LibraryUserController.addNewLibraryUser(new LibraryUser("gamer@email.com", "gamer"));
        user3 = LibraryUserController.addNewLibraryUser(new LibraryUser("multiplier@email.com", "mark"));
        user4 = LibraryUserController.addNewLibraryUser(new LibraryUser("kowalski@gmanil.pl", "kowalski"));
        LibraryUser userToDelete = LibraryUserController.addNewLibraryUser(new LibraryUser("abcd@email.com", "iwillbedeleted"));
        LibraryUserController.DEBUGPrintAllUsers();

        LibraryUserController.deleteUser(userToDelete.getId());
        LibraryUserController.DEBUGPrintAllUsers();
    }

    public static void createExampleRatings() {
        Rating rating1 = RatingController.addNewRating(new Rating(5, "Awesome", book1.getId(), user1.getId()));
        Rating ratingToBeDeleted = RatingController.addNewRating(new Rating(2, "I will be deleted", book3.getId(), user2.getId()));
        Rating ratingThatWillNotBeCreated1 = RatingController.addNewRating(new Rating(4, "I will not be added", book1.getId(), user1.getId()));
        Rating ratingThatWillNotBeCreated2 = RatingController.addNewRating(new Rating(5, "I will not be added", new ObjectId(), new ObjectId()));

        LibraryUserController.DEBUGPrintAllUsers();
        RatingController.deleteRating(ratingToBeDeleted.getId());
        LibraryUserController.DEBUGPrintAllUsers();

    }

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
        AbstractController.attachMongoRepository();
        //AbstractController.initMongoCollections();

        createExampleBooks();
        createExampleUsers();
        createExampleRatings();


        AbstractController.closeMongoRepository();

    }
}