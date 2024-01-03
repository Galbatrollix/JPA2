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
import pl.nbd.cassandra.model.Lending;
import pl.nbd.cassandra.model.LibraryUser;
import pl.nbd.cassandra.model.Rating;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.Date;
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


    @Test
    void testAddAndGetLending() {
        Book book = new Book( "A.Mickiewicz", "Dziady" );
        System.out.println(book.getId());
        LibraryUser user = new LibraryUser("driller", "driller@drg.com");
        System.out.println(user.getId());
        Lending lending = new Lending(user.getId(), book.getId());
        assertNotNull(lending);
        bookDao.addBook(book);
        libraryUserDao.addLibraryUser(user);
        lendingDao.addLending(lending);
        Lending lendingToGet = lendingDao.getLending(book.getId(), user.getId());
        assertNotNull(lendingToGet);
        assertEquals(lending.getId(), lendingToGet.getId());
        assertEquals(lending.getBeginDate(), lendingToGet.getBeginDate());
        assertEquals(lending.getExpectedEndDate(), lendingToGet.getExpectedEndDate());
        assertEquals(lending.getCloseDate(), lendingToGet.getCloseDate());
        assertEquals(lending.getBookId(), lendingToGet.getBookId());
        assertEquals(lending.getUserId(), lendingToGet.getUserId());
    }

    @AfterAll
    static void closeCassandra() {
        cassandraRepo.closeSession();
    }
}
