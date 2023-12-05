package pl.comp.prog;

import controllers.*;
import model.Book;
import model.Catalog;
import model.LibraryUser;
import model.Rating;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redisRepo.RedisRepository;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestingRedis {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeAll
    static void initDBAndCashe() {
        AbstractController.attachMongoRepository();
    }

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @AfterAll
    static void closeMongo() {
        AbstractController.closeMongoRepository();
    }


    @Test
    void testBookAddToCashe() {
        Book book = BookController.addNewBook(new Book("Percy Jackson", "R.Riordan", 10));
        assertNotNull(book);
        assertNotNull(book.getId());
        assertNotNull(BookController.getFromCashe(RedisRepository.bookHashPrefix, book.getId()));
    }

    @Test
    void testUserAddToCashe() {
        LibraryUser user = LibraryUserController.addNewLibraryUser(new LibraryUser("Jan Kowal", "mail@uni.com"));
        assertNotNull(user);
        assertNotNull(user.getId());
        assertNotNull(LibraryUserController.getFromCashe(RedisRepository.userHashPrefix, user.getId()));
    }

    @Test
    void testRatingAddToCashe() {
        LibraryUser user = LibraryUserController.addNewLibraryUser(new LibraryUser("Jan Kowal", "mail@uni.com"));
        Book book = BookController.addNewBook(new Book("Percy Jackson", "R.Riordan", 10));
        Rating rating = RatingController.addNewRating(new Rating(5, "So cool", book.getId(), user.getId()));
        assertNotNull(rating);
        assertNotNull(rating.getId());
        Document ratingDoc = RatingController.getFromCashe(RedisRepository.ratingHashPrefix, rating.getId());
        assertNotNull(ratingDoc);
        assertEquals(ratingDoc.get("book_id"), book.getId().toString());
        assertEquals(ratingDoc.get("userId"), user.getId().toString());
        Document userDoc = LibraryUserController.getFromCashe(RedisRepository.userHashPrefix, user.getId());
        ArrayList<Document> ratingsOfUser = (ArrayList<Document>) userDoc.get("ratings");
        assertEquals(ratingsOfUser.size(), 1);
    }

    @Test
    void testCatalogAddToCashe() {
        Book book = BookController.addNewBook(new Book("Percy Jackson", "R.Riordan", 10));
        Catalog catalog = CatalogController.addNewCatalog(new Catalog("Fantasy", book.getId()));
        assertNotNull(catalog);
        assertNotNull(catalog.getId());
        Document catalogDoc = CatalogController.getFromCashe(RedisRepository.catalogHashPrefix, catalog.getId());
        assertNotNull(catalogDoc);
        assertEquals(catalogDoc.get("bookId"), book.getId().toString());
        Document bookDoc = BookController.getFromCashe(RedisRepository.bookHashPrefix, book.getId());
        assertTrue(bookDoc.containsKey("catalog"));
    }


    @Test
    void testBookGetFromCashe() {
        Book book = BookController.addNewBook(new Book("Percy Jackson", "R.Riordan", 10));
        Book bookGet = BookController.getBook(book.getId());
        assertNotNull(bookGet);
        assertEquals(book.getId(), bookGet.getId());
        assertTrue(outputStreamCaptor.toString()
                .contains("got book from cashe"));
    }

    @Test
    void testUserGetFromCashe() {
        LibraryUser user = LibraryUserController.addNewLibraryUser(new LibraryUser("Jan Kowal", "mail@uni.com"));
        LibraryUser userGet = LibraryUserController.getLibraryUser(user.getId());
        assertNotNull(userGet);
        assertEquals(user.getId(), userGet.getId());
        assertTrue(outputStreamCaptor.toString()
                .contains("got user from cashe"));
    }

    @Test
    void testCatalogGetFromCashe() {
        Book book = BookController.addNewBook(new Book("Percy Jackson", "R.Riordan", 10));
        Catalog catalog = CatalogController.addNewCatalog(new Catalog("Fantasy", book.getId()));
        Catalog catalogGet = CatalogController.getCatalog(catalog.getId());
        assertNotNull(catalogGet);
        assertEquals(catalog.getId(), catalogGet.getId());
        assertEquals(catalog.getBookId(), catalogGet.getBookId());
        assertTrue(outputStreamCaptor.toString()
                .contains("got catalog from cashe"));
    }

    @Test
    void clearCashe() {
        Book book = BookController.addNewBook(new Book("Percy Jackson", "R.Riordan", 10));
        assertNotNull(book);
        BookController.clearCashe();
        Document bookDovGet = BookController.getFromCashe(RedisRepository.bookHashPrefix, book.getId());
        assertNull(bookDovGet);

    }


}
