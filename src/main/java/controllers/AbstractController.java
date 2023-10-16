package controllers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public abstract class AbstractController {
    public static EntityManager em = Persistence.createEntityManagerFactory(
            "test").createEntityManager();

}

