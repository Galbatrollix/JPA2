package pl.nbd.cassandra.query_providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import pl.nbd.cassandra.model.Book;
import pl.nbd.cassandra.model.LibraryUser;
import pl.nbd.cassandra.repositories.CassandraRepo;

public class LibraryUserQueryProvider {

    private final CqlSession session;
    private EntityHelper<LibraryUser> libraryUserHelper;

    public LibraryUserQueryProvider(MapperContext context, EntityHelper<LibraryUser> libraryUserHelper) {
        this.session = context.getSession();
        this.libraryUserHelper = libraryUserHelper;
    }

    public void updateLibraryUser(LibraryUser libraryUser) {
        SimpleStatement deletePreviousUser = QueryBuilder.deleteFrom(CassandraRepo.TABLE_LIBRARY_USERS)
                .whereColumn("id")
                .isEqualTo(QueryBuilder.literal(libraryUser.getId()))
                .build();
        SimpleStatement addUpdatedUser = QueryBuilder.insertInto(CassandraRepo.TABLE_LIBRARY_USERS)
                .value("id", QueryBuilder.literal(libraryUser.getId()))
                .value("username", QueryBuilder.literal(libraryUser.getUsername()))
                .value("email", QueryBuilder.literal(libraryUser.getEmail()))
                .build();
        session.execute(deletePreviousUser);
        session.execute(addUpdatedUser);
    }
}
