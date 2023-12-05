package model;

import org.bson.types.ObjectId;

public class Catalog extends AbstractEntity{

    private ObjectId bookId;
    private String name;

    public Catalog(
            String name,
            ObjectId bookId,
            ObjectId id) {
        super(id);
        this.name = name;
        this.bookId = bookId;
    }

    public Catalog(
            String name,
            String bookId,
            String id) {
        super(new ObjectId(id));
        this.name = name;
        this.bookId = new ObjectId(bookId);
    }

    public Catalog(
            String name,
            ObjectId bookId) {
        super(null);
        this.name = name;
        this.bookId = bookId;
    }


    public Catalog(Catalog catalog) {
        super(catalog.id);
        this.name = catalog.name;
        this.bookId = catalog.bookId;
    }

    public ObjectId getBookId() {
        return bookId;
    }

    public void setBookId(ObjectId bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
