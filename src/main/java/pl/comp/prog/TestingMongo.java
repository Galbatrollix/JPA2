package pl.comp.prog;


import controllers.*;
import model.*;
import org.bson.types.ObjectId;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestingMongo {

    static Book book1;
    static Book book2;
    static Book book3;
    static LibraryUser user1;
    static LibraryUser user2;
    static LibraryUser user3;
    static LibraryUser user4;


    @BeforeAll
    static void initMongo() {
        AbstractController.attachMongoRepository();
        //AbstractController.initMongoCollections();
    }

    @AfterAll
    static void closeMongo() {
        AbstractController.closeMongoRepository();
    }

    private boolean checkIfBookInDB(Book bookToCheck) {
        List<Book> books = BookController.getAllBooks();
        boolean containsBook = false;

        for (Book book : books) {
            if (book.getId().equals(bookToCheck.getId())) {
                containsBook = true;
                break;
            }
        }
        return containsBook;
    }

    private boolean checkIfUserInDB(LibraryUser userToCheck) {
        List<LibraryUser> users = LibraryUserController.getAllUsers();
        boolean containsUser = false;

        for (LibraryUser user: users) {
            if (user.getId().equals(userToCheck.getId())) {
                containsUser = true;
                break;
            }
        }
        return containsUser;
    }

    private boolean checkIfRatingInDB(Rating ratingToCheck) {
        List<LibraryUser> users = LibraryUserController.getAllUsers();
        boolean containsRating = false;

        for (LibraryUser user: users) {
            List<Rating> ratings = RatingController.getAllRatingsOfUser(user.getId());
            for (Rating rating: ratings) {
                if (rating.getId().equals(ratingToCheck.getId())) {
                    containsRating = true;
                    break;
                }
                if (containsRating) {
                    break;
                }
            }
        }

        return containsRating;
    }
    @Test
    void testBookCreation() {
        Book book = BookController.addNewBook(new Book("Pan Tadeusz", "A.Mickiewicz", 2));
        assertNotNull(book);
        assertNotNull(book.getId());

        boolean containsBook = checkIfBookInDB(book);
        assertTrue(containsBook);
    }

    @Test
    public void testBookDeletion() {
        Book bookToDelete = BookController.addNewBook(new Book("This should be deleted", "Author", 2));
        assertNotNull(bookToDelete);
        assertNotNull(bookToDelete.getId());
        BookController.deleteBook(bookToDelete.getId());

        boolean containsBook = checkIfBookInDB(bookToDelete);

        assertFalse(containsBook);
    }

    @Test
    public void testUserCreation() {
        LibraryUser user = LibraryUserController.addNewLibraryUser(new LibraryUser("bomba@email.com", "kapitan"));
        assertNotNull(user);
        assertNotNull(user.getId());

        boolean containsUser = checkIfUserInDB(user);
        assertTrue(containsUser);
    }

    @Test
    public void testUserDeletion() {
        LibraryUser userToDelete = LibraryUserController.addNewLibraryUser(new LibraryUser("delet@mail.com", "Mr deleted"));
        assertNotNull(userToDelete);
        assertNotNull(userToDelete.getId());
        LibraryUserController.deleteUser(userToDelete.getId());

        boolean containsBook = checkIfUserInDB(userToDelete);

        assertFalse(containsBook);
    }


    //TODO
    public void testRatingCreation() {
        Rating rating1 = RatingController.addNewRating(new Rating(5, "Awesome", book1.getId(), user1.getId()));
        Rating ratingToBeDeleted = RatingController.addNewRating(new Rating(2, "I will be deleted", book3.getId(), user2.getId()));
        Rating ratingThatWillNotBeCreated1 = RatingController.addNewRating(new Rating(4, "I will not be added", book1.getId(), user1.getId()));
        Rating ratingThatWillNotBeCreated2 = RatingController.addNewRating(new Rating(5, "I will not be added", new ObjectId(), new ObjectId()));

        LibraryUserController.DEBUGPrintAllUsers();
        RatingController.deleteRating(ratingToBeDeleted.getId());
        LibraryUserController.DEBUGPrintAllUsers();

    }

    public void testCatalogCreation() {

        Catalog catalog1 = CatalogController.addNewCatalog(new Catalog("Catalog1", book1.getId()));
        Catalog catalogToDelete = CatalogController.addNewCatalog(new Catalog("Catalog to delete", book2.getId()));
        BookController.DEBUGPrintAllBooks();
        CatalogController.deleteCatalog(catalogToDelete.getId());
        BookController.DEBUGPrintAllBooks();
    }

    public static void createExampleEvents() {
        Reservation reservation = BookEventQueueController.addNewReservation(user1.getId(), book1.getId());
        BookController.DEBUGPrintAllBooks();
        Reservation shouldNotBeAdded = BookEventQueueController.addNewReservation(user1.getId(), book1.getId());
        Lending lending = BookEventQueueController.addNewLending(user2.getId(), book2.getId());
        BookController.DEBUGPrintAllBooks();
        Lending shouldNotBeAdded2 = BookEventQueueController.addNewLending(user1.getId(), book1.getId());

    }


}
