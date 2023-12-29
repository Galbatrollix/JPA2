package pl.nbd.cassandra.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import pl.nbd.cassandra.model.Book;

import java.util.ArrayList;

@Dao
public interface BookDao {

    @Insert
    void addBook(Book book);

    //todo
//    Book getBook(UUID id);
//    ArrayList<Book> getAllBooks();
//
//    void deleteBook(UUID id);
//    void editBook(Book updatedBook);


}
