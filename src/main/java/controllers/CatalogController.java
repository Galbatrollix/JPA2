package controllers;


import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import model.Book;
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
        CatalogController.addToCash(catalogDoc, RedisRepository.catalogHashPrefix, 300);

        Document relevantBookDoc = bookCollection.find(filterBook).first();
        CatalogController.addToCash(relevantBookDoc, RedisRepository.bookHashPrefix, 300);



        Catalog resultCatalog = new Catalog(catalog);
        resultCatalog.setId(catalogDoc.getObjectId("_id"));
        return resultCatalog;

    }

    public static Catalog getCatalog(ObjectId catalogId){
        MongoCollection<Document> bookCollection = CatalogController.mongoRepo.getBookCollection();
        Bson filter = Filters.eq("catalog._id", catalogId);
        Document bookDoc = bookCollection.find(filter).first();

        Document catalogFromCasheDoc = CatalogController.redisRepo.getDocumentFromCashe(RedisRepository.bookHashPrefix, catalogId);

        if (catalogFromCasheDoc != null) {
            System.out.println("got catalog from cashe");
            return CatalogMapper.fromMongoCatalog(catalogFromCasheDoc, bookDoc);
        }

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
