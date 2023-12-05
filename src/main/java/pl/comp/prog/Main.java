package pl.comp.prog;

import controllers.*;
import model.*;
import org.bson.types.ObjectId;
import redisRepo.RedisRepository;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        AbstractController.attachMongoRepository();
        AbstractController.initRedisRepo();
        // to see Redis database, go to localhost:8001

        Book book = BookController.addNewBook(new Book("Explosions", "Totlay Sane Person", 10));
        Book book2 = BookController.addNewBook(new Book("Java", "Java fanatic", 2));
        LibraryUser user = LibraryUserController.addNewLibraryUser(new LibraryUser("Pan Kapitan", "bomba@email.com"));
        Catalog catalog = CatalogController.addNewCatalog(new Catalog("Catalog1", book.getId()));


        Rating rating = RatingController.addNewRating(new Rating(5, "So cool", book.getId(), user.getId()));
        Rating rating2 = RatingController.addNewRating(new Rating(2, "Eh out of 10", book2.getId(), user.getId()));

        BookController.deleteBook(book2.getId());


        Book bookTestGet = BookController.getBook(book.getId());
        System.out.println(book.getId());
        System.out.println(bookTestGet.getId());
        LibraryUserController.DEBUGPrintAllUsers();

        AbstractController.closeMongoRepository();
        AbstractController.closeRedis();

    }
}