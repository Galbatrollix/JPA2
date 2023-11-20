package mongoMappers;

import model.MGTestModel;
import org.bson.Document;
import org.bson.types.ObjectId;


public class MGTestMapper {
    public static final String ID = "_id";
    public static final String TEST_STRING = "test_string";
    public static final String TEST_INT = "test_int";
    public static final String TEST_INT_SMALLER = "smaller_int";

    public static Document toMongoTest(MGTestModel testModel) {
        Document testDocument = new Document(ID, new ObjectId()).
                append(TEST_STRING, testModel.getTest_string()).
                append(TEST_INT, testModel.getTest_int())
                .append(TEST_INT_SMALLER, testModel.getSmaller_int());

        return testDocument;
    }

    public static MGTestModel fromMongoTest(Document testDocument) {
        MGTestModel testModel = new MGTestModel(
                testDocument.get(ID, ObjectId.class),
                testDocument.get(TEST_INT, Integer.class),
                testDocument.get(TEST_INT_SMALLER, Integer.class),
                testDocument.get(TEST_STRING, String.class));
        return testModel;
    }
}