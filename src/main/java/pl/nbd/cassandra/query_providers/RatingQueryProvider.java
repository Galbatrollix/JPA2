package pl.nbd.cassandra.query_providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import pl.nbd.cassandra.model.Book;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import pl.nbd.cassandra.model.Rating;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
                .value(CqlIdentifier.fromCql("book_id"), literal(rating.getBookId()))
                .value(CqlIdentifier.fromCql("id"), literal(rating.getId()))
                .value(CqlIdentifier.fromCql("user_id"), literal(rating.getUserId()))
                .value(CqlIdentifier.fromCql("stars"), literal(rating.getStars()))
                .value(CqlIdentifier.fromCql("comment"), literal(rating.getComment()));

        RegularInsert insert2 = QueryBuilder.insertInto(CassandraRepo.TABLE_RATING_BY_USER)
                .value(CqlIdentifier.fromCql("user_id"), literal(rating.getUserId()))
                .value(CqlIdentifier.fromCql("id"), literal(rating.getId()))
                .value(CqlIdentifier.fromCql("book_id"), literal(rating.getBookId()))
                .value(CqlIdentifier.fromCql("stars"), literal(rating.getStars()))
                .value(CqlIdentifier.fromCql("comment"), literal(rating.getComment()));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED).
                addStatement(insert.build()).addStatement(insert2.build())
                .build();

        session.execute(batchStatement);
    }

    private Rating ratingFromRow(Row row) {
        if (row == null){
            return null;
        }


        return new Rating(
                row.getUuid("id"),
                row.getInt("stars"),
                row.getString("comment"),
                row.getUuid("book_id"),
                row.getUuid("user_id")
        );
    }

    public Rating getRating(UUID bookId, UUID userId) {
        String table = Math.random() < 0.5 ? CassandraRepo.TABLE_RATING_BY_USER : CassandraRepo.TABLE_RATING_BY_BOOK;
        Statement statement = QueryBuilder.selectFrom(table)
                .all()
                .where(Relation.column("book_id").isEqualTo(literal(bookId)))
                .where(Relation.column("user_id").isEqualTo(literal(userId)))
                .build();

        Row result_row = session.execute(statement).one();
        return ratingFromRow(result_row);

    }
    public List<Rating> getRatingsByBookId(UUID bookId) {
        SimpleStatement statement = QueryBuilder.selectFrom(CassandraRepo.TABLE_RATING_BY_BOOK)
                .all()
                .where(Relation.column("book_id").isEqualTo(literal(bookId)))
                .build();
        List<Row> resultRows = session.execute(statement).all();
        List<Rating> results = new ArrayList<Rating>();
        resultRows.forEach(row -> results.add(ratingFromRow(row)));
        return results;
    }

    public List<Rating> getRatingsByUserId(UUID userId) {
        SimpleStatement statement = QueryBuilder.selectFrom(CassandraRepo.TABLE_RATING_BY_USER)
                .all()
                .where(Relation.column("user_id").isEqualTo(literal(userId)))
                .build();
        List<Row> resultRows = session.execute(statement).all();
        List<Rating> results = new ArrayList<Rating>();
        resultRows.forEach(row -> results.add(ratingFromRow(row)));
        return results;
    }


    public void deleteRating(UUID bookId, UUID userId) {
        SimpleStatement deleteStatement1 = QueryBuilder.deleteFrom(CassandraRepo.TABLE_RATING_BY_BOOK)
                .where(Relation.column("book_id").isEqualTo(literal(bookId)))
                .where(Relation.column("user_id").isEqualTo(literal(userId)))
                .build();
        SimpleStatement deleteStatement2 = QueryBuilder.deleteFrom(CassandraRepo.TABLE_RATING_BY_USER)
                .where(Relation.column("book_id").isEqualTo(literal(bookId)))
                .where(Relation.column("user_id").isEqualTo(literal(userId)))
                .build();

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED).
                addStatement(deleteStatement1).addStatement(deleteStatement2)
                .build();

        session.execute(batchStatement);
    }
}
