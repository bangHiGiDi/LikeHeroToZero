package de.iubh.webanwendungen.likeherotozero.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class ConnectionTest {
    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("mydbPU");
            EntityManager em = emf.createEntityManager();

            System.out.println("Verbindung zur Datenbank erfolgreich!");
            em.close();
            emf.close();
        } catch (Exception e) {
            System.err.println("Verbindungsfehler: " + e.getMessage());
            e.printStackTrace();
        }
    }
}