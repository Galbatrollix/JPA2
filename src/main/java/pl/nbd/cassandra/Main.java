package pl.nbd.cassandra;

import pl.nbd.cassandra.dao.BookDao;
import pl.nbd.cassandra.dao.LibraryUserDao;
import pl.nbd.cassandra.mappers.BookMapper;
import pl.nbd.cassandra.mappers.BookMapperBuilder;
import pl.nbd.cassandra.mappers.LibraryUserMapper;
import pl.nbd.cassandra.mappers.LibraryUserMapperBuilder;
import pl.nbd.cassandra.model.Book;
import pl.nbd.cassandra.model.LibraryUser;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.UUID;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println("Test");
        CassandraRepo repo = new CassandraRepo();

        repo.initKeyspace();
        repo.initSession();
        repo.addBooksTable();
        repo.addLibraryUsersTable();

        // WAZNE - zeby generated code byl tu dostepny trzeba kliknac
        // PPM na project folder (JPA [FirstJPaApp]) > Maven > Generate Sources and Upgrade Folders
        // i potem Maven > Reload Project
        BookMapper bookMapper = new BookMapperBuilder(repo.getSession()).build();
        BookDao bookDao = bookMapper.bookDao();
        Book book = new Book( "Percy Jackson 2",10, "R.Riordan");
        bookDao.addBook(book);

        LibraryUserMapper libraryUserMapper = new LibraryUserMapperBuilder(repo.getSession()).build();
        LibraryUserDao libraryUserDao = libraryUserMapper.libraryUserDao();
        LibraryUser libraryUser = new LibraryUser("KapitanBomba", "bomba@kapitan.com");
        libraryUserDao.addLibraryUser(libraryUser);


        repo.closeSession();

    }
}