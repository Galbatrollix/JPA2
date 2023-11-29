package redisRepo;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import model.Book;
import redis.clients.jedis.*;

import java.util.UUID;

public class RedisRepository {

    private static JedisPooled jedisPool;
    private static Jsonb jsonb = JsonbBuilder.create();

    public void initConnection() {
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
        jedisPool = new JedisPooled(new HostAndPort("localhost", 6379), clientConfig);

    }

    public void closeConnection() {
        jedisPool.close();
    }

    public void addBookTes() {
        Book book = new Book("Title", "Author", 10);
        UUID id = UUID.randomUUID();

        String json = jsonb.toJson(book);



        jedisPool.jsonSet("book:" + id, json);
        //jedisPool.expire("book:" + id, 1);
        System.out.println("added book");
    }
}
