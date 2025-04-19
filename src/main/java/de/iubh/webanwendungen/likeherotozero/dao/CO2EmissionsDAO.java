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

    public List<CO2Emissions> findAll() {
        return em.createQuery("SELECT c FROM CO2Emissions c", CO2Emissions.class).getResultList();
    }
    
    public List<CO2Emissions> findByCountryId(int countryId) {
        return em.createQuery(
            "SELECT c FROM CO2Emissions c WHERE c.countryID = :countryId ORDER BY c.co2EmissionYear DESC",
            CO2Emissions.class
        )
        .setParameter("countryId", countryId)
        .getResultList();
    }

    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}