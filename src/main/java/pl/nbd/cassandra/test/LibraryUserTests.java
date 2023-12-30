package pl.nbd.cassandra.test;

import com.datastax.oss.driver.api.core.PagingIterable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.nbd.cassandra.dao.LibraryUserDao;
import pl.nbd.cassandra.mappers.BookMapper;
import pl.nbd.cassandra.mappers.BookMapperBuilder;
import pl.nbd.cassandra.mappers.LibraryUserMapper;
import pl.nbd.cassandra.mappers.LibraryUserMapperBuilder;
import pl.nbd.cassandra.model.Book;
import pl.nbd.cassandra.model.LibraryUser;
import pl.nbd.cassandra.repositories.CassandraRepo;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LibraryUserTests {


    static CassandraRepo cassandraRepo = new CassandraRepo();
    static LibraryUserDao libraryUserDao;

    @BeforeAll
    static void initCassandraAndCreateTable() {
        cassandraRepo.initKeyspace();
        cassandraRepo.initSession();
        cassandraRepo.addBooksTable();
        LibraryUserMapper libraryUserMapper = new LibraryUserMapperBuilder(cassandraRepo.getSession()).build();
        libraryUserDao = libraryUserMapper.libraryUserDao();
        assertNotNull(libraryUserDao);
    }

    @Test
    void testAddAndGetLibraryUser() {
        LibraryUser user = new LibraryUser("driller", "driller@drg.com");
        assertNotNull(user);
        libraryUserDao.addLibraryUser(user);
        LibraryUser userToGet = libraryUserDao.getLibraryUserById(user.getId());
        assertNotNull(userToGet);
        assertEquals(user.getId(), userToGet.getId());
        assertEquals(user.getUsername(), userToGet.getUsername());
        assertEquals(user.getEmail(), userToGet.getEmail());
    }

    @Test
    void testDeleteLibraryUser() {
        LibraryUser user = new LibraryUser("driller", "driller@drg.com");
        assertNotNull(user);
        libraryUserDao.addLibraryUser(user);
        libraryUserDao.deleteLibraryUser(user);
        LibraryUser check = libraryUserDao.getLibraryUserById(user.getId());
        assertNull(check);
    }

    @Test
    void testGetAllLibraryUsers() {
        libraryUserDao.addLibraryUser(new LibraryUser("Engineer1", "engineer@drg.com"));
        libraryUserDao.addLibraryUser(new LibraryUser("Engineer1", "engineer@drg.com"));
        PagingIterable<LibraryUser> allUsers = libraryUserDao.getAllLibraryUsers();
        assertNotNull(allUsers);
        assertTrue(allUsers.all().size() >= 2);
    }

    @Test
    void testUpdateLibraryUser() {
        LibraryUser user = new LibraryUser("mr_programmer", "programmer@mail.com");
        libraryUserDao.addLibraryUser(user);
        LibraryUser userGet1 = libraryUserDao.getLibraryUserById(user.getId());
        assertEquals(userGet1.getUsername(), "mr_programmer");
        LibraryUser updatedUser = new LibraryUser( user.getId(), "mr_developer", "programmer@mail.com");
        libraryUserDao.updateLibraryUser(updatedUser);
        LibraryUser userGet2 = libraryUserDao.getLibraryUserById(user.getId());
        assertEquals(userGet2.getUsername(), "mr_developer");
    }



    @AfterAll
    static void closeCassandra() {
        cassandraRepo.closeSession();
    }
}
