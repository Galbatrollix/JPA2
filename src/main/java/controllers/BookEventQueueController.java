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

    public static Reservation addReservationTransaction(ObjectId userId, ObjectId bookId){
        Reservation reservationToAdd = new Reservation(new Date(), null, null, bookId, userId);
        MongoCollection<Document> bookCollection = BookEventQueueController.repo.getBookCollection();
        MongoCollection<Document> userCollection = BookEventQueueController.repo.getUserCollection();

        //check for book and user
        Bson filterBook = Filters.eq("_id", bookId);
        Bson filterUser = Filters.eq("_id", userId);
        Document relevantBookDoc = bookCollection.find(filterBook).first();
        Document relevantUserDoc = userCollection.find(filterUser).first();

        if (relevantUserDoc == null || relevantBookDoc == null) {
            System.out.println("Could not find user/book with given id");
            return null;
        }

        //check if reservation already exists
        Bson filterReservation = Filters.and(Filters.eq(BookEventMapper.USER_ID, userId), Filters.eq(BookEventMapper.CLOSE, null));
        Bson filterAlreadyExisting = Filters.and(filterBook, Filters.elemMatch(BookMapper.EVENTS_ACTIVE, filterReservation));
        Document bookWithAlreadyExistingReservation = bookCollection.find(filterAlreadyExisting).first();

        if (bookWithAlreadyExistingReservation != null) {
            System.out.println("Given user already has reservation for this book");
            return null;
        }


        //if book quantity is bigger than eventNum ( length of activeEvent) set expectedEndDate of reservation
        ArrayList<Document> eventsActive = (ArrayList<Document>)relevantBookDoc.get(BookMapper.EVENTS_ACTIVE);
        ArrayList<Document> reservationQueue = (ArrayList<Document>)relevantBookDoc.get(BookMapper.RESERVATION_QUEUE);


        Book relevantBook = BookMapper.fromMongoBook(relevantBookDoc);
        int eventNum = eventsActive.size() + reservationQueue.size();
        boolean activeReservation = relevantBook.getQuantity() > eventNum;
        if (activeReservation) {
            reservationToAdd.setExpectedEndDate(daysFromNow(reservationDaysLimit));
        }


        Document reservationDoc = BookEventMapper.toMongoBookEvent(reservationToAdd);
        reservationDoc.put("_id", new ObjectId());


        System.out.println(String.format("%s.%d", BookMapper.SLOTS, eventNum));
        System.out.println(relevantBookDoc);
        ArrayList<Integer> slots = (ArrayList<Integer>) relevantBookDoc.get(BookMapper.SLOTS);

        slots.set(eventNum, slots.get(eventNum) == 0 ? slots.get(eventNum) + 1 : slots.get(eventNum) - 1);
        Bson updateEvents = Updates.push(activeReservation ? BookMapper.EVENTS_ACTIVE : BookMapper.RESERVATION_QUEUE, reservationDoc);

        Bson updateSlot = Updates.set(BookMapper.SLOTS, slots);
        bookCollection.updateOne(filterBook, updateSlot);
        bookCollection.updateOne(filterBook, updateEvents);

        System.out.println(reservationToAdd.getBookId() + " " + reservationToAdd.getUserId());
        return reservationToAdd;

    }

//    public static BookEvent addReservationTransaction(ObjectId userId, ObjectId bookId){
//        MongoClient client  = MongoClients.create(uri);
//
//        ClientSession session = client.startSession();
//
//        TransactionOptions txnOptions = TransactionOptions.builder()
//                .readPreference(ReadPreference.primary())
//                .readConcern(ReadConcern.LOCAL)
//                .writeConcern(WriteConcern.MAJORITY)
//                .build();
//
//        TransactionBody<Document> txnBody = new TransactionBody<>() {
//            @Override
//            public Document execute() {
//                MongoCollection<Document> userCollection = repo.getUserCollection();
//                MongoCollection<Document> bookCollection = repo.getBookCollection();
//
//                // todo check if user of this id even exists if not, throw error
//                //Document doc = userCollection.find(session, Filters.eq());
//
//
//                Document bookDoc = bookCollection.find(session, Filters.eq(BookMapper.ID, bookId)).first();
//                if (bookDoc == null){
//                    throw new RuntimeException("Book doesn't exist");
//                }
//
//                ArrayList<Document> queue = (ArrayList<Document>)bookDoc.get(BookMapper.RESERVATION_QUEUE);
//                ArrayList<Document> activeEvents = (ArrayList<Document>)bookDoc.get(BookMapper.EVENTS_ACTIVE);
//
//                for (Document document:queue){
//                    if (document.get(BookEventMapper.USER_ID, ObjectId.class).equals(userId)){
//                        throw new RuntimeException("Pending reservation for this user for this book already exists.");
//                    }
//                }
//                for (Document document:activeEvents){
//                    if (document.get(BookEventMapper.USER_ID, ObjectId.class).equals(userId)){
//                        throw new RuntimeException("Active event for this user for this book already exists.");
//                    }
//                }
//
//
//                ArrayList<Integer> slots = (ArrayList<Integer>)bookDoc.get(BookMapper.SLOTS);
//                int available_count = slots.stream().reduce(0, Integer::sum);
//
//                if (available_count == 0) {
//                    // add a reservation in the queue
//                }else{
//                    // add a reservation to actives
//                    // find a 1 in the array and decrement it
//                }
//
//
//
//
//
//                return new Document();
//            }
//        };
//
//
//
//        try {
//            Document reservation = session.withTransaction(txnBody, txnOptions);
//            session.close();
//            return BookEventMapper.fromMongoBookEvent(reservation);
//
//        } catch (RuntimeException e) {
//            session.close();
//        }
//
//        return  null;
//    }

//
//
//    public static Reservation addReservationTransaction(long userId, long bookId) {
//        em.getTransaction().begin();
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Long> query = cb.createQuery(Long.class);
//        From<BookEvent, BookEvent> from = query.from(BookEvent.class);
//
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
