package pl.nbd.cassandra.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import pl.nbd.cassandra.model.Lending;
import pl.nbd.cassandra.model.Rating;
import pl.nbd.cassandra.query_providers.LendingQueryProvider;
import pl.nbd.cassandra.query_providers.RatingQueryProvider;

import java.util.List;
import java.util.UUID;

@Dao
public interface LendingDao {

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass= LendingQueryProvider.class, entityHelpers = {Lending.class})
    void addLending(Lending lending);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass= LendingQueryProvider.class, entityHelpers = {Lending.class})
    List<Lending> getLendingsByBookId(UUID bookId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass= LendingQueryProvider.class, entityHelpers = {Lending.class})
    List<Lending> getLendingsByUserId(UUID userId);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass= LendingQueryProvider.class, entityHelpers = {Lending.class})
    Lending getLending(UUID bookId, UUID userId);

    @QueryProvider(providerClass= LendingQueryProvider.class, entityHelpers = {Lending.class})
    void deleteLending(UUID bookId, UUID userId);
}
