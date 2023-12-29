package pl.nbd.cassandra.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import pl.nbd.cassandra.model.Book;

import java.util.ArrayList;
import java.util.UUID;

@Dao
public interface BookDao {

    @Insert
    void addBook(Book book);
    @Select
    Book getBookById(UUID id);

//    @QueryProvider(providerClass = GetBookQueryProvider.class, entityHelpers = {Book.class})
//    Book getBookByTitleAndAuthor(String searchedTitle, String searchedAuthor);

    @Select
    PagingIterable<Book> getAllBooks();

    @Delete
    void deleteBook(Book book);

    //todo change to query provider?
    @Update
    void updateBook(Book updatedBook);


}
