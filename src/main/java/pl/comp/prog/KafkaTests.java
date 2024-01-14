package pl.comp.prog;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class KafkaTests {

//    @BeforeAll
//    static void initCassandraAndCreateTable() {
//        cassandraRepo.initKeyspace();
//        cassandraRepo.initSession();
//        cassandraRepo.addBooksTable();
//        BookMapper bookMapper = new BookMapperBuilder(cassandraRepo.getSession()).build();
//        bookDao = bookMapper.bookDao();
//        assertNotNull(bookDao);
//    }
//
//    @Test
//    void testAddAndGetBook() {
//        Book book = new Book( "A.Mickiewicz", "Dziady" );
//        assertNotNull(book);
//        bookDao.addBook(book);
//        Book bookToGet = bookDao.getBookById(book.getId());
//        assertNotNull(bookToGet);
//        assertEquals(book.getId(), bookToGet.getId());
//        assertEquals(book.getTitle(), bookToGet.getTitle());
//        assertEquals(book.getAuthor(), bookToGet.getAuthor());
//    }
}
