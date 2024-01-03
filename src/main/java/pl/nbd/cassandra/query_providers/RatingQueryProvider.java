package pl.nbd.cassandra.query_providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchType;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import pl.nbd.cassandra.model.Book;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.BatchStatement;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import pl.nbd.cassandra.model.Rating;
import pl.nbd.cassandra.repositories.CassandraRepo;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class RatingQueryProvider {

    private final CqlSession session;
    private EntityHelper<Rating> ratingEntityHelper;

    public RatingQueryProvider(MapperContext context, EntityHelper<Rating> ratingEntityHelper) {
        this.session = context.getSession();
        this.ratingEntityHelper = ratingEntityHelper;
    }
    public void addRating(Rating rating){
        RegularInsert insert = QueryBuilder.insertInto(CassandraRepo.TABLE_RATING_BY_BOOK)
                .value(CqlIdentifier.fromCql("id"), literal(rating.getId()))
                .value(CqlIdentifier.fromCql("book_id"), literal(rating.getBookId()))
                .value(CqlIdentifier.fromCql("user_id"), literal(rating.getUserId()))
                .value(CqlIdentifier.fromCql("stars"), literal(rating.getStars()))
                .value(CqlIdentifier.fromCql("comment"), literal(rating.getComment()));

        RegularInsert insert2 = QueryBuilder.insertInto(CassandraRepo.TABLE_RATING_BY_USER)
                .value(CqlIdentifier.fromCql("id"), literal(rating.getId()))
                .value(CqlIdentifier.fromCql("user_id"), literal(rating.getUserId()))
                .value(CqlIdentifier.fromCql("book_id"), literal(rating.getBookId()))
                .value(CqlIdentifier.fromCql("stars"), literal(rating.getStars()))
                .value(CqlIdentifier.fromCql("comment"), literal(rating.getComment()));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED).
                addStatement(insert.build()).addStatement(insert2.build())
                .build();

        session.execute(batchStatement);
    }
}
