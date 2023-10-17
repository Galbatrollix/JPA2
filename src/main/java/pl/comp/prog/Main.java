package pl.comp.prog;

import controllers.*;
import model.*;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {
        Book book1 = BookController.addBookTransaction("Pan Tadeusz", "A.Mickiewicz", 2);
        Book bookToDelete = BookController.addBookTransaction("This should be deleted", "Test", 2);
        Book book3 = BookController.addBookTransaction("book 3", "h", 6);
        Book book4 = BookController.addBookTransaction("book 4", "h2", 4);
        BookController.deleteBookTransaction(bookToDelete);

        LibraryUser user1 = LibraryUserController.addUserTransaction("bomba@email.com", "kapitan");
        LibraryUser user2 = LibraryUserController.addUserTransaction("abcd@email.com", "iwillbedeleted");
        LibraryUser user3 = LibraryUserController.addUserTransaction("efgh@email.com", "gamer");
        LibraryUser user4 = LibraryUserController.addUserTransaction("aaaaaa@email.com", "gamer2");
        LibraryUser user5 = LibraryUserController.addUserTransaction("hhhh@gmanil.pl", "gamer3");
        LibraryUserController.deleteUserTransaction(user2);

        Rating rating1 = RatingController.addRatingTransaction(5, "Comment", 1, 1);
        Rating rating2 = RatingController.addRatingTransaction(4, "comment2", 1, 1);
        Rating badrating1 = RatingController.addRatingTransaction(5, "Comment", 99, 99);
        RatingController.deleteRatingByIdTransaction(rating1.getId());

        List<Long> bookIds = new ArrayList<Long>() {{
            add(1L);
            add(2L);
            add(3L);
        }};
        CatalogController.addCatalogTransaction("Catalog1", bookIds);

        //Testing.testRatingRemoval();
        Reservation rv1 = BookEventQueueController.addReservationTransaction(user1.getId(), book1.getId());
        Reservation rv2 = BookEventQueueController.addReservationTransaction(user3.getId(), book1.getId());
        Reservation rv3_ = BookEventQueueController.addReservationTransaction(user3.getId(), book1.getId());
        Reservation rv4 = BookEventQueueController.addReservationTransaction(user4.getId(), book1.getId());
        Reservation rv5 = BookEventQueueController.addReservationTransaction(user5.getId(), book1.getId());
        BookEventQueueController.deleteEventByIdTransaction(rv2.getId());
        BookEventQueueController.addLendingTransaction(user4.getId(), book1.getId());

        BookEventQueueController.addLendingTransaction(user4.getId(), book3.getId());
        BookEventQueueController.addLendingTransaction(user4.getId(), book3.getId());
    }
}