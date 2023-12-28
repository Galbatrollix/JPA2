package pl.nbd.cassandra.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import pl.nbd.cassandra.model.Book;

@Dao
public interface BookDao {

    @Insert
    void addBook(Book book);

    //todo
    //Book findBook(long id);

}
