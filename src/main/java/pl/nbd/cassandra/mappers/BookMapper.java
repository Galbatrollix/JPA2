package pl.nbd.cassandra.mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;
import pl.nbd.cassandra.dao.BookDao;

@Mapper
public interface BookMapper {
    @DaoFactory
    BookDao bookDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    BookDao bookDao();
}
