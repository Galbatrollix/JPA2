package redisRepo;

import com.google.gson.internal.LinkedTreeMap;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import model.Book;
import org.bson.Document;
import org.bson.types.ObjectId;
import redis.clients.jedis.*;

import java.util.Date;
import java.util.regex.Pattern;

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
        // ObjectID get converted to Date in .toJson(), se we have to replace it
        Pattern pattern = Pattern.compile("\"_id\":.+},");
        json = json.replaceAll(pattern.pattern(), "\"_id\":\"" + document.get("_id").toString() + "\",");
        System.out.println(json);
        jedisPool.jsonSet(hash + document.get("_id"), json);
        jedisPool.expire(hash + document.get("_id"), expiration);
    }

    public Document getDocumentFromCashe(String hash, ObjectId id) {
        Object read = jedisPool.jsonGet(hash + id);
        String json = jsonb.toJson(read);
        System.out.println(json);
        Document document = jsonb.fromJson(json, Document.class);
        //TODO this read all Integer as FP - proaobley need to change all inst in document to FP :skull emoji:
        System.out.println(document);
        return document;
    }

    public void addBook(Book book, Integer expiration) {
        String json = jsonb.toJson(book);
        jedisPool.jsonSet(bookHashPrefix + book.getId(), json);
        jedisPool.expire(bookHashPrefix + book.getId(), expiration);

    }
}
