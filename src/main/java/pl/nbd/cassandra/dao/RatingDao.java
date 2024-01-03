package pl.nbd.cassandra.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import pl.nbd.cassandra.model.Rating;
import pl.nbd.cassandra.query_providers.RatingQueryProvider;

@Dao
public interface RatingDao {

    @QueryProvider(providerClass= RatingQueryProvider.class, entityHelpers = {Rating.class})
    void addRating(Rating rating);
}
