package pl.comp.prog;

import controllers.BookController;
import controllers.CatalogController;
import controllers.LibraryUserController;
import controllers.RatingController;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Book book1 = BookController.addBookTransaction("Pan Tadeusz", "A.Mickiewicz", 10);
        Book book2 = BookController.addBookTransaction("This should be deleted", "Test", 2);
        Book book3 = BookController.addBookTransaction("book 3", "h", 6);
        Book book4 = BookController.addBookTransaction("book 4", "h2", 4);
        BookController.deleteBookTransaction(book2);

        LibraryUser user1 = LibraryUserController.addUserTransaction("bomba@email.com", "kapitan");
        LibraryUser user2 = LibraryUserController.addUserTransaction("abcd@email.com", "iwillbedeleted");
        LibraryUserController.deleteUserTransaction(user2);

        Rating rating1 = RatingController.addRatingTransaction(5,"Comment",1,1);
        Rating rating2 = RatingController.addRatingTransaction(4,"comment2",1,1);
        Rating badrating1 = RatingController.addRatingTransaction(5,"Comment",99,99);
        RatingController.deleteRatingByIdTransaction(rating1.getId());

        List<Long> bookIds = new ArrayList<Long>() {{
            add(1L);
            add(2L);
            add(3L);
        }};
        CatalogController.addCatalogTransaction("Catalog1", bookIds);

        Testing.testRatingRemoval();

    }
}