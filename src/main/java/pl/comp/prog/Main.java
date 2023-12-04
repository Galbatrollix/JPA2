package pl.comp.prog;

import controllers.*;
import model.*;
import org.bson.types.ObjectId;
import redisRepo.RedisRepository;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException {
        AbstractController.attachMongoRepository();
      //  AbstractController.initMongoCollections();

        // to see Redis database, go to localhost:8001


        Book book = BookController.addNewBook(new Book("Book Redis", "BookAuthor", 10));
        LibraryUser user = LibraryUserController.addNewLibraryUser(new LibraryUser("bomba@email.com", "kapitan"));
        //TODO ogarnac book w add catalog
        //Catalog catalog = CatalogController.addNewCatalog(new Catalog("Catalog1", book.getId()));
        BookController.DEBUGPrintAllBooks();

        Book bookTestGet = BookController.getBook(book.getId());
        System.out.println(book.getId());
        System.out.println(bookTestGet.getId());

        // TODO move redis add func to proper Mongo Controllers
        // TODO and decide about when add/modify stuff to redis cashe (and with what expiration)
        AbstractController.closeMongoRepository();

    }
}