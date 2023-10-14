package pl.comp.prog;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import model.Student;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        EntityManager em = emf.createEntityManager();
        Student student = new Student();
        student.setName("Kowalski");
        em.getTransaction().begin();
        em.persist(student);
        em.getTransaction().commit();
    }
}