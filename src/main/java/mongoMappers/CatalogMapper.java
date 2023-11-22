package mongoMappers;

import model.Catalog;
import org.bson.Document;
import org.bson.types.ObjectId;

public class CatalogMapper {
    public static final String ID = "_id";
    public static final String CATALOG_NAME = "name";

    public static Document toMongoCatalog(Catalog catalog) {
        Document catalogDocument = new Document(ID, catalog.getId()).
                append(CATALOG_NAME, catalog.getName());

        return catalogDocument;
    }

    public static Catalog fromMongoCatalog(Document catalogDocument, Document bookDocument) {
        Catalog catalog = new Catalog(
                catalogDocument.get(CATALOG_NAME, String.class),
                bookDocument.get(ID, ObjectId.class),
                catalogDocument.get(ID, ObjectId.class));
        return catalog;
    }

}
