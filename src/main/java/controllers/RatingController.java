package controllers;

import jakarta.persistence.LockModeType;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import model.Rating;
import model.Book;
import model.LibraryUser;


public class RatingController extends AbstractController {

//    public static Rating addRatingTransaction(int stars, String comment, long bookId, long userID) {
//        Rating rating = new Rating();
//        rating.setStars(stars);
//        rating.setComment(comment);
//
//        em.getTransaction().begin();
//
//        LibraryUser relevantUser = em.find(LibraryUser.class, userID, LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//        Book relevantBook = em.find(Book.class, bookId);
//        //todo maybe some exception handling idk, for now I just return null on mistake
//        if (relevantBook == null || relevantUser == null) {
//            em.getTransaction().rollback();
//            return null;
//        }
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Rating> query = cb.createQuery(Rating.class);
//        From<Rating, Rating> from = query.from(Rating.class);
//        // todo switch named attributs for metamodel implementation if u have time
//
//        Predicate[] predicates = new Predicate[2];
//        predicates[0] = cb.equal(from.get("book").get("id"), bookId);
//        predicates[1] = cb.equal(from.get("user").get("id"), userID);
//
//        query.select(from).where(predicates);
//        try {
//            Rating ratingThatAlreadyExist = em.createQuery(query).getSingleResult();
//        } catch (NoResultException e) {
//            rating.setBook(relevantBook);
//            rating.setUser(relevantUser);
//            em.persist(rating);
//            em.getTransaction().commit();
//            return rating;
//        }
//        em.getTransaction().rollback();
//        return null;
//    }
//
//    public static void deleteRatingByIdTransaction(long ratingId) {
//        em.getTransaction().begin();
//
//        Rating ratingToDelete = em.find(Rating.class, ratingId);
//        em.remove(ratingToDelete);
//
//        em.getTransaction().commit();
//    }

}
