package pl.nbd.cassandra.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import pl.nbd.cassandra.model.Book;
import pl.nbd.cassandra.query_providers.BookQueryProvider;

import java.util.ArrayList;
import java.util.UUID;

@Dao
public interface BookDao {

    @Insert
    void addBook(Book book);
    @Select
    Book getBookById(UUID id);

    @Select
    PagingIterable<Book> getAllBooks();

    @Delete
    void deleteBook(Book book);


    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = BookQueryProvider.class, entityHelpers = {Book.class})
    void updateBook(Book updatedBook);


}
