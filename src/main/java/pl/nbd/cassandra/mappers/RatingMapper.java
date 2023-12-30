package pl.nbd.cassandra.mappers;

import com.datastax.oss.driver.api.mapper.annotations.DaoFactory;
import com.datastax.oss.driver.api.mapper.annotations.DaoKeyspace;
import com.datastax.oss.driver.api.mapper.annotations.DaoTable;
import com.datastax.oss.driver.api.mapper.annotations.Mapper;

import pl.nbd.cassandra.dao.RatingDao;

@Mapper
public interface RatingMapper {
    @DaoFactory
    RatingDao ratingDao(@DaoKeyspace String keyspace, @DaoTable String table);

    @DaoFactory
    RatingDao ratingDao();
}