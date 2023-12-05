package redisRepo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.bson.Document;
import org.bson.types.ObjectId;
import redis.clients.jedis.*;

import javax.print.Doc;
import java.util.ArrayList;
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
        clear();

    }

    public void clear() {
        jedisPool.flushDB();
    }

    public void closeConnection() {
        jedisPool.close();
    }


    private Document getDocumentCopyWithStringID(Document document) {
        String idString = document.get("_id").toString();
        Document docCopy = new Document(document);
        docCopy.put("_id", idString);
        return docCopy;
    }

    public void addDocumentToCashe(Document document, String hash, Integer expiration) throws JsonProcessingException {
        Document docCopy = getDocumentCopyWithStringID(document);

        String json = objectMapper.writeValueAsString(docCopy);
        jedisPool.jsonSet(hash + document.get("_id"), json);
        jedisPool.expire(hash + document.get("_id"), expiration);
    }

    public void addCatalogToCashe(Document catalog, ObjectId bookId, Integer expiration) throws JsonProcessingException {
        Document docCopy = getDocumentCopyWithStringID(catalog);
        docCopy.put("bookId", bookId.toString());

        String json = objectMapper.writeValueAsString(docCopy);
        jedisPool.jsonSet(catalogHashPrefix + catalog.get("_id"), json);
        jedisPool.expire(catalogHashPrefix + catalog.get("_id"), expiration);
    }


    public void addRatingToCashe(Document rating, ObjectId userId, Integer expiration) throws JsonProcessingException {
        Document docCopy = getDocumentCopyWithStringID(rating);
        docCopy.put("userId", userId.toString());
        docCopy.put("book_id", rating.get("book_id").toString());

        String json = objectMapper.writeValueAsString(docCopy);
        jedisPool.jsonSet(ratingHashPrefix + rating.get("_id"), json);
        jedisPool.expire(ratingHashPrefix + rating.get("_id"), expiration);
    }

    public void addBookWithCatalogToCashe(Document book, Document catalog, Integer expiration) throws JsonProcessingException {
        Document docCopyCatalog = getDocumentCopyWithStringID(catalog);
        Document docCopyBook = getDocumentCopyWithStringID(book);
        docCopyBook.put("catalog", docCopyCatalog);

        String json = objectMapper.writeValueAsString(docCopyBook);
        jedisPool.jsonSet(bookHashPrefix + book.get("_id"), json);
        jedisPool.expire(bookHashPrefix + book.get("_id"), expiration);
    }

    public void addUserWithRatingsToCashe(Document user, ArrayList<Document> ratings, Integer expiration) throws JsonProcessingException {
        ArrayList<Document> ratingsCopy = new ArrayList<>();
        for(Document r : ratings) {
            Document docCopyRating = getDocumentCopyWithStringID(r);
            docCopyRating.put("book_id", r.get("book_id").toString());
            ratingsCopy.add(docCopyRating);
        }
        Document docCopyUser = getDocumentCopyWithStringID(user);
        docCopyUser.put("ratings", ratingsCopy);
        String json = objectMapper.writeValueAsString(docCopyUser);
        jedisPool.jsonSet(userHashPrefix + user.get("_id"), json);
        jedisPool.expire(userHashPrefix + user.get("_id"), expiration);
    }

    public Document getDocumentFromCashe(String hash, ObjectId id) {
        Object read = jedisPool.jsonGet(hash + id);
        String json = jsonb.toJson(read);
        System.out.println(json);
        Document document = null;

        try {
            document = objectMapper.readValue(json, Document.class);
        } catch (JsonProcessingException e) {
            System.out.println("Could not get document from cashe");
        }
        return document;
    }



}
