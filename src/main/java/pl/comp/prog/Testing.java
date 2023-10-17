package pl.comp.prog;

import controllers.BookController;
import controllers.LibraryUserController;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
import model.Book;
import model.LibraryUser;
import model.Rating;


public class Testing {
    public static void testRatingRemoval(){

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em1 = emf.createEntityManager();
        EntityManager em2 = emf.createEntityManager();

        Book addedBook = BookController.addBookTransaction("TitleTest", "AuthorTest",8);
        LibraryUser addedUser = LibraryUserController.addUserTransaction("emailTest","testname");

        em1.getTransaction().begin();
        Rating rating = new Rating();
        rating.setStars(5);
        rating.setComment("Testcomment");
        LibraryUser relevantUser = em1.find(LibraryUser.class, addedUser.getId());
        Book relevantBook = em1.find(Book.class, addedBook.getId());

        /*book gets deleted mid-transaction*/
        em2.getTransaction().begin();
        Book bookToDelete = em2.find(Book.class, addedBook.getId());
        em2.remove(bookToDelete);
        em2.getTransaction().commit();
        /*book gets deleted mid-transaction*/

        rating.setBook(relevantBook);
        rating.setUser(relevantUser);
        em1.persist(rating);
        try{
            em1.getTransaction().commit();
        }
        catch (RollbackException e){
            System.out.println("Transaction rolled back.");
        }


    }
}
