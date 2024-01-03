package pl.nbd.cassandra.query_providers;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import com.datastax.oss.driver.api.querybuilder.relation.Relation;
import pl.nbd.cassandra.model.Lending;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.currentTimestamp;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;

public class LendingQueryProvider {
    private final CqlSession session;
    private EntityHelper<Lending> ratingEntityHelper;



    public LendingQueryProvider(MapperContext context, EntityHelper<Lending> ratingEntityHelper) {
        this.session = context.getSession();
        this.ratingEntityHelper = ratingEntityHelper;
    }

    private String parseDate(Date date) {
       SimpleDateFormat x = new SimpleDateFormat("yyyy-MM-dd");
       return x.format(date);
    }


    public void addLending(Lending lending){


        RegularInsert insert = QueryBuilder.insertInto(CassandraRepo.TABLE_LENDING_BY_BOOK)
                .value(CqlIdentifier.fromCql("book_id"), literal(lending.getBookId()))
                .value(CqlIdentifier.fromCql("user_id"), literal(lending.getUserId()))
                .value(CqlIdentifier.fromCql("id"), literal(lending.getId()))
                .value(CqlIdentifier.fromCql("begin_date"), literal(parseDate(lending.getBeginDate())))
                .value(CqlIdentifier.fromCql("expected_end_date"), literal(parseDate(lending.getExpectedEndDate())))
                .value(CqlIdentifier.fromCql("close_date"), literal(parseDate(lending.getCloseDate())));


        RegularInsert insert2 = QueryBuilder.insertInto(CassandraRepo.TABLE_LENDING_BY_USER)
                .value(CqlIdentifier.fromCql("user_id"), literal(lending.getUserId()))
                .value(CqlIdentifier.fromCql("book_id"), literal(lending.getBookId()))
                .value(CqlIdentifier.fromCql("id"), literal(lending.getId()))
                .value(CqlIdentifier.fromCql("begin_date"), literal(parseDate(lending.getBeginDate())))
                .value(CqlIdentifier.fromCql("expected_end_date"), literal(parseDate(lending.getExpectedEndDate())))
                .value(CqlIdentifier.fromCql("close_date"), literal(parseDate(lending.getCloseDate())));

        BatchStatement batchStatement = BatchStatement.builder(BatchType.LOGGED).
                addStatement(insert.build()).addStatement(insert2.build())
                .build();

        session.execute(batchStatement);
    }

    private Lending ratingFromRow(Row row) {
        if (row == null){
            return null;
        }

        return new Lending(
                row.getUuid("id"),
                java.util.Date.from(row.getLocalDate("begin_date").atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()),

                java.util.Date.from(row.getLocalDate("close_date").atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()),
                java.util.Date.from(row.getLocalDate("expected_end_date").atStartOfDay()
                        .atZone(ZoneId.systemDefault())
                        .toInstant()),

                row.getUuid("book_id"),
                row.getUuid("user_id")
        );
    }

    public Lending getLending(UUID bookId, UUID userId) {
        System.out.println(bookId);
        System.out.println(userId);
        //String table = Math.random() < 0.5 ? CassandraRepo.TABLE_LENDING_BY_USER : CassandraRepo.TABLE_LENDING_BY_BOOK;
        SimpleStatement statement = QueryBuilder.selectFrom(CassandraRepo.TABLE_LENDING_BY_USER)
                .all()
                .where(Relation.column("user_id").isEqualTo(literal(userId)))
                .where(Relation.column("book_id").isEqualTo(literal(bookId)))
                .build();
        System.out.println(statement.getQuery());

        Row result_row = session.execute(statement).one();
        System.out.println(result_row);
        return ratingFromRow(result_row);
    }
    public List<Lending> getLendingsByBookId(UUID bookId) {
        SimpleStatement statement = QueryBuilder.selectFrom(CassandraRepo.TABLE_RATING_BY_BOOK)
                .all()
                .where(Relation.column("book_id").isEqualTo(literal(bookId)))
                .build();
        List<Row> resultRows = session.execute(statement).all();
        List<Lending> results = new ArrayList<Lending>();
        resultRows.forEach(row -> results.add(ratingFromRow(row)));
        return results;
    }

    public List<Lending> getLendingsByUserId(UUID userId) {
        SimpleStatement statement = QueryBuilder.selectFrom(CassandraRepo.TABLE_RATING_BY_USER)
                .all()
                .where(Relation.column("user_id").isEqualTo(literal(userId)))
                .build();
        List<Row> resultRows = session.execute(statement).all();
        List<Lending> results = new ArrayList<Lending>();
        resultRows.forEach(row -> results.add(ratingFromRow(row)));
        return results;
    }


    public void deleteLending(UUID bookId, UUID userId) {
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
