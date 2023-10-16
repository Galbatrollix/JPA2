package pl.comp.prog;

import controllers.BookController;
import controllers.LibraryUserController;
import controllers.RatingController;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Book;
import model.LibraryUser;
import model.Rating;
import model.Student;

public class Main {

    static void studentTestTransaction() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        Student student = new Student();
        student.setName("Kowalski");
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
    }
    public static void main(String[] args) throws ClassNotFoundException {
        Book book1 = BookController.addBookTransaction("Pan Tadeusz", "A.Mickiewicz", 10);
        Book book2 = BookController.addBookTransaction("This should be deleted", "Test", 2);
        BookController.deleteBookTransaction(book2);

        LibraryUser user1 = LibraryUserController.addUserTransaction("bomba@email.com", "kapitan");
        LibraryUser user2 = LibraryUserController.addUserTransaction("abcd@email.com", "iwillbedeleted");
        LibraryUserController.deleteUserTransaction(user2);

        Rating rating1 = RatingController.addRatingTransaction(5,"Comment",1,1);
        Rating rating2 = RatingController.addRatingTransaction(4,"comment2",1,1);
        Rating badrating1 = RatingController.addRatingTransaction(5,"Comment",99,99);
        RatingController.deleteRatingByIdTransaction(rating1.getId());

    }
}