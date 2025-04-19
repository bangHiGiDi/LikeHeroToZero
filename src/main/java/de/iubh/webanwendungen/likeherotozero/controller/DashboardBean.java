package de.iubh.webanwendungen.likeherotozero.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

import de.iubh.webanwendungen.likeherotozero.dao.CO2EmissionsDAO;
import de.iubh.webanwendungen.likeherotozero.model.CO2Emissions;

@Named
@RequestScoped
public class DashboardBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UserBean userBean;

    private List<CO2Emissions> emissions;

    @PostConstruct
    public void init() {
        if (userBean.getLoggedInUser() != null) {
            int countryId = userBean.getLoggedInUser().getCitizenshipID();
            CO2EmissionsDAO dao = new CO2EmissionsDAO();
            emissions = dao.findByCountryId(countryId);
        }
    }

    public List<CO2Emissions> getEmissions() {
        return emissions;
    }

}