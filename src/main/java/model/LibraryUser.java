package model;

import org.bson.types.ObjectId;



public class LibraryUser extends AbstractEntity{

    private String email;

    private String username;


    public LibraryUser(
                        String username,
                        String email,
                        ObjectId id) {
        super(id);
        this.username = username;
        this.email = email;
    }

    public LibraryUser(
            String username,
            String email,
            String id) {
        super(new ObjectId(id));
        this.username = username;
        this.email = email;
    }

    public LibraryUser(
            String username,
            String email) {
        super(null);
        this.username = username;
        this.email = email;
    }

    public LibraryUser(LibraryUser user){
        super(user.id);
        this.username = user.username;
        this.email = user.email;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }



}
