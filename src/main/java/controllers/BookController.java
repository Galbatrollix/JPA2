package controllers;

import model.Book;
import model.BookData;

public class BookController extends AbstractController {

//    public static Book addBookTransaction(String title, String author, int quantity) {
//        BookData bookData = new BookData();
//        bookData.setAuthor(author);
//        bookData.setTitle(title);
//        Book book = new Book();
//        book.setData(bookData);
//        book.setQuantity(quantity);
//        book.setEventCount(0);
//        em.getTransaction().begin();
//        em.persist(book);
//        em.getTransaction().commit();
//        return book;
//    }

    //todo maybe add some cascading for case where we
    // delete when there are existing events
    public static void deleteBookTransaction(long bookId) {
        em.getTransaction().begin();
        Book bookToDelete = em.find(Book.class, bookId);
        em.remove(bookToDelete);
        em.getTransaction().commit();
    }


}
