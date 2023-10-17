package controllers;

import jakarta.persistence.criteria.*;
import model.Book;
import model.Catalog;
import model.LibraryUser;
import model.Rating;

import java.util.List;

public class CatalogController extends AbstractController{

    public static Catalog addCatalogTransaction(String name, List<Long> bookIds){
        Catalog catalog = new Catalog();
        catalog.setName(name);

        em.getTransaction().begin();

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Book> query = cb.createQuery(Book.class);
        From<Book, Book> from = query.from(Book.class);
        // todo switch named attribute id for metamodel implementation if u have time
        query.select(from).where(from.get("id").in(bookIds));
        List<Book> found = em.createQuery(query).getResultList();
        catalog.setBooks(found);
        em.persist(catalog);
        em.getTransaction().commit();

        return catalog;
    }

    public static void deleteCatalogByIdTransaction(long catalogId){
        em.getTransaction().begin();

        Catalog catalogToDelete = em.find(Catalog.class, catalogId);
        em.remove(catalogToDelete);

        em.getTransaction().commit();
    }
}
