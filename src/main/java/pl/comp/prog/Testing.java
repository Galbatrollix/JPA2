package pl.comp.prog;

import controllers.BookController;
import controllers.LibraryUserController;
import jakarta.persistence.*;
import model.Book;
import model.BookData;
import model.LibraryUser;
import model.Rating;
import org.hibernate.annotations.OptimisticLock;


public class Testing {

//    public static void testBookWriteskew(){
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
//        EntityManager em1 = emf.createEntityManager();
//        EntityManager em2 = emf.createEntityManager();
//        Book addedBook = BookController.addBookTransaction("TitleTest2", "AuthorTest2",15);
//        Book newBook1 = new Book();
//        newBook1.setData(new BookData());
//        newBook1.getData().setTitle("NEWBOOK1");
//        Book newBook2 = new Book();
//        newBook2.setData(new BookData());
//        newBook2.getData().setTitle("NEWBOOK2");
//
//
//
//        em1.getTransaction().begin();
//        Book book1 = em1.find(Book.class, addedBook.getId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//
//        em2.getTransaction().begin();
//        Book book2 = em2.find(Book.class, addedBook.getId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//        em1.persist(newBook1);
////        book1.getData().setTitle("FIRST TITLE");
////        em1.merge(book1);
//
//        em1.getTransaction().commit();
//        em2.persist(newBook2);
//
////        book2.getData().setAuthor("SECOND AUTHOR");
////        em2.merge(book2);
//
//        em2.getTransaction().commit();
//
//
//        em1.close();
//        em2.close();
//    }
//
//    public static void testRatingRemoval(){
//
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
//        EntityManager em1 = emf.createEntityManager();
//        EntityManager em2 = emf.createEntityManager();
//
//        Book addedBook = BookController.addBookTransaction("TitleTest", "AuthorTest",8);
//        LibraryUser addedUser = LibraryUserController.addUserTransaction("emailTest","testname");
//
//        em1.getTransaction().begin();
//        Rating rating = new Rating();
//        rating.setStars(5);
//        rating.setComment("Testcomment");
//        LibraryUser relevantUser = em1.find(LibraryUser.class, addedUser.getId());
//        Book relevantBook = em1.find(Book.class, addedBook.getId());
//
//        /*book gets deleted mid-transaction*/
//        em2.getTransaction().begin();
//        Book bookToDelete = em2.find(Book.class, addedBook.getId());
//        em2.remove(bookToDelete);
//        em2.getTransaction().commit();
//        /*book gets deleted mid-transaction*/
//
//        rating.setBook(relevantBook);
//        rating.setUser(relevantUser);
//        em1.persist(rating);
//        try{
//            em1.getTransaction().commit();
//        }
//        catch (RollbackException e){
//            System.out.println("Transaction rolled back.");
//        }
//        em1.close();
//        em2.close();
//
//    }
}
