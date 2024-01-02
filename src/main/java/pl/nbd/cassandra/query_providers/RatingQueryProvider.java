package pl.nbd.cassandra.query_providers;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.mapper.MapperContext;
import com.datastax.oss.driver.api.mapper.entity.EntityHelper;
import pl.nbd.cassandra.model.Book;
import pl.nbd.cassandra.model.Rating;

public class RatingQueryProvider {

    private final CqlSession session;
    private EntityHelper<Rating> ratingEntityHelper;

    public RatingQueryProvider(MapperContext context, EntityHelper<Rating> ratingEntityHelper) {
        this.session = context.getSession();
        this.ratingEntityHelper = ratingEntityHelper;
    }
    public void addRating(Rating rating){

    }
}
