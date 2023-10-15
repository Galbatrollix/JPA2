package controllers;

import model.Book;
import model.BookData;

public class BookController extends AbstractController {


    public BookController() {
        super();
    }

    public Book addBookTransaction(String title, String author, int quantity) {
        BookData bookData = new BookData();
        bookData.setAuthor(author);
        bookData.setTitle(title);
        Book book = new Book();
        book.setData(bookData);
        book.setQuantityTotal(quantity);
        book.setQuantityAvailable(quantity);
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(book);
        this.entityManager.getTransaction().commit();
        return book;
    }

    public void deleteBookTransaction(Book book) {
        long bookId = book.getId();
        this.entityManager.getTransaction().begin();
        Book bookToDelete = this.entityManager.find(Book.class, bookId);
        this.entityManager.remove(bookToDelete);
        this.entityManager.getTransaction().commit();
    }


}
