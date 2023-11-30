package pl.comp.prog;

import controllers.*;
import model.*;
import org.bson.types.ObjectId;
import redisRepo.RedisRepository;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        AbstractController.attachMongoRepository();
        //AbstractController.initMongoCollections();

        // to see Redis database, go to localhost:8001
        RedisRepository redisRepo = new RedisRepository();
        redisRepo.initConnection();


        Book book = BookController.addNewBook(new Book("Book Mongo", "BookAuthor", 10));

        // TODO move redis add func to proper Mongo Controllers
        // TODO and decide about when add/modify stuff to redis cashe (and with what expiration)
        redisRepo.addBook(book);
        redisRepo.closeConnection();
        AbstractController.closeMongoRepository();

    }
}