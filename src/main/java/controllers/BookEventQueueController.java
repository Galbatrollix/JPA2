package controllers;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import model.*;

import java.sql.Time;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookEventQueueController extends AbstractController {
    private static final int reservationDaysLimit = 7;
    private static final int lendingDaysLimit = 14;

    private static Date daysFromNow(int days) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();

    }
    public static Reservation addReservationTEST(long userId, long bookId){
        em.getTransaction().begin();
        Reservation reservation = new Reservation();
        reservation.setBeginDate(new Date());


        LibraryUser relevantUser = em.find(LibraryUser.class, userId);
        Book relevantBook = em.find(Book.class, bookId);
        //todo maybe some exception handling idk, for now I just return null on mistake
        if (relevantBook == null || relevantUser == null){
            em.getTransaction().rollback();
            return null;
        }

        reservation.setBook(relevantBook);
        reservation.setUser(relevantUser);
        em.persist(reservation);
        em.getTransaction().commit();

        return reservation;
    }


    public static Reservation addReservationTransaction(long userId, long bookId){
        em.getTransaction().begin();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        From<BookEvent, BookEvent> from = query.from(BookEvent.class);
        // todo switch named attributs for metamodel implementation if u have time

        Predicate[] predicates = new Predicate[3];
        predicates[0] = cb.equal(from.get("book").get("id"), bookId);
        predicates[1] = cb.equal(from.get("user").get("id"), userId);
        predicates[2] = cb.isNull(from.get("closeDate"));

        query.select(cb.count(from)).where(predicates);
        long numActiveEvents = em.createQuery(query).getSingleResult();
        // if there exists lending or reservation for this user and book already, cancel the operation.
        if (numActiveEvents > 0){
            em.getTransaction().rollback();
            return null;
        }

        LibraryUser relevantUser = em.find(LibraryUser.class, userId);
        Book relevantBook = em.find(Book.class, bookId);
        //todo maybe some exception handling idk, for now I just return null on mistake
        if (relevantBook == null || relevantUser == null){
            em.getTransaction().rollback();
            return null;
        }
        int eventNum = relevantBook.getEventCount();
        int quantity = relevantBook.getQuantity();

        Reservation reservation = new Reservation();
        reservation.setBook(relevantBook);
        reservation.setUser(relevantUser);
        reservation.setBeginDate(new Date());
        if (quantity > eventNum){
            reservation.setExpectedEndDate(daysFromNow(reservationDaysLimit));
        }

        relevantBook.setEventCount(eventNum + 1);
        em.merge(relevantBook);
        em.persist(reservation);

        em.getTransaction().commit();

        return reservation;
    }

}
