package controllers;


import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.TransactionOptions;
import com.mongodb.WriteConcern;
import com.mongodb.client.*;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.*;
import mongoMappers.BookEventMapper;
import mongoMappers.BookMapper;
import mongoMappers.LibraryUserMapper;
import mongoMappers.RatingMapper;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.*;


public class BookEventQueueController extends AbstractController {

    private static String uri = "mongodb://localhost:27017,localhost:27018,localhost:27019/?replicaSet=replica_set_single";

    private static final int reservationDaysLimit = 7;
    private static final int lendingDaysLimit = 14;

    private static Date daysFromNow(int days) {
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();

    }

    private static boolean userAndBookExists(ObjectId userId, ObjectId bookId) {
        MongoCollection<Document> bookCollection = BookEventQueueController.repo.getBookCollection();
        MongoCollection<Document> userCollection = BookEventQueueController.repo.getUserCollection();

        //check for book and user
        Bson filterBook = Filters.eq("_id", bookId);
        Bson filterUser = Filters.eq("_id", userId);
        Document relevantBookDoc = bookCollection.find(filterBook).first();
        Document relevantUserDoc = userCollection.find(filterUser).first();
        return relevantUserDoc != null && relevantBookDoc != null;
    }

    public static Reservation addNewReservation(ObjectId userId, ObjectId bookId){
        Reservation reservationToAdd = new Reservation(new Date(), null, null, bookId, userId);

        if (!userAndBookExists(userId, bookId)) {
            System.out.println("Could not find user/book with given id");
            return null;
        }
        MongoCollection<Document> bookCollection = BookEventQueueController.repo.getBookCollection();
        Bson filterBook = Filters.eq("_id", bookId);
        Document relevantBookDoc = bookCollection.find(filterBook).first();

        //check if reservation already exists
        Bson filterReservation = Filters.and(Filters.eq(BookEventMapper.USER_ID, userId), Filters.eq(BookEventMapper.CLOSE, null));
        Bson filterAlreadyExistingActive = Filters.and(filterBook, Filters.elemMatch(BookMapper.EVENTS_ACTIVE, filterReservation));
        Document bookWithAlreadyExistingActiveReservation = bookCollection.find(filterAlreadyExistingActive).first();

        Bson filterAlreadyExistingQueue = Filters.and(filterBook, Filters.elemMatch(BookMapper.RESERVATION_QUEUE, filterReservation));
        Document bookWithAlreadyExistingReservation = bookCollection.find(filterAlreadyExistingQueue).first();

        if (bookWithAlreadyExistingReservation != null || bookWithAlreadyExistingActiveReservation != null) {
            System.out.println("Given user already has reservation for this book");
            return null;
        }

        ArrayList<Integer> slots = (ArrayList<Integer>)relevantBookDoc.get(BookMapper.SLOTS);
        int available_count = slots.stream().reduce(0, Integer::sum);
        boolean activeReservation = available_count != 0;

        if (activeReservation) {
            // add a reservation to actives
            reservationToAdd.setExpectedEndDate(daysFromNow(reservationDaysLimit));
            // find a 1 in the array and decrement it
            int index = slots.indexOf(1);
            slots.set(index, slots.get(index) - 1);
            Bson updateSlot = Updates.set(BookMapper.SLOTS, slots);
            bookCollection.updateOne(filterBook, updateSlot);
        }
        //otherwise it will be added to queue

        Document reservationDoc = BookEventMapper.toMongoBookEvent(reservationToAdd);
        reservationDoc.put("_id", new ObjectId());

        Bson updateEvents = Updates.push(activeReservation ? BookMapper.EVENTS_ACTIVE : BookMapper.RESERVATION_QUEUE, reservationDoc);
        bookCollection.updateOne(filterBook, updateEvents);

        System.out.println(reservationToAdd.getBookId() + " " + reservationToAdd.getUserId());
        return reservationToAdd;

    }

