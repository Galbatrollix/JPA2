package pl.nbd.cassandra.test;

import com.datastax.oss.driver.api.core.PagingIterable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import pl.nbd.cassandra.dao.BookDao;
import pl.nbd.cassandra.mappers.BookMapper;
import pl.nbd.cassandra.mappers.BookMapperBuilder;
import pl.nbd.cassandra.model.Book;
import pl.nbd.cassandra.repositories.CassandraRepo;

public class BookTests {
//    private final PrintStream standardOut = System.out;
//    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    static CassandraRepo cassandraRepo = new CassandraRepo();
    static BookDao bookDao;

    @BeforeAll
    static void initCassandraAndCreateTable() {
        cassandraRepo.initKeyspace();
        cassandraRepo.initSession();
        cassandraRepo.addBooksTable();
        BookMapper bookMapper = new BookMapperBuilder(cassandraRepo.getSession()).build();
        bookDao = bookMapper.bookDao();
        assertNotNull(bookDao);
    }

    @Test
    void testAddAndGetBook() {
        Book book = new Book( "A.Mickiewicz", 11, "Dziady" );
        assertNotNull(book);
        bookDao.addBook(book);
        Book bookToGet = bookDao.getBookById(book.getId());
        book.debugPrint();
        bookToGet.debugPrint();
        assertNotNull(bookToGet);
        assertEquals(book.getId(), bookToGet.getId());
        assertEquals(book.getTitle(), bookToGet.getTitle());
        assertEquals(book.getQuantity(), bookToGet.getQuantity());
        assertEquals(book.getAuthor(), bookToGet.getAuthor());
    }

    @Test
    void testDeleteBook() {
        Book book = new Book( "A.Mickiewicz", 11, "Dziady");
        assertNotNull(book);
        bookDao.addBook(book);
        bookDao.deleteBook(book);
        Book check = bookDao.getBookById(book.getId());
        assertNull(check);
    }

    @Test
    void testGetAllBooks() {
        bookDao.addBook(new Book("J.K.Rowling", 9, "Harry Potter 1"));
        bookDao.addBook(new Book("J.K.Rowling", 1, "Harry Potter 2"));
        PagingIterable<Book> allBooks = bookDao.getAllBooks();
        assertNotNull(allBooks);
        assertTrue(allBooks.all().size() >= 2);
    }

    @Test
    void testUpdateBook() {
        Book book = new Book("A.Programmer", 2, "Programming in Java");
        bookDao.addBook(book);
        Book bookGet1 = bookDao.getBookById(book.getId());
        assertEquals(bookGet1.getTitle(), "Programming in Java");
        Book updatedBook = new Book(book.getId(), "A.Programmer", 3, "Programming in JavaScript");
        bookDao.updateBook(updatedBook);
        Book bookGet2 = bookDao.getBookById(book.getId());
        assertEquals(bookGet2.getTitle(), "Programming in JavaScript");
    }



    @AfterAll
    static void closeCassandra() {
        cassandraRepo.closeSession();
    }
}
