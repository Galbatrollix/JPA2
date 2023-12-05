package controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import mongodbRepo.MongoRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import redisRepo.RedisRepository;

import java.util.ArrayList;


public abstract class AbstractController {
    protected static MongoRepository mongoRepo;
    protected static RedisRepository redisRepo = null;


    public static int EXPIRATION = 60;
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

    public static void addToCashe(Document document, String hash, Integer expiration) {
        if (AbstractController.redisRepo == null) {
            AbstractController.initRedisRepo();
        }
        try {
            AbstractController.redisRepo.addDocumentToCashe(document, hash, expiration);
        } catch (JsonProcessingException e) {
            System.out.println("Could not add document "+ document + " to cashe" + e );
        }
    }

    public static void addBookWithCatalogToCashe(Document book, Document catalog, Integer expiration) {
        if (AbstractController.redisRepo == null) {
            AbstractController.initRedisRepo();
        }
        try {
            AbstractController.redisRepo.addBookWithCatalogToCashe(book, catalog, expiration);
        } catch (JsonProcessingException e) {
            System.out.println("Could not add book with catalog "+ book + " to cashe" + e );
        }
    }

    public static void addCatalogToCashe(Document catalog, ObjectId bookId, Integer expiration) {
        if (AbstractController.redisRepo == null) {
            AbstractController.initRedisRepo();
        }
        try {
            AbstractController.redisRepo.addCatalogToCashe(catalog, bookId, expiration);
        } catch (JsonProcessingException e) {
            System.out.println("Could not add catalog "+ catalog + " to cashe" + e );
        }
    }

    public static void addRatingToCashe(Document rating, ObjectId userId, Integer expiration) {
        if (AbstractController.redisRepo == null) {
            AbstractController.initRedisRepo();
        }
        try {
            AbstractController.redisRepo.addRatingToCashe(rating, userId, expiration);
        } catch (JsonProcessingException e) {
            System.out.println("Could not add rating "+ rating + " to cashe" + e );
        }
    }

    public static void addUserWithRatingsToCashe(Document user, ArrayList<Document> ratings, Integer expiration) {
        if (AbstractController.redisRepo == null) {
            AbstractController.initRedisRepo();
        }
        try {
            AbstractController.redisRepo.addUserWithRatingsToCashe(user, ratings, expiration);
        } catch (JsonProcessingException e) {
            System.out.println("Could not add user with ratings "+ user + " to cashe" + e );
        }
    }

    public static Document getFromCashe(String hash, ObjectId id) {
        return AbstractController.redisRepo.getDocumentFromCashe(hash, id);
    }

    public static void clearCashe() {
        AbstractController.redisRepo.clear();
    }





}

