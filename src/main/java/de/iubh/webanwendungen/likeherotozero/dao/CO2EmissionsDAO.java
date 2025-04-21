package de.iubh.webanwendungen.likeherotozero.dao;

import de.iubh.webanwendungen.likeherotozero.model.CO2Emissions;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.List;

public class CO2EmissionsDAO {

    private EntityManager em;

    public CO2EmissionsDAO() {
        em = Persistence.createEntityManagerFactory("mydbPU").createEntityManager();
    }
    
    public List<CO2Emissions> findAllApproved() {
        return em.createQuery("SELECT c FROM CO2Emissions c WHERE c.isApproved = true ORDER BY c.co2EmissionYear DESC", CO2Emissions.class)
                 .getResultList();
    }

    public List<CO2Emissions> findApprovedByCountryId(int countryId) {
        return em.createQuery(
                "SELECT c FROM CO2Emissions c WHERE c.countryID = :countryId AND c.isApproved = true ORDER BY c.co2EmissionYear DESC", CO2Emissions.class)
                .setParameter("countryId", countryId)
                .getResultList();
    }
    
    public List<CO2Emissions> findUnapprovedByUserId(int userId) {
        return em.createQuery(
                "SELECT c FROM CO2Emissions c WHERE c.createdByID = :userId AND c.isApproved = false ORDER BY c.co2EmissionYear DESC",
                CO2Emissions.class)
                .setParameter("userId", userId)
                .getResultList();
    }
    
    public void deleteById(int id) {
        em.getTransaction().begin();
        CO2Emissions e = em.find(CO2Emissions.class, id);
        if (e != null) {
            em.remove(e);
        }
        em.getTransaction().commit();
    }
    
    public void save(CO2Emissions emission) {
        em.getTransaction().begin();
        em.persist(emission);
        em.getTransaction().commit();
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}