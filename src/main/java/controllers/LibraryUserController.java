package controllers;

import model.LibraryUser;

public class LibraryUserController extends AbstractController{

    public static LibraryUser addUserTransaction(String email, String username) {
        LibraryUser user = new LibraryUser();
        user.setEmail(email);
        user.setUsername(username);
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        return user;
    }

    //todo maybe add some cascading for case where we
    // delete when there are existing events
    public static void deleteUserTransaction(LibraryUser user) {
        long userId = user.getId();
        em.getTransaction().begin();
        LibraryUser userToDelete = em.find(LibraryUser.class, userId);
        em.remove(userToDelete);
        em.getTransaction().commit();
    }



}
