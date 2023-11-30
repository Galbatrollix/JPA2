package redisRepo;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import model.Book;
import redis.clients.jedis.*;

import java.util.UUID;

public class RedisRepository {

    private static JedisPooled jedisPool;
    private static Jsonb jsonb = JsonbBuilder.create();

    private static String bookHashPrefix = "book:";
    private static String userHashPrefix = "libraryUser:";
    private static String ratingHashPrefix = "rating:";
    private static String catalogHashPrefix = "catalog:";
    private static String reservationHashPrefix = "reservation:";
    private static String lendingHashPrefix = "lending:";

    public void initConnection() {
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
        jedisPool = new JedisPooled(new HostAndPort("localhost", 6379), clientConfig);

    }

    public void closeConnection() {
        jedisPool.close();
    }

    public void addBook(Book book) {
        String json = jsonb.toJson(book);
        System.out.println(book.getId());
        jedisPool.jsonSet(bookHashPrefix + book.getId(), json);
        //jedisPool.expire("book:" + id, 1);
    }

    public void addBook(Book book, Integer expiration) {
        String json = jsonb.toJson(book);
        jedisPool.jsonSet(bookHashPrefix + book.getId(), json);
        jedisPool.expire(bookHashPrefix + book.getId(), expiration);

    }
}
