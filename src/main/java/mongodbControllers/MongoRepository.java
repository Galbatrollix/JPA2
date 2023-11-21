package mongodbControllers;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import com.mongodb.client.model.CreateCollectionOptions;
import com.mongodb.client.model.ValidationOptions;
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

    public void createCollections() {
        createBookCollection();
        createCatalogCollection();
        // todo remove later
        createTestCollection();
        createUserCollection();

    }

    private void createCatalogCollection() {
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse("""
                        {
                            $jsonSchema:{
                                "bsonType": "object",
                                "required": [ "name", "book_ids" ],
                                "properties": {
                                   "name" : {
                                       "bsonType": "string"
                                   },
                                   "book_ids": {
                                       "bsonType": "array",
                                       "uniqueItems": true,
                                       items: {
                                            "bsonType": "objectId"
                                       
                                       }
                                   
                                   }
                                }
                            }
                        }"""));
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        bookDB.createCollection("catalogs", createCollectionOptions);
    }

    private void createUserCollection() {
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse("""
                        {
                            $jsonSchema:{
                                "bsonType": "object",
                                "required": [ "username", "email", "ratings" ],
                                "properties": {
                                   "username" : {
                                       "bsonType": "string"
                                   },
                                   "email" : {
                                       "bsonType": "string"
                                   },
                                   "ratings": {
                                       "bsonType": "array",
                                       "uniqueItems": true,
                                       "items": {
                                            "bsonType": "object",
                                            "required": [ "stars", "comment", "book_id" ],
                                            "properties": {
                                                "stars" : {
                                                    "bsonType": "int"
                                                },
                                                "comment": {
                                                    "bsonType": "string"
                                                },
                                                "book_id":{
                                                    "bsonType": "objectId"
                                                }
                                            }
                                            
                                        
                                       }
                                   
                                   }
                                }
                            }
                        }"""));

        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);

        bookDB.createCollection("users", createCollectionOptions);
    }

    private void createBookCollection() {
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse("""
                        {
                            $jsonSchema:{
                                "bsonType": "object",
                                "required": [ "title", "author", "reservation_queue", "events_active", "events_closed", "is_available_slots", "quantity"],
                                "properties": {
                                   "title" : {
                                       "bsonType": "string"
                                   },
                                   "author" : {
                                       "bsonType": "string"
                                   },
                                   "reservation_queue":{
                                       "bsonType": "object",
                                       "required": ["begin_date", "expected_end_date"],
                                       "properties": {
                                            "begin_date": {
                                                "bsonType": "date"
                                            },
                                            "expected_end_date": {
                                                "bsonType": "date"
                                            }
                                       }
                                   },
                                   "events_active":{
                                       "bsonType": "object",
                                       "required": ["begin_date", "expected_end_date", "_clazz"],
                                       "properties": {
                                            "begin_date": {
                                                "bsonType": "date"
                                            },
                                            "expected_end_date": {
                                                "bsonType": "date"
                                            },
                                            "_clazz": {
                                                "bsonType": "string",
                                                "enum": ["lending", "reservation"]
                                            }
                                       }
                                   },
                                   
                                   "events_closed":{
                                       "bsonType": "object",
                                       "required": ["begin_date", "expected_end_date", "_clazz"],
                                       "properties": {
                                            "begin_date": {
                                                "bsonType": "date"
                                            },
                                            "expected_end_date": {
                                                "bsonType": "date"
                                            },
                                            "_clazz": {
                                                "bsonType": "string",
                                                "enum": ["lending", "reservation"]
                                            }
                                       }
                                   },
                                   
                                   "is_available_slots":{
                                        "bsonType": "array",
                                        "items": {
                                            "bsonType": "int",
                                            "maximum": 1,
                                            "minimum": 0
                                        }
                                   },
                                   
                                   "quantity":{
                                      "bsonType": "int",
                                            "minimum": 1
                                  }
                                }
                                
                           }
                        }"""));

        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);
        bookDB.createCollection("books", createCollectionOptions);

    }

    private void createTestCollection() {
        ValidationOptions validationOptions = new ValidationOptions().validator(
                Document.parse("""
                        {
                            $jsonSchema:{
                                "bsonType": "object",
                                "required": [ "test_string", "test_int", "smaller_int" ],
                                "properties": {
                                   "test_string" : {
                                       "bsonType": "string"
                                   },
                                   "test_int": {
                                       "bsonType": "int"
                                   },
                                   "smaller_int": {
                                       "bsonType": "int"
                                   }
                                }
                            }
                        }"""));
        CreateCollectionOptions createCollectionOptions = new CreateCollectionOptions()
                .validationOptions(validationOptions);


        bookDB.createCollection("test", createCollectionOptions);
    }

    public MongoCollection<Document> getTestCollection() {
        return bookDB.getCollection("test", Document.class);

    }

    public MongoCollection<Document> getBookCollection() {
        return bookDB.getCollection("books", Document.class);

    }

    public MongoCollection<Document> getCatalogCollection() {
        return bookDB.getCollection("catalogs", Document.class);

    }

    public MongoCollection<Document> getUserCollection() {
        return bookDB.getCollection("users", Document.class);
    }

    public void close() {
        mongoClient.close();
    }

}
