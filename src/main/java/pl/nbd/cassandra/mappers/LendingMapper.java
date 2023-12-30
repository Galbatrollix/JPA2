package pl.nbd.cassandra.mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import pl.nbd.cassandra.dao.BookDao;
import pl.nbd.cassandra.dao.LendingDao;

@Mapper
public interface LendingMapper {
    @DaoFactory
    LendingDao lendingDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    LendingDao lendingDao();
}
