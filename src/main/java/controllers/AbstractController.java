package controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import mongodbRepo.MongoRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import redisRepo.RedisRepository;


public abstract class AbstractController {
    protected static MongoRepository mongoRepo;
    protected static RedisRepository redisRepo = null;


    public static void attachMongoRepository(){
        MongoRepository mongoRepo = new MongoRepository();
        mongoRepo.initDbConnection("bookDB");

        AbstractController.mongoRepo = mongoRepo;
    }

    public static  void initMongoCollections(){
        mongoRepo.createCollections();
    }

    public static void closeMongoRepository(){
        AbstractController.mongoRepo.close();
        if (AbstractController.redisRepo == null) {
            AbstractController.closeRedis();
        }

    }

    private static void initRedisRepo() {
        AbstractController.redisRepo = new RedisRepository();
        AbstractController.redisRepo.initConnection();
    }

    private static void closeRedis() {
        AbstractController.redisRepo.closeConnection();
    }

    public static void addToCash(Document document, String hash, Integer expiration) {
        if (AbstractController.redisRepo == null) {
            AbstractController.initRedisRepo();
        }
        try {
            AbstractController.redisRepo.addDocumentToCashe(document, hash, expiration);
        } catch (JsonProcessingException e) {
            System.out.println("Could not add document "+ document + " to cashe" + e );
        }
    }



}

