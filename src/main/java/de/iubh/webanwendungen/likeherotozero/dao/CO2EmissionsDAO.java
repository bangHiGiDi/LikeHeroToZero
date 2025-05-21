package de.iubh.webanwendungen.likeherotozero.dao;

import de.iubh.webanwendungen.likeherotozero.model.CO2Emissions;
import de.iubh.webanwendungen.likeherotozero.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class CO2EmissionsDAO {
	
    /**
     * Neuer Emissionseintrag in der Datenbank.
     */
    public void save(CO2Emissions emission) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(emission);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
    
    /**
     * Setzt das Feld isApproved eines Emissionseintrags auf true.
     */
    public void approveEmission(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            CO2Emissions emission = em.find(CO2Emissions.class, id);
            if (emission != null) {
                emission.setApproved(true);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    /**
     * Löscht eine Emission anhand seiner id.
     */
    public void deleteById(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            CO2Emissions e = em.find(CO2Emissions.class, id);
            if (e != null) {
                em.remove(e);
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    /**
     * Gibt alle genehmigten CO2-Einträge zurück, sortiert nach Jahr (absteigend).
     */
    public List<CO2Emissions> findAllApproved() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<CO2Emissions> query = em.createQuery(
                "SELECT c FROM CO2Emissions c WHERE c.isApproved = true ORDER BY c.co2EmissionYear DESC",
                CO2Emissions.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    /**
     * Gibt alle genehmigten CO2-Einträge eines bestimmten Landes zurück.
     */
    public List<CO2Emissions> findApprovedByCountryId(int countryId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<CO2Emissions> query = em.createQuery(
                "SELECT c FROM CO2Emissions c WHERE c.countryID = :countryId AND c.isApproved = true ORDER BY c.co2EmissionYear DESC",
                CO2Emissions.class);
            query.setParameter("countryId", countryId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Gibt alle User, noch nicht genehmigten Einträge zurück.
     */
    public List<CO2Emissions> findUnapprovedByUserId(int userId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<CO2Emissions> query = em.createQuery(
                "SELECT c FROM CO2Emissions c WHERE c.createdByID = :userId AND c.isApproved = false ORDER BY c.co2EmissionYear DESC",
                CO2Emissions.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
    /**
     * Gibt alle nicht genehmigten Emissionseinträge zurück.
     */
    public List<CO2Emissions> findAllUnapproved() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<CO2Emissions> query = em.createQuery(
                "SELECT e FROM CO2Emissions e WHERE e.isApproved = false ORDER BY e.co2EmissionYear DESC",
                CO2Emissions.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
