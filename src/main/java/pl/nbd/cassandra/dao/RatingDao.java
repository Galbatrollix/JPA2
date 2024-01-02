package pl.nbd.cassandra.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;

@Dao
public interface RatingDao {

//    @QueryProvider(providerClass=, entityHelpers = {Rating.class})
//    void addRating(Book updatedBook);
}
