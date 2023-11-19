package mongodbControllers;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import model.Book;
import model.MGTestModel;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MongoRepository {
    private ConnectionString connsectionString = new ConnectionString("mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=replica_set_single");
    private MongoCredential credidential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

    private CodecRegistry pojoCodecregistery = CodecRegistries.fromProviders(PojoCodecProvider.builder()
            .automatic(true)
            .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
            .build());
    private MongoClient mongoClient;
    private MongoDatabase bookDB; //TODO proper DB name

    public void initDbConnection(String dbName) {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credidential)
                .applyConnectionString(connsectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD)),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecregistery
                ))
                .applyToClusterSettings(builder ->
                        builder.serverSelectionTimeout(10, TimeUnit.SECONDS))
                .build();
        mongoClient = MongoClients.create(settings);
        bookDB = this.mongoClient.getDatabase(dbName);

        mongoClient.listDatabaseNames().forEach(System.out::println);
        System.out.println(bookDB);
    }

    public void createBookCollection() {
        bookDB.createCollection("books");
    }

    public void createTestCollection(){
        bookDB.createCollection("test");
    }
    public MongoCollection<Document> getTestCollection(){
        return bookDB.getCollection("test", Document.class);

    }

    public void addBook(Book book) {
        MongoCollection<Book> bookCollection = bookDB.getCollection("books", Book.class);
        bookCollection.insertOne(book);
    }

    public void close() {
        mongoClient.close();
    }

}
