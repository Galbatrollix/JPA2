package redisRepo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private static ObjectMapper objectMapper = new ObjectMapper();

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


    public void addDocumentToCashe(Document document, String hash, Integer expiration) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(document);
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
        Document document = null;
        try {
            document = objectMapper.readValue(json, Document.class);
        } catch (JsonProcessingException e) {
            System.out.println("Could not get document from cashe");
        }
        return document;
    }

    public void addBook(Book book, Integer expiration) {
        String json = jsonb.toJson(book);
        jedisPool.jsonSet(bookHashPrefix + book.getId(), json);
        jedisPool.expire(bookHashPrefix + book.getId(), expiration);

    }
}
