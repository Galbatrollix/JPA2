package controllers;
import model.Rating;
import model.Book;
import model.LibraryUser;


public class RatingController extends AbstractController{

    public static Rating addRatingTransaction(int stars, String comment, long bookId, long userID){
        Rating rating = new Rating();
        rating.setStars(stars);
        rating.setComment(comment);

        em.getTransaction().begin();

        LibraryUser relevantUser = em.find(LibraryUser.class, userID);
        Book relevantBook = em.find(Book.class, bookId);
        //todo maybe some exception handling idk, for now I just return null on mistake
        if (relevantBook == null || relevantUser == null){
            em.getTransaction().rollback();
            return null;
        }

        rating.setBook(relevantBook);
        rating.setUser(relevantUser);
        em.persist(rating);
        em.getTransaction().commit();

        return rating;
    }

    public static void deleteRatingByIdTransaction(long ratingId){
        em.getTransaction().begin();

        Rating ratingToDelete = em.find(Rating.class, ratingId);
        em.remove(ratingToDelete);

        em.getTransaction().commit();
    }
}
