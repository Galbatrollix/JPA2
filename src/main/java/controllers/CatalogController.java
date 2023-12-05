package controllers;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.Catalog;
import mongoMappers.BookMapper;
import mongoMappers.CatalogMapper;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import redisRepo.RedisRepository;

public class CatalogController extends AbstractController{

    public static Catalog addNewCatalog(Catalog catalog) {
        MongoCollection<Document> bookCollection = CatalogController.mongoRepo.getBookCollection();

        Document catalogDoc = CatalogMapper.toMongoCatalog(catalog);
        catalogDoc.put(CatalogMapper.ID, new ObjectId());

        Bson filterBook = Filters.eq("_id", catalog.getBookId());
        Bson update = Updates.set(BookMapper.CATALOG, catalogDoc);
        bookCollection.updateOne(filterBook, update);

        CatalogController.addCatalogToCashe(catalogDoc, catalog.getBookId(),  EXPIRATION);
        Document relevantBookDoc = bookCollection.find(filterBook).first();
        CatalogController.addBookWithCatalogToCashe(relevantBookDoc, catalogDoc, EXPIRATION);

        Catalog resultCatalog = new Catalog(catalog);
        resultCatalog.setId(catalogDoc.getObjectId("_id"));
        return resultCatalog;

    }

    public static Catalog getCatalog(ObjectId catalogId){
        Document catalogFromCasheDoc = CatalogController.getFromCashe(RedisRepository.catalogHashPrefix, catalogId);

        if (catalogFromCasheDoc != null) {
            System.out.println("got catalog from cashe");
            return CatalogMapper.fromRedisCatalog(catalogFromCasheDoc);
        }
        MongoCollection<Document> bookCollection = CatalogController.mongoRepo.getBookCollection();
        Bson filter = Filters.eq("catalog._id", catalogId);
        Document bookDoc = bookCollection.find(filter).first();


        Document catalogDoc = (Document)bookDoc.get(BookMapper.CATALOG);

        return CatalogMapper.fromMongoCatalog(catalogDoc, bookDoc);
    }


    public static void deleteCatalog(ObjectId catalogId) {
        MongoCollection<Document> bookCollection = CatalogController.mongoRepo.getBookCollection();
        Bson filter = Filters.eq("catalog._id", catalogId);
        Bson update = Updates.unset(BookMapper.CATALOG);
        bookCollection.updateOne(filter, update);
    }

}
