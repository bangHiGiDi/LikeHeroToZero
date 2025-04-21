package de.iubh.webanwendungen.likeherotozero.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "CO2Emissions")
public class CO2Emissions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "idCO2Emissions")
    private int idCO2Emissions;

    @Column(name = "TotalEmissions", nullable = false)
    private double totalEmissions;

    @Column(name = "CreatedByID", nullable = false)
    private int createdByID;

    @Column(name = "CountryID", nullable = false)
    private int countryID;

    @Column(name = "CreatedAt", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "IsApproved", nullable = false)
    private boolean isApproved;

    @Column(name = "CO2EmissionYear", nullable = false)
    private LocalDateTime co2EmissionYear;

    // Getter and Setter

    public int getIdCO2Emissions() {
        return idCO2Emissions;
    }

    public void setIdCO2Emissions(int idCO2Emissions) {
        this.idCO2Emissions = idCO2Emissions;
    }

    public double getTotalEmissions() {
        return totalEmissions;
    }

    public void setTotalEmissions(double totalEmissions) {
        this.totalEmissions = totalEmissions;
    }

    public int getCreatedByID() {
        return createdByID;
    }

    public void setCreatedByID(int createdByID) {
        this.createdByID = createdByID;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public LocalDateTime getCo2EmissionYear() {
        return co2EmissionYear;
    }

    public void setCo2EmissionYear(LocalDateTime co2EmissionYear) {
        this.co2EmissionYear = co2EmissionYear;
    }

    public String getYearOnly() {
        return co2EmissionYear != null ? String.valueOf(co2EmissionYear.getYear()) : "";
    }
}
