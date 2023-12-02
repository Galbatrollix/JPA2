package redisRepo;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import model.Book;
import org.bson.Document;
import org.bson.types.ObjectId;
import redis.clients.jedis.*;

public class RedisRepository {

    private static JedisPooled jedisPool;
    private static Jsonb jsonb = JsonbBuilder.create();

    public static String bookHashPrefix = "book:";
    public static String userHashPrefix = "libraryUser:";
    public static String ratingHashPrefix = "rating:";
    public static String catalogHashPrefix = "catalog:";

    public void initConnection() {
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
        jedisPool = new JedisPooled(new HostAndPort("localhost", 6379), clientConfig);
        jedisPool.flushDB();

    }

    public void closeConnection() {
        jedisPool.close();
    }

    public void addDocumentToCashe(Document document, String hash) {
        String json = jsonb.toJson(document);
        jedisPool.jsonSet(hash + document.get("_id"), json);
        //jedisPool.expire("book:" + id, 1);
    }

    public void addDocumentToCashe(Document document, String hash, Integer expiration) {

        String json = jsonb.toJson(document);
        jedisPool.jsonSet(hash + document.get("_id"), json);
        jedisPool.expire(hash + document.get("_id"), expiration);
    }

    public Document getDocumentFromCashe(String hash, ObjectId id) {
        String jsonClient = jedisPool.get(hash + id);
        Document document = jsonb.fromJson(jsonClient, Document.class); //Error?
        return document;
    }

    public void addBook(Book book, Integer expiration) {
        String json = jsonb.toJson(book);
        jedisPool.jsonSet(bookHashPrefix + book.getId(), json);
        jedisPool.expire(bookHashPrefix + book.getId(), expiration);

    }
}
