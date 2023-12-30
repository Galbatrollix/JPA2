package pl.nbd.cassandra.dao;

import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.mapper.annotations.*;
import pl.nbd.cassandra.model.Book;
import pl.nbd.cassandra.model.LibraryUser;
import pl.nbd.cassandra.query_providers.BookQueryProvider;
import pl.nbd.cassandra.query_providers.LibraryUserQueryProvider;

import java.util.UUID;

@Dao
public interface LibraryUserDao {

    @Insert
    void addLibraryUser(LibraryUser libraryUser);

    @Select
    LibraryUser getLibraryUserById(UUID id);

    @Select
    PagingIterable<LibraryUser> getAllLibraryUsers();

    @Delete
    void deleteLibraryUser(LibraryUser libraryUser);

    @QueryProvider(providerClass = LibraryUserQueryProvider.class, entityHelpers = {LibraryUser.class})
    void updateLibraryUser(LibraryUser updatedLibraryUser);
}
