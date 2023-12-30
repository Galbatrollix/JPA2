package pl.nbd.cassandra;

import com.datastax.oss.driver.api.core.PagingIterable;
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

        repo.addLendingByBookTable();
        repo.addLendingByUserTable();
        repo.addRatingByBookTable();
        repo.addRatingByUserTable();


        // WAZNE - zeby generated code byl tu dostepny trzeba kliknac
        // PPM na project folder (JPA [FirstJPaApp]) > Maven > Generate Sources and Upgrade Folders
        // i potem Maven > Reload Project
         BookMapper bookMapper = new BookMapperBuilder(repo.getSession()).build();
       // BookDao bookDao = bookMapper.bookDao();
//        Book book = new Book( "Harry Potter",10, "J.K.Rowling");
//        bookDao.addBook(book);
//        book.setQuantity(2);
//        bookDao.updateBook(book);
        //Book bookToGet1 = bookDao.getBookById(UUID.fromString("4452a616-aebf-4978-a194-14e5c021febd"));
//        bookToGet1.debugPrint();



//        PagingIterable<Book> allBook = bookDao.getAllBooks();
//        allBook.forEach(book -> {
//            System.out.println(book.getAuthor() + " " + book.getTitle());
//        });

      //  LibraryUserMapper libraryUserMapper = new LibraryUserMapperBuilder(repo.getSession()).build();
       // LibraryUserDao libraryUserDao = libraryUserMapper.libraryUserDao();
//        LibraryUser libraryUser = new LibraryUser("Mateo", "mateo@mail.com");
//        libraryUserDao.addLibraryUser(libraryUser);


        repo.closeSession();

    }
}