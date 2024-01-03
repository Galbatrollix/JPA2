package pl.nbd.cassandra.query_providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.PagingIterable;
import com.datastax.oss.driver.api.core.cql.PreparedStatement;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.cql.Statement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.Update;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import pl.nbd.cassandra.model.Book;
import pl.nbd.cassandra.repositories.CassandraRepo;

import java.util.UUID;

public class BookQueryProvider {

    private final CqlSession session;
    private EntityHelper<Book> bookHelper;

    public BookQueryProvider(MapperContext context, EntityHelper<Book> bookHelper) {
        this.session = context.getSession();
        this.bookHelper = bookHelper;
    }

    public void updateBook(Book book) {
        SimpleStatement deletePreviousBook = QueryBuilder.deleteFrom(CassandraRepo.TABLE_BOOKS)
                .whereColumn("id")
                .isEqualTo(QueryBuilder.literal(book.getId()))
                .build();
        SimpleStatement addUpdatedBook = QueryBuilder.insertInto(CassandraRepo.TABLE_BOOKS)
                .value("id", QueryBuilder.literal(book.getId()))
                .value("author", QueryBuilder.literal(book.getAuthor()))
                .value("title", QueryBuilder.literal(book.getTitle()))
                .build();

//        //Alternative:
//        RegularInsert addUpdatedBook = QueryBuilder.insertInto(CassandraRepo.TABLE_BOOKS)
//                .value("id", QueryBuilder.literal(book.getId()))
//                .value("author", QueryBuilder.literal(book.getAuthor()))
//                .value("title", QueryBuilder.literal(book.getTitle()))
//                .value("quantity", QueryBuilder.literal(book.getQuantity()));
//        //i potem exectute/batch itp musialyby dodac .build
//        session.execute(addUpdatedBook.build());

        session.execute(deletePreviousBook);
        session.execute(addUpdatedBook);
    }
}
