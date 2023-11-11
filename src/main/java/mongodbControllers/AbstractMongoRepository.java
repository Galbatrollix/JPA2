package mongodbControllers;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;

import java.util.List;

public abstract class AbstractMongoRepository implements  AutoCloseable {
    private ConnectionString connsectionString = new ConnectionString("mongodb://localhost:27017");
    private MongoCredential credidential = MongoCredential.createCredential("admin", "admin", "adminpassword".toCharArray());

    private CodecRegistry pojoCodecregistery = CodecRegistries.fromProviders(PojoCodecProvider.builder()
            .automatic(true)
            .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
            .build());
    private MongoClient mongoClient;
    private MongoDatabase bookDB; //TODO proper DB name

    private void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credidential)
                .applyConnectionString(connsectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD)),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecregistery
                ))
                .build();
        mongoClient = MongoClients.create(settings);
        bookDB = mongoClient.getDatabase("bookDB");
    }
}
