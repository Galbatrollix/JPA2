package pl.nbd.cassandra.dao;


import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;

import pl.nbd.cassandra.model.Rating;
import pl.nbd.cassandra.query_providers.RatingQueryProvider;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

@Dao
public interface RatingDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass= RatingQueryProvider.class, entityHelpers = {Rating.class})
    void addRating(Rating rating);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass= RatingQueryProvider.class, entityHelpers = {Rating.class})
    List<Rating> getRatingsByBookId(UUID bookId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass= RatingQueryProvider.class, entityHelpers = {Rating.class})
    List<Rating> getRatingsByUserId(UUID userId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass= RatingQueryProvider.class, entityHelpers = {Rating.class})
    Rating getRating(UUID bookId, UUID userId);

    @QueryProvider(providerClass= RatingQueryProvider.class, entityHelpers = {Rating.class})
    void deleteRating(UUID bookId, UUID userId);
}
