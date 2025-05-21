package de.iubh.webanwendungen.likeherotozero.dao;

import de.iubh.webanwendungen.likeherotozero.model.Country;
import de.iubh.webanwendungen.likeherotozero.util.JPAUtil;

import jakarta.persistence.EntityManager;
import java.util.*;

public class CountryDAO implements AutoCloseable {

    private EntityManager em;
    
    /**
     * Initialisiert EntityManager über hilfsklasse JPAUtil-Klasse.
     */
    public CountryDAO() {
        em = JPAUtil.getEntityManager();
    }
    
    /**
     * Gibt alle Länder in Form einer Map ID -> Name zurück.
     */
    public Map<Integer, String> getCountryNamesMap() {
        List<Country> countries = em.createQuery("SELECT c FROM Country c", Country.class).getResultList();
        Map<Integer, String> map = new HashMap<>();
        for (Country c : countries) {
            map.put(c.getIdCountry(), c.getName());
        }
        return map;
    }
    
    /**
     * Gibt alle Länder als Liste von Map.Objekten zurück.
     */
    public List<Map.Entry<Integer, String>> getAllCountriesAsMapEntries() {
        return new ArrayList<>(getCountryNamesMap().entrySet());
    }

    @Override
    public void close() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
}
