package repositories;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import jnr.ffi.annotations.In;

import java.net.InetSocketAddress;

public class CassandraRepo {

    private static CqlSession session;

    private final String KEYSPACE_NAME = "books";

    public static CqlSession getSession() {
        return session;
    }

    public void initSession() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandrapassword")
                .withKeyspace(CqlIdentifier.fromCql(KEYSPACE_NAME))
                .build();
    }

    public void initKeyspace() {
        CqlSession sessionWithoutKeyspace = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
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
}
