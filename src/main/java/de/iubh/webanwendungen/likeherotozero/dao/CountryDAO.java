package de.iubh.webanwendungen.likeherotozero.dao;

import de.iubh.webanwendungen.likeherotozero.model.Country;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class CountryDAO implements AutoCloseable {

    private EntityManager em;

    public CountryDAO() {
        em = Persistence.createEntityManagerFactory("mydbPU").createEntityManager();
    }

    public Map<Integer, String> getCountryNamesMap() {
        List<Country> countries = em.createQuery("SELECT c FROM Country c", Country.class).getResultList();
        Map<Integer, String> map = new HashMap<>();
        for (Country c : countries) {
            map.put(c.getIdCountry(), c.getName());
        }
        return map;
    }

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