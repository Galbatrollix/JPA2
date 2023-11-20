package controllers;


import jakarta.persistence.LockModeType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.From;
import jakarta.persistence.criteria.Predicate;
import model.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BookEventQueueController extends AbstractController {
    private static final int reservationDaysLimit = 7;
    private static final int lendingDaysLimit = 14;

//    private static Date daysFromNow(int days) {
//        Date date = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.add(Calendar.DATE, days);
//        return cal.getTime();
//
//    }
//
//
//    public static Reservation addReservationTransaction(long userId, long bookId) {
//        em.getTransaction().begin();
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Long> query = cb.createQuery(Long.class);
//        From<BookEvent, BookEvent> from = query.from(BookEvent.class);
//        // todo switch named attributs for metamodel implementation if u have time
//
//        Predicate[] predicates = new Predicate[3];
//        predicates[0] = cb.equal(from.get("book").get("id"), bookId);
//        predicates[1] = cb.equal(from.get("user").get("id"), userId);
//        predicates[2] = cb.isNull(from.get("closeDate"));
//
//        query.select(cb.count(from)).where(predicates);
//        long numActiveEvents = em.createQuery(query).getSingleResult();
//        // if there exists lending or reservation for this user and book already, cancel the operation.
//        if (numActiveEvents > 0) {
//            em.getTransaction().rollback();
//            return null;
//        }
//
//        LibraryUser relevantUser = em.find(LibraryUser.class, userId);
//        Book relevantBook = em.find(Book.class, bookId);
//        //todo maybe some exception handling idk, for now I just return null on mistake
//        if (relevantBook == null || relevantUser == null) {
//            em.getTransaction().rollback();
//            return null;
//        }
//        int eventNum = relevantBook.getEventCount();
//        int quantity = relevantBook.getQuantity();
//
//        relevantBook.setEventCount(eventNum + 1);
//        relevantBook = em.merge(relevantBook);
//
//        Reservation reservation = new Reservation();
//        reservation.setBook(relevantBook);
//        reservation.setUser(relevantUser);
//        reservation.setBeginDate(new Date());
//        if (quantity > eventNum) {
//            reservation.setExpectedEndDate(daysFromNow(reservationDaysLimit));
//        }
//
//        em.persist(reservation);
//        em.getTransaction().commit();
//
//        return reservation;
//    }
//
//    public static void deleteEventByIdTransaction(long eventId) {
//        em.getTransaction().begin();
//        BookEvent eventToDelete = em.find(BookEvent.class, eventId);
//        if (eventToDelete == null || eventToDelete.getCloseDate() != null) {
//            em.getTransaction().rollback();
//            return;
//        }
//        Book relevantBook = eventToDelete.getBook();
//        int eventNum = relevantBook.getEventCount();
//        int quantity = relevantBook.getQuantity();
//
//        relevantBook.setEventCount(eventNum - 1);
//        relevantBook = em.merge(relevantBook);
//
//        eventToDelete.setBook(relevantBook);
//        eventToDelete.setCloseDate(new Date());
//        eventToDelete = em.merge(eventToDelete);
//
//        // if there are no reservations waiting in queue
//        // end the transaction
//        if (eventNum <= quantity) {
//            em.getTransaction().commit();
//            return;
//        }
//
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Reservation> query = cb.createQuery(Reservation.class);
//        From<Reservation, Reservation> from = query.from(Reservation.class);
//        // todo switch named attributs for metamodel implementation if u have time
//
//        Predicate[] predicates = new Predicate[3];
//        predicates[0] = cb.equal(from.get("book").get("id"), relevantBook.getId());
//        predicates[1] = cb.isNull(from.get("closeDate"));
//        predicates[2] = cb.isNull(from.get("expectedEndDate"));
//
//        query.select(from).where(predicates)
//                .orderBy(cb.asc(from.get("beginDate")));
//
//
//        List<Reservation> reservationToUpdateList = em.createQuery(query).setMaxResults(1).getResultList();
//        if (reservationToUpdateList.isEmpty()) {
//            em.getTransaction().commit();
//            return;
//        }
//        Reservation reservationToUpdate = reservationToUpdateList.get(0);
//        reservationToUpdate.setExpectedEndDate(daysFromNow(reservationDaysLimit));
//        em.merge(reservationToUpdate);
//        em.getTransaction().commit();
//
//    }
//
//
//    public static Lending addLendingTransaction(long userId, long bookId) {
//        em.getTransaction().begin();
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<BookEvent> query = cb.createQuery(BookEvent.class);
//        From<BookEvent,BookEvent> from = query.from(BookEvent.class);
//        // todo switch named attributs for metamodel implementation if u have time
//
//        Predicate[] predicates = new Predicate[4];
//        predicates[0] = cb.equal(from.get("book").get("id"), bookId);
//        predicates[3] = cb.equal(from.get("user").get("id"), userId);
//        predicates[1] = cb.isNull(from.get("closeDate"));
//        predicates[2] = cb.isNotNull(from.get("expectedEndDate"));
//
//        query.select(from).where(predicates);
//        List<BookEvent> activeEvents = em.createQuery(query).getResultList();
//        // if a waiting matching reservation exists, close it and make lending in its place
//        if (!activeEvents.isEmpty() && activeEvents.get(0) instanceof Reservation) {
//            BookEvent activeReservation = activeEvents.get(0);
//
//            activeReservation.setCloseDate(new Date());
//            activeReservation = em.merge(activeReservation);
//            // todo perhaps force book version update somewhere here
//            Lending newLending = new Lending();
//            newLending.setBook(activeReservation.getBook());
//            newLending.setUser(activeReservation.getUser());
//            newLending.setBeginDate(new Date());
//            newLending.setExpectedEndDate(daysFromNow(lendingDaysLimit));
//            em.persist(newLending);
//
//            // forcing increment for book object
//            em.find(Book.class, activeReservation.getBook().getId(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
//
//            em.getTransaction().commit();
//            return newLending;
//        }else if (!activeEvents.isEmpty()){ // there is an event but it is lending
//            em.getTransaction().rollback();
//            return null;
//        }
//
//        // otherwise if no waiting active event exists, check if book is available
//        LibraryUser relevantUser = em.find(LibraryUser.class, userId);
//        Book relevantBook = em.find(Book.class, bookId);
//        //todo maybe some exception handling idk, for now I just return null on mistake
//        if (relevantBook == null || relevantUser == null) {
//            em.getTransaction().rollback();
//            return null;
//        }
//
//        int eventNum = relevantBook.getEventCount();
//        int quantity = relevantBook.getQuantity();
//
//        // if queue is full, cannot lend
//        if (quantity <= eventNum) {
//            em.getTransaction().rollback();
//            return null;
//        }
//
//        relevantBook.setEventCount(eventNum + 1);
//        relevantBook = em.merge(relevantBook);
//
//        Lending newLending = new Lending();
//        newLending.setBook(relevantBook);
//        newLending.setUser(relevantUser);
//        newLending.setBeginDate(new Date());
//        newLending.setExpectedEndDate(daysFromNow(lendingDaysLimit));
//        em.persist(newLending);
//        em.getTransaction().commit();
//
//        return newLending;
//
//    }


}
