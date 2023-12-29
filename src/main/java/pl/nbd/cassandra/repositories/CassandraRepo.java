package pl.nbd.cassandra.repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.protocol.internal.ProtocolConstants;

import java.net.InetSocketAddress;

public class CassandraRepo {

    private CqlSession session;

    public static final String KEYSPACE_NAME = "library_books_system";

    public static final String TABLE_BOOKS = "books";
    public static final String TABLE_LIBRARY_USERS = "libraryUsers";

    public CqlSession getSession() {
        return session;
    }

    public void initSession() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                //in case of problems with docker running more 2 nodes (memory issues), comment line below and run only 1 node
               // .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandrapassword")
                .withKeyspace(CqlIdentifier.fromCql(KEYSPACE_NAME))
                .build();
    }

    public void initKeyspace() {
        CqlSession sessionWithoutKeyspace = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                //in case of problems with docker running more 2 nodes (memory issues), comment line below and run only 1 node
                //.addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandrapassword")
                .build();

        sessionWithoutKeyspace.execute(SchemaBuilder.createKeyspace(CqlIdentifier.fromCql(KEYSPACE_NAME))
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true)
                .build());
        sessionWithoutKeyspace.close();
    }

    public void addBooksTable() {
        SimpleStatement createBooksTable =
                SchemaBuilder.createTable(CqlIdentifier.fromCql(CassandraRepo.TABLE_BOOKS))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("author"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("title"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("quantity"), DataTypes.INT)
                        .withClusteringOrder(CqlIdentifier.fromCql("author"), ClusteringOrder.ASC)
                        .build();
        session.execute(createBooksTable);
        System.out.println("Added books table");
    }

    public void addLibraryUsersTable() {
        SimpleStatement createLibraryUsersTable =
                SchemaBuilder.createTable(CqlIdentifier.fromCql(CassandraRepo.TABLE_LIBRARY_USERS))
                        .ifNotExists()
                        .withPartitionKey(CqlIdentifier.fromCql("id"), DataTypes.UUID)
                        .withClusteringColumn(CqlIdentifier.fromCql("username"), DataTypes.TEXT)
                        .withColumn(CqlIdentifier.fromCql("email"), DataTypes.TEXT)
                        .withClusteringOrder(CqlIdentifier.fromCql("username"), ClusteringOrder.ASC)
                        .build();
        session.execute(createLibraryUsersTable);
        System.out.println("Added library users table");
    }

    public void closeSession() {
        session.close();
    }
}
