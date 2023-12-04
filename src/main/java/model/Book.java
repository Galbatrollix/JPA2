package model;


import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class Book extends AbstractEntity {


    private double quantity;

    private String title;
    private String author;

    public Book(
            String bookTitle,
            String bookAuthor,
            double quantity,
            String id) {
        super(new ObjectId(id));
        this.title = bookTitle;
        this.author = bookAuthor;
        this.quantity = quantity;
    }

    public Book(
            String bookTitle,
            String bookAuthor,
            double quantity,
            ObjectId id) {
        super(id);
        this.title = bookTitle;
        this.author = bookAuthor;
        this.quantity = quantity;
    }

    public Book(
            String bookTitle,
            String bookAuthor,
            int quantity) {
        super(null);
        this.title = bookTitle;
        this.author = bookAuthor;
        this.quantity = quantity;
    }


    public Book(Book book){
        super(book.id);
        this.title = book.title;
        this.author = book.author;
        this.quantity = book.quantity;
    }


    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


}
