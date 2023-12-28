package pl.nbd.cassandra.model;


import org.bson.types.ObjectId;

public class Rating {
    private int stars;

    private String comment;

    private ObjectId bookId;

    private ObjectId userId;

//    public Rating(
//            int stars,
//            String comment,
//            ObjectId bookId,
//            ObjectId userId,
//            ObjectId id) {
//        super(id);
//        this.stars = stars;
//        this.comment = comment;
//        this.bookId = bookId;
//        this.userId  = userId;
//    }
//
//    public Rating(
//            int stars,
//            String comment,
//            String bookId,
//            String userId,
//            String id) {
//        super(new ObjectId(id));
//        this.stars = stars;
//        this.comment = comment;
//        this.bookId = new ObjectId(bookId);
//        this.userId  = new ObjectId(userId);
//    }
//
//    public Rating(
//            int stars,
//            String comment,
//            ObjectId bookId,
//            ObjectId userId) {
//        super(null);
//        this.stars = stars;
//        this.comment = comment;
//        this.bookId = bookId;
//        this.userId  = userId;
//    }
//
//
//    public Rating(Rating rating) {
//        super(rating.id);
//        this.stars = rating.stars;
//        this.comment = rating.comment;
//        this.bookId = rating.bookId;
//        this.userId = rating.userId;
//    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ObjectId getBookId() {
        return bookId;
    }

    public void setBookId(ObjectId bookId) {
        this.bookId = bookId;
    }

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }
}
