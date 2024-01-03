package pl.nbd.cassandra.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.nbd.cassandra.dao.BookDao;
import pl.nbd.cassandra.dao.LendingDao;
import pl.nbd.cassandra.dao.LibraryUserDao;
import pl.nbd.cassandra.dao.RatingDao;
import pl.nbd.cassandra.mappers.*;
import pl.nbd.cassandra.model.Book;
import pl.nbd.cassandra.model.LibraryUser;
import pl.nbd.cassandra.model.Rating;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LendingTests {

    static CassandraRepo cassandraRepo = new CassandraRepo();
    static LendingDao lendingDao;
    static LibraryUserDao libraryUserDao;
    static BookDao bookDao;

    @BeforeAll
    static void initCassandraAndCreateTable() {
        cassandraRepo.initKeyspace();
        cassandraRepo.initSession();
        cassandraRepo.addBooksTable();
        cassandraRepo.addLibraryUsersTable();
        cassandraRepo.addLendingByBookTable();
        cassandraRepo.addLendingByUserTable();
        BookMapper bookMapper = new BookMapperBuilder(cassandraRepo.getSession()).build();
        bookDao = bookMapper.bookDao();
        assertNotNull(bookDao);
        LibraryUserMapper libraryUserMapper = new LibraryUserMapperBuilder(cassandraRepo.getSession()).build();
        libraryUserDao = libraryUserMapper.libraryUserDao();
        assertNotNull(libraryUserDao);
        LendingMapper lendingMapper = new LendingMapperBuilder(cassandraRepo.getSession()).build();
        lendingDao = lendingMapper.lendingDao();
        assertNotNull(lendingDao);
    }


//    @Test
//    void testAddAndGetRating() {
//        Book book = new Book( "A.Mickiewicz", "Dziady" );
//        LibraryUser user = new LibraryUser("driller", "driller@drg.com");
//        Lending
//
//        Rating rating = new Rating(5, "good book", book.getId(), user.getId());
//        assertNotNull(rating);
//        bookDao.addBook(book);
//        libraryUserDao.addLibraryUser(user);
//        ratingDao.addRating(rating);
//        Rating ratingToGet = ratingDao.getRating(book.getId(), user.getId());
//        assertNotNull(ratingToGet);
//        System.out.println(book.getId());
//        System.out.println(user.getId());
//        ratingToGet.debugPrint();
//        assertEquals(rating.getId(), ratingToGet.getId());
//        assertEquals(rating.getComment(), ratingToGet.getComment());
//        assertEquals(rating.getStars(), ratingToGet.getStars());
//        assertEquals(rating.getBookId(), ratingToGet.getBookId());
//        assertEquals(rating.getUserId(), ratingToGet.getUserId());
//    }
//
//    @Test
//    void testReplacingRating() {
//        Book book = new Book( "A.Mickiewicz", "Dziady" );
//        LibraryUser user = new LibraryUser("driller", "driller@drg.com");
//        Rating rating = new Rating(5, "good book", book.getId(), user.getId());
//        Rating rating2 = new Rating(2, "meh", book.getId(), user.getId());
//        assertNotNull(rating);
//        bookDao.addBook(book);
//        libraryUserDao.addLibraryUser(user);
//        ratingDao.addRating(rating);
//        Rating ratingToGet1 = ratingDao.getRating(book.getId(), user.getId());
//        assertNotNull(ratingToGet1);
//        assertEquals(rating.getId(), ratingToGet1.getId());
//        assertEquals(rating.getComment(), ratingToGet1.getComment());
//        assertEquals(rating.getStars(), ratingToGet1.getStars());
//        assertEquals(rating.getBookId(), ratingToGet1.getBookId());
//        assertEquals(rating.getUserId(), ratingToGet1.getUserId());
//        ratingDao.addRating(rating2);
//        Rating ratingToGet2 = ratingDao.getRating(book.getId(), user.getId());
//        assertNotNull(ratingToGet2);
//        assertEquals(rating2.getId(), ratingToGet2.getId());
//        assertEquals(rating2.getComment(), ratingToGet2.getComment());
//        assertEquals(rating2.getStars(), ratingToGet2.getStars());
//        assertEquals(rating2.getBookId(), ratingToGet2.getBookId());
//        assertEquals(rating2.getUserId(), ratingToGet2.getUserId());
//
//    }
//
//    @Test
//    void testDeleteRating() {
//        Book book = new Book( "A.Mickiewicz", "Dziady" );
//        LibraryUser user = new LibraryUser("driller", "driller@drg.com");
//        Rating rating = new Rating(5, "i should be deleted", book.getId(), user.getId());
//        assertNotNull(rating);
//        ratingDao.addRating(rating);
//        ratingDao.deleteRating(rating.getBookId(), rating.getUserId());
//        Rating check = ratingDao.getRating(rating.getBookId(), rating.getUserId());
//        assertNull(check);
//    }
//
//    @Test
//    void testRatingByUserORBook() {
//        Book book1 = new Book( "A.Mickiewicz", "Dziady" );
//        Book book2 = new Book( "R.Riordan", "Percy Jackson" );
//        Book book3 = new Book( "J.K.Rowling", "Harry Potter" );
//        LibraryUser user1 = new LibraryUser("driller", "driller@drg.com");
//        LibraryUser user2 = new LibraryUser("kapitan_bomba", "bomba@emial.com");
//        Rating rating1 = new Rating(5, "good book", book1.getId(), user1.getId());
//        Rating rating2 = new Rating(2, "meh", book2.getId(), user2.getId());
//        Rating rating3 = new Rating(3, "okay", book3.getId(), user2.getId());
//
//        bookDao.addBook(book1);
//        bookDao.addBook(book2);
//        bookDao.addBook(book3);
//
//        libraryUserDao.addLibraryUser(user1);
//        libraryUserDao.addLibraryUser(user2);
//
//        ratingDao.addRating(rating1);
//        ratingDao.addRating(rating2);
//        ratingDao.addRating(rating3);
//
//        List<Rating> ratingsBook1 = ratingDao.getRatingsByBookId(book1.getId());
//        assertNotNull(ratingsBook1);
//        assertTrue(ratingsBook1.size() >= 1);
//        List<Rating> ratingsBook2 = ratingDao.getRatingsByBookId(book1.getId());
//        assertNotNull(ratingsBook2);
//        assertTrue(ratingsBook2.size() >= 1);
//
//        List<Rating> ratingsUser1 = ratingDao.getRatingsByUserId(user1.getId());
//        assertNotNull(ratingsUser1);
//        assertTrue(ratingsUser1.size() >= 1);
//        List<Rating> ratingsUser2 = ratingDao.getRatingsByUserId(user2.getId());
//        assertNotNull(ratingsUser2);
//        assertTrue(ratingsUser2.size() >= 2);
//    }

    @AfterAll
    static void closeCassandra() {
        cassandraRepo.closeSession();
    }
}
