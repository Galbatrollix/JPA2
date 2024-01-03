package pl.nbd.cassandra.test;

import com.datastax.oss.driver.api.core.PagingIterable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.nbd.cassandra.dao.BookDao;
import pl.nbd.cassandra.dao.LibraryUserDao;
import pl.nbd.cassandra.dao.RatingDao;
import pl.nbd.cassandra.mappers.*;
import pl.nbd.cassandra.model.Book;
import pl.nbd.cassandra.model.LibraryUser;
import pl.nbd.cassandra.model.Rating;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingTests {

    static CassandraRepo cassandraRepo = new CassandraRepo();
    static RatingDao ratingDao;
    static LibraryUserDao libraryUserDao;
    static BookDao bookDao;

    @BeforeAll
    static void initCassandraAndCreateTable() {
        cassandraRepo.initKeyspace();
        cassandraRepo.initSession();
        cassandraRepo.addBooksTable();
        cassandraRepo.addLibraryUsersTable();
        cassandraRepo.addRatingByBookTable();
        cassandraRepo.addRatingByUserTable();
        BookMapper bookMapper = new BookMapperBuilder(cassandraRepo.getSession()).build();
        bookDao = bookMapper.bookDao();
        assertNotNull(bookDao);
        LibraryUserMapper libraryUserMapper = new LibraryUserMapperBuilder(cassandraRepo.getSession()).build();
        libraryUserDao = libraryUserMapper.libraryUserDao();
        assertNotNull(libraryUserDao);
        RatingMapper ratingMapper = new RatingMapperBuilder(cassandraRepo.getSession()).build();
        ratingDao = ratingMapper.ratingDao();
        assertNotNull(ratingDao);
    }


    @Test
    void testAddAndGetRating() {
        Book book = new Book( "A.Mickiewicz", "Dziady" );
        LibraryUser user = new LibraryUser("driller", "driller@drg.com");
        Rating rating = new Rating(5, "good book", book.getId(), user.getId());
        assertNotNull(rating);
        bookDao.addBook(book);
        libraryUserDao.addLibraryUser(user);
        ratingDao.addRating(rating);
        Rating ratingToGet = ratingDao.getRating(book.getId(), user.getId());
        assertNotNull(ratingToGet);
        System.out.println(book.getId());
        System.out.println(user.getId());
        ratingToGet.debugPrint();
        assertEquals(rating.getId(), ratingToGet.getId());
        assertEquals(rating.getComment(), ratingToGet.getComment());
        assertEquals(rating.getStars(), ratingToGet.getStars());
        assertEquals(rating.getBookId(), ratingToGet.getBookId());
        assertEquals(rating.getUserId(), ratingToGet.getUserId());
    }

    @Test
    void testReplacingRating() {
        Book book = new Book( "A.Mickiewicz", "Dziady" );
        LibraryUser user = new LibraryUser("driller", "driller@drg.com");
        Rating rating = new Rating(5, "good book", book.getId(), user.getId());
        Rating rating2 = new Rating(2, "meh", book.getId(), user.getId());
        assertNotNull(rating);
        bookDao.addBook(book);
        libraryUserDao.addLibraryUser(user);
        ratingDao.addRating(rating);
        Rating ratingToGet1 = ratingDao.getRating(book.getId(), user.getId());
        assertEquals(rating.getId(), ratingToGet1.getId());
        assertEquals(rating.getComment(), ratingToGet1.getComment());
        assertEquals(rating.getStars(), ratingToGet1.getStars());
        assertEquals(rating.getBookId(), ratingToGet1.getBookId());
        assertEquals(rating.getUserId(), ratingToGet1.getUserId());
        ratingDao.addRating(rating2);
        Rating ratingToGet2 = ratingDao.getRating(book.getId(), user.getId());
        assertEquals(rating2.getId(), ratingToGet2.getId());
        assertEquals(rating2.getComment(), ratingToGet2.getComment());
        assertEquals(rating2.getStars(), ratingToGet2.getStars());
        assertEquals(rating2.getBookId(), ratingToGet2.getBookId());
        assertEquals(rating2.getUserId(), ratingToGet2.getUserId());

    }

//    @Test
//    void testDeleteBook() {
//        Book book = new Book( "A.Mickiewicz", 11, "Dziady");
//        assertNotNull(book);
//        bookDao.addBook(book);
//        bookDao.deleteBook(book);
//        Book check = bookDao.getBookById(book.getId());
//        assertNull(check);
//    }
//
    @Test
    void testRatingByUserORBook() {
        Book book1 = new Book( "A.Mickiewicz", "Dziady" );
        Book book2 = new Book( "R.Riordan", "Percy Jackson" );
        Book book3 = new Book( "J.K.Rowling", "Harry Potter" );
        LibraryUser user1 = new LibraryUser("driller", "driller@drg.com");
        LibraryUser user2 = new LibraryUser("kapitan_bomba", "bomba@emial.com");
        Rating rating1 = new Rating(5, "good book", book1.getId(), user1.getId());
        Rating rating2 = new Rating(2, "meh", book2.getId(), user2.getId());
        Rating rating3 = new Rating(3, "okay", book3.getId(), user2.getId());

        bookDao.addBook(book1);
        bookDao.addBook(book2);
        bookDao.addBook(book3);

        libraryUserDao.addLibraryUser(user1);
        libraryUserDao.addLibraryUser(user2);

        ratingDao.addRating(rating1);
        ratingDao.addRating(rating2);
        ratingDao.addRating(rating3);

        List<Rating> ratingsBook1 = ratingDao.getRatingsByBookId(book1.getId());
        assertTrue(ratingsBook1.size() >= 1);
        List<Rating> ratingsBook2 = ratingDao.getRatingsByBookId(book1.getId());
        assertTrue(ratingsBook1.size() >= 1);
    }
//
//    @Test
//    void testUpdateBook() {
//        Book book = new Book("A.Programmer", 2, "Programming in Java");
//        bookDao.addBook(book);
//        Book bookGet1 = bookDao.getBookById(book.getId());
//        assertEquals(bookGet1.getTitle(), "Programming in Java");
//        Book updatedBook = new Book(book.getId(), "A.Programmer", 3, "Programming in JavaScript");
//        bookDao.updateBook(updatedBook);
//        Book bookGet2 = bookDao.getBookById(book.getId());
//        assertEquals(bookGet2.getTitle(), "Programming in JavaScript");
//    }
//


    @AfterAll
    static void closeCassandra() {
        cassandraRepo.closeSession();
    }
}
