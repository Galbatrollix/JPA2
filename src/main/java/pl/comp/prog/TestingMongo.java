package pl.comp.prog;


import controllers.*;
import model.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestingMongo {


    @BeforeAll
    static void initMongo() {
        AbstractController.attachMongoRepository();
    }

    @AfterAll
    static void closeMongo() {
        AbstractController.closeMongoRepository();
    }

    private boolean checkIfBookInDB(Book bookToCheck) {
        List<Book> books = BookController.getAllBooks();
        boolean containsBook = false;

        for (Book book : books) {
            if (book.getId().equals(bookToCheck.getId())) {
                containsBook = true;
                break;
            }
        }
        return containsBook;
    }

    private boolean checkIfUserInDB(LibraryUser userToCheck) {
        List<LibraryUser> users = LibraryUserController.getAllUsers();
        boolean containsUser = false;

        for (LibraryUser user: users) {
            if (user.getId().equals(userToCheck.getId())) {
                containsUser = true;
                break;
            }
        }
        return containsUser;
    }

    @Test
    void testBookCreation() {
        Book book = BookController.addNewBook(new Book("Pan Tadeusz", "A.Mickiewicz", 2));
        assertNotNull(book);
        assertNotNull(book.getId());

        boolean containsBook = checkIfBookInDB(book);
        assertTrue(containsBook);
    }

    @Test
    public void testBookDeletion() {
        Book bookToDelete = BookController.addNewBook(new Book("This should be deleted", "Author", 2));
        assertNotNull(bookToDelete);
        assertNotNull(bookToDelete.getId());
        BookController.deleteBook(bookToDelete.getId());

        boolean containsBook = checkIfBookInDB(bookToDelete);

        assertFalse(containsBook);
    }

    @Test
    public void testUserCreation() {
        LibraryUser user = LibraryUserController.addNewLibraryUser(new LibraryUser("bomba@email.com", "kapitan"));
        assertNotNull(user);
        assertNotNull(user.getId());

        boolean containsUser = checkIfUserInDB(user);
        assertTrue(containsUser);
    }

    @Test
    public void testUserDeletion() {
        LibraryUser userToDelete = LibraryUserController.addNewLibraryUser(new LibraryUser("delet@mail.com", "Mr deleted"));
        assertNotNull(userToDelete);
        assertNotNull(userToDelete.getId());
        LibraryUserController.deleteUser(userToDelete.getId());

        boolean containsBook = checkIfUserInDB(userToDelete);

        assertFalse(containsBook);
    }


}