    public static Lending addNewLending(ObjectId userId, ObjectId bookId){
        Lending lendingToAdd = new Lending(new Date(), daysFromNow(lendingDaysLimit), null, bookId, userId);

        if (!userAndBookExists(userId, bookId)) {
            System.out.println("Could not find user/book with given id");
            return null;
        }
        MongoCollection<Document> bookCollection = BookEventQueueController.repo.getBookCollection();
        Bson filterBook = Filters.eq("_id", bookId);
        Document relevantBookDoc = bookCollection.find(filterBook).first();

        //check if lending already exists
        Bson filterLending = Filters.and(Filters.eq(BookEventMapper.USER_ID, userId), Filters.eq(BookEventMapper.CLOSE, null));
        Bson filterAlreadyExisting = Filters.and(filterBook, Filters.elemMatch(BookMapper.EVENTS_ACTIVE, filterLending));
        Document bookWithAlreadyExistingLending = bookCollection.find(filterAlreadyExisting).first();

        if (bookWithAlreadyExistingLending != null) {
            System.out.println("Given user is already lending this book");
            return null;
        }

        ArrayList<Integer> slots = (ArrayList<Integer>)relevantBookDoc.get(BookMapper.SLOTS);
        int available_count = slots.stream().reduce(0, Integer::sum);
        boolean availableToLend = available_count != 0;
        if (!availableToLend) {
            System.out.println("Currently there is no book to lend");
            return null;
        }
        //TODO check for matching reservation

        int index = slots.indexOf(1);
        slots.set(index, slots.get(index) - 1);
        Bson updateSlot = Updates.set(BookMapper.SLOTS, slots);
        bookCollection.updateOne(filterBook, updateSlot);

        Document lendingDoc = BookEventMapper.toMongoBookEvent(lendingToAdd);
        lendingDoc.put("_id", new ObjectId());

        Bson updateEvents = Updates.push(BookMapper.EVENTS_ACTIVE, lendingDoc);
        bookCollection.updateOne(filterBook, updateEvents);

        System.out.println(lendingToAdd.getBookId() + " " + lendingToAdd.getUserId());
        return lendingToAdd;

    }

    public static BookEvent getBookEvent(ObjectId bookEventId) {
        //TODO fix
        MongoCollection<Document> bookCollection = CatalogController.repo.getBookCollection();
        Bson filterQueue = Filters.eq("reservation_queue._id", bookEventId);
        Bson filterEvent = Filters.eq("events_active._id", bookEventId);
        Bson filterBookQueue = Filters.elemMatch(BookMapper.RESERVATION_QUEUE, filterQueue);
        Document bookDocQueue = bookCollection.find(filterBookQueue).first();
        Bson filterBookEvent = Filters.elemMatch(BookMapper.EVENTS_ACTIVE, filterEvent);
        Document bookDocEvent = bookCollection.find(filterBookEvent).first();

        System.out.println(bookDocEvent);
        System.out.println(bookDocQueue);
        BookEvent bookEventFound = null;

        if (bookDocQueue != null) {
           ArrayList<Document> reservationQueue = (ArrayList<Document>)bookDocQueue.get(BookMapper.RESERVATION_QUEUE);

            for (Document event : reservationQueue) {
                if (event.getObjectId("_id").equals(bookEventId)) {
                    bookEventFound = BookEventMapper.fromMongoBookEvent(event, bookDocQueue);
                    break;
                }
            }

        } else if (bookDocEvent != null) {
            System.out.println(bookEventId);
            ArrayList<Document> eventsActive = (ArrayList<Document>)bookDocEvent.get(BookMapper.EVENTS_ACTIVE);

            for (Document event : eventsActive) {
                System.out.println(event);
                if (event.getObjectId("_id").equals(bookEventId)) {
                    System.out.println("here");
                    bookEventFound = BookEventMapper.fromMongoBookEvent(event, bookDocEvent);
                    break;
                }
            }
        }
        return bookEventFound;



    }

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


}
