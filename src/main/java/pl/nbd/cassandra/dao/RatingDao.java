package pl.nbd.cassandra.dao;

import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import pl.nbd.cassandra.model.Rating;
import pl.nbd.cassandra.query_providers.RatingQueryProvider;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

@Dao
public interface RatingDao {

    @QueryProvider(providerClass= RatingQueryProvider.class, entityHelpers = {Rating.class})
    void addRating(Rating rating);

    @QueryProvider(providerClass= RatingQueryProvider.class, entityHelpers = {Rating.class})
    List<Rating> getRatingsByBookId(UUID bookId);

    @QueryProvider(providerClass= RatingQueryProvider.class, entityHelpers = {Rating.class})
    Rating getRating(UUID bookId, UUID userId);
}
