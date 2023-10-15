package controllers;

import model.LibraryUser;

public class LibraryUserController extends AbstractController{

    public LibraryUserController() { super(); }

    public LibraryUser addUserTransaction(String email, String username) {
        LibraryUser user = new LibraryUser();
        user.setEmail(email);
        user.setUsername(username);
        this.entityManager.getTransaction().begin();
        this.entityManager.persist(user);
        this.entityManager.getTransaction().commit();
        return user;
    }

    public void deleteUserTransaction(LibraryUser user) {
        long userId = user.getId();
        this.entityManager.getTransaction().begin();
        LibraryUser userToDelete = this.entityManager.find(LibraryUser.class, userId);
        this.entityManager.remove(userToDelete);
        this.entityManager.getTransaction().commit();
    }



}
