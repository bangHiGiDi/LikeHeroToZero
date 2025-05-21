package de.iubh.webanwendungen.likeherotozero.controller;

import de.iubh.webanwendungen.likeherotozero.dao.CO2EmissionsDAO;
import de.iubh.webanwendungen.likeherotozero.dao.CountryDAO;
import de.iubh.webanwendungen.likeherotozero.model.CO2Emissions;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

@Named
@RequestScoped
public class DashboardBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UserBean userBean;

    private List<CO2Emissions> emissions;
    private List<CO2Emissions> unapprovedEmissions;
    private int selectedCountryId;
    private double newEmissionValue;
    private int selectedYear;
    private List<Integer> yearOptions;

    
    /**
     * Initialisiert die Bean nach dem Laden.
     * gibt Staatsangehörige genehmigte Emissionen wieder und erzeugt die Jahresauswahl
     */
    @PostConstruct
    public void init() {
        if (userBean.getLoggedInUser() != null) {
            int countryId = userBean.getLoggedInUser().getCitizenshipID();
            CO2EmissionsDAO dao = new CO2EmissionsDAO();
            emissions = dao.findApprovedByCountryId(countryId);
        }


        yearOptions = new ArrayList<>();
        int currentYear = Year.now().getValue();
        for (int year = currentYear; year >= 1900; year--) {
            yearOptions.add(year);
        }
    }
    
    /**
     * Fügt einen neuen CO2-Emissionswert hinzu (Status: nicht genehmigt)
     */
    public String addEmission() {
        if (!userBean.isAdmin() && !userBean.isScientist() && !userBean.isReviewer()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Keine Berechtigung zum Hinzufügen von Emissionen.", null));
            return null;
        }

        CO2Emissions emission = new CO2Emissions();
        emission.setCountryID(selectedCountryId);
        emission.setTotalEmissions(newEmissionValue);
        emission.setCo2EmissionYear(LocalDateTime.of(selectedYear, 1, 1, 0, 0));
        emission.setCreatedByID(userBean.getLoggedInUser().getIdUsers());

        CO2EmissionsDAO dao = new CO2EmissionsDAO();
        dao.save(emission);


        int countryId = userBean.getLoggedInUser().getCitizenshipID();
        emissions = dao.findApprovedByCountryId(countryId);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Emission gespeichert.", null));

        return "dashboard.xhtml?faces-redirect=true";
    }
    
    // getter für genhmigte Emissionen
    public List<CO2Emissions> getEmissions() {
        return emissions;
    }

    //Erzeugt Dropdown mit Länder der DB
    public List<SelectItem> getCountryOptions() {
        List<SelectItem> items = new ArrayList<>();
        Map<Integer, String> countryMap = new CountryDAO().getCountryNamesMap();
        for (Map.Entry<Integer, String> entry : countryMap.entrySet()) {
            items.add(new SelectItem(entry.getKey(), entry.getValue()));
        }
        return items;
    }

    //gibt nicht genehmigte Emissionen zurück vom ersteller
    public List<CO2Emissions> getUnapprovedEmissions() {
        if (userBean.getLoggedInUser() != null && unapprovedEmissions == null) {
            int userId = userBean.getLoggedInUser().getIdUsers();
            CO2EmissionsDAO dao = new CO2EmissionsDAO();
            unapprovedEmissions = dao.findUnapprovedByUserId(userId);
        }
        return unapprovedEmissions;
    }
    
    //gibt nicht genehmigte Emissionen zurück(Admin/reviewer)
    public List<CO2Emissions> getAllUnapprovedEmissions() {
        CO2EmissionsDAO dao = new CO2EmissionsDAO();
        return dao.findAllUnapproved();
    }
    
    /**
     * Löscht einen Emissionsdaten anhand der id
     */
    public String deleteEmission(int id) {
        CO2EmissionsDAO dao = new CO2EmissionsDAO();
        dao.deleteById(id);
        if (userBean.getLoggedInUser() != null) {
            unapprovedEmissions = dao.findUnapprovedByUserId(userBean.getLoggedInUser().getIdUsers());
        }
        return null; 
    }
    
    /**
     * Genehmigt einen Emissionseintrag(Admin/reviewer)
     */
    public String approveEmission(int id) {
    	
        if (!userBean.isAdmin() && !userBean.isReviewer()) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Keine Berechtigung zum Bestätigen von Emissionen.", null));
            return null;
        }
        new CO2EmissionsDAO().approveEmission(id);
        
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Emission bestätigt.", null));
        return "dashboard.xhtml?faces-redirect=true";    	
    	

    }

    public List<Integer> getYearOptions() {
        return yearOptions;
    }

    public int getSelectedCountryId() {
        return selectedCountryId;
    }

    public void setSelectedCountryId(int selectedCountryId) {
        this.selectedCountryId = selectedCountryId;
    }

    public double getNewEmissionValue() {
        return newEmissionValue;
    }

    public void setNewEmissionValue(double newEmissionValue) {
        this.newEmissionValue = newEmissionValue;
    }

    public int getSelectedYear() {
        return selectedYear;
    }
    
    //Gibt Ländername wieder anhand id
    public String getCountryNameById(int countryId) {
        return new CountryDAO().getCountryNamesMap().getOrDefault(countryId, "Unbekannt");
    }

    public void setSelectedYear(int selectedYear) {
        this.selectedYear = selectedYear;
    }
}
