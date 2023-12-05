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

    public static void initRedisRepo() {
        AbstractController.redisRepo = new RedisRepository();
        AbstractController.redisRepo.initConnection();
    }

    public static void closeRedis() {
        if (AbstractController.redisRepo != null) {
            AbstractController.redisRepo.closeConnection();
            AbstractController.redisRepo = null;
        }
    }

    public static void addToCashe(Document document, String hash, Integer expiration) {
        if (AbstractController.redisRepo != null) {
            try {
                AbstractController.redisRepo.addDocumentToCashe(document, hash, expiration);
            } catch (JsonProcessingException e) {
                System.out.println("Could not add document " + document + " to cashe" + e);
            }
        }
    }

    public static void addBookWithCatalogToCashe(Document book, Document catalog, Integer expiration) {
        if (AbstractController.redisRepo != null) {
            try {
                AbstractController.redisRepo.addBookWithCatalogToCashe(book, catalog, expiration);
            } catch (JsonProcessingException e) {
                System.out.println("Could not add book with catalog "+ book + " to cashe" + e );
            }
        }
    }

    public static void addCatalogToCashe(Document catalog, ObjectId bookId, Integer expiration) {
        if (AbstractController.redisRepo != null) {
            try {
                AbstractController.redisRepo.addCatalogToCashe(catalog, bookId, expiration);
            } catch (JsonProcessingException e) {
                System.out.println("Could not add catalog " + catalog + " to cashe" + e);
            }
        }
    }

    public static void addRatingToCashe(Document rating, ObjectId userId, Integer expiration) {
        if (AbstractController.redisRepo != null) {
            try {
                AbstractController.redisRepo.addRatingToCashe(rating, userId, expiration);
            } catch (JsonProcessingException e) {
                System.out.println("Could not add rating " + rating + " to cashe" + e);
            }
        }
    }

    public static void addUserWithRatingsToCashe(Document user, ArrayList<Document> ratings, Integer expiration) {
        if (AbstractController.redisRepo != null) {
            try {
                AbstractController.redisRepo.addUserWithRatingsToCashe(user, ratings, expiration);
            } catch (JsonProcessingException e) {
                System.out.println("Could not add user with ratings " + user + " to cashe" + e);
            }
        }
    }

    public static Document getFromCashe(String hash, ObjectId id) {
        if (AbstractController.redisRepo == null) {
            return null;
        }
        return AbstractController.redisRepo.getDocumentFromCashe(hash, id);
    }

    public static void clearCashe() {
        if (AbstractController.redisRepo != null) {
            AbstractController.redisRepo.clear();
        }
    }





}

