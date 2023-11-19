package model;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class MGTestModel extends AbstractEntity {
    // These define automatic conversion to BSON
    @BsonProperty("test_int")
    public int test_int;

    @BsonProperty("test_string")
    public String test_string;

    // This creator defines automatic conversion FROM BSON
    @BsonCreator
    public MGTestModel(@BsonProperty("_id") ObjectId id,
                       @BsonProperty("test_int") int integer_param,
                       @BsonProperty("test_string") String string_param) {
        super(id);
        this.test_int = integer_param;
        this.test_string = string_param;
    }

    public int getTest_int() {
        return test_int;
    }

    public void setTest_int(int test_int) {
        this.test_int = test_int;
    }

    public String getTest_string() {
        return test_string;
    }

    public void setTest_string(String test_string) {
        this.test_string = test_string;
    }
}
