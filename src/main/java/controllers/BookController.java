package controllers;

import model.Book;
import model.BookData;

public class BookController extends AbstractController {


    public BookController() {
        super();
    }

    public static Book addBookTransaction(String title, String author, int quantity) {
        BookData bookData = new BookData();
        bookData.setAuthor(author);
        bookData.setTitle(title);
        Book book = new Book();
        book.setData(bookData);
        book.setQuantityTotal(quantity);
        book.setQuantityAvailable(quantity);
        em.getTransaction().begin();
        em.persist(book);
        em.getTransaction().commit();
        return book;
    }

    public static void deleteBookTransaction(Book book) {
        long bookId = book.getId();
        em.getTransaction().begin();
        Book bookToDelete = em.find(Book.class, bookId);
        em.remove(bookToDelete);
        em.getTransaction().commit();
    }


}
