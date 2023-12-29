package pl.nbd.cassandra.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import pl.nbd.cassandra.model.LibraryUser;

@Dao
public interface LibraryUserDao {

    @Insert
    void addLibraryUser(LibraryUser libraryUser);

    //todo
//    LibraryUser getLibraryUser(UUID id);
//    ArrayList<LibraryUser> getAllBooks();
//
//    void deleteLibraryUser(UUID id);
//    void updateLibraryUser(LibraryUser updatedLibraryUser);
}
