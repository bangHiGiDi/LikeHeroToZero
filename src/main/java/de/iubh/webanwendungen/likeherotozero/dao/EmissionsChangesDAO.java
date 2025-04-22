package de.iubh.webanwendungen.likeherotozero.dao;

import de.iubh.webanwendungen.likeherotozero.model.EmissionsChanges;
import de.iubh.webanwendungen.likeherotozero.util.JPAUtil;

import jakarta.persistence.EntityManager;

public class EmissionsChangesDAO {

    public void save(EmissionsChanges change) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(change);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
