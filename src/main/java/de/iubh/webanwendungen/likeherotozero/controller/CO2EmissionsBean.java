package de.iubh.webanwendungen.likeherotozero.controller;

import de.iubh.webanwendungen.likeherotozero.dao.CO2EmissionsDAO;
import de.iubh.webanwendungen.likeherotozero.dao.CountryDAO;
import de.iubh.webanwendungen.likeherotozero.model.CO2Emissions;

import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class CO2EmissionsBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Integer> years; // Liste aller Jahre (z.B. 2020, 2021, 2022)
    private List<EmissionRow> emissionRows; // Enthält Länderdaten + Jahreswerte

    /**
     * Diese Klasse repräsentiert eine Tabellenzeile: ein Land mit seinen Jahreswerten.
     */
    public static class EmissionRow {
        private String countryName;
        private Map<Integer, Double> emissions = new HashMap<>();

        public EmissionRow(String countryName) {
            this.countryName = countryName;
        }

        public String getCountryName() {
            return countryName;
        }

        public Map<Integer, Double> getEmissions() {
            return emissions;
        }
    }

    @PostConstruct
    public void init() {
        prepareEmissionTable();        // kreiert die verdammte Liste....Land x Jahr
        extractYearsDescending();      // Jahre extrahieren und sortieren (neueste zuerst)
    }

    /**
     * Lädt CO2Daten und grupiert sie nach Ländern.
     * Ergebnis: eine Liste von EmissionRow (Land + Map<Jahr, Wert>)
     */
    private void prepareEmissionTable() {
        CO2EmissionsDAO emissionsDAO = new CO2EmissionsDAO();
        CountryDAO countryDAO = new CountryDAO();

        Map<Integer, String> countryNames = countryDAO.getCountryNamesMap();
        Map<Integer, EmissionRow> tempMap = new TreeMap<>();
        Set<Integer> allYears = new TreeSet<>();

        for (CO2Emissions e : emissionsDAO.findAll()) {
            int year = e.getCo2EmissionYear().getYear();
            int countryId = e.getCountryID();
            allYears.add(year);

            String countryName = countryNames.getOrDefault(countryId, "Unbekannt");
            EmissionRow row = tempMap.computeIfAbsent(countryId, id -> new EmissionRow(countryName));
            row.getEmissions().put(year, e.getTotalEmissions());
        }

        emissionRows = new ArrayList<>(tempMap.values());
        years = new ArrayList<>(allYears);
    }

    private void extractYearsDescending() {
        Collections.reverse(years);
    }

    public List<Integer> getYears() {
        return years;
    }

    public List<EmissionRow> getEmissionRows() {
        return emissionRows;
    }
}
