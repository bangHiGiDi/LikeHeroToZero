package de.iubh.webanwendungen.likeherotozero.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "CO2Emissions")
public class CO2Emissions implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idCO2Emissions;

    private double totalEmissions;

    private int createdByID;

    private int countryID;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @Column(name = "IsApproved")
    private boolean isApproved;

    @Column(name = "CO2EmissionYear")
    private LocalDateTime co2EmissionYear;
    
    // Getters & Setters
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

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isApproved() {
		return isApproved;
	}

	public void setApproved(boolean isApproved) {
		this.isApproved = isApproved;
	}

	public LocalDateTime getCo2EmissionYear() {
		return co2EmissionYear;
	}

	public void setCo2EmissionYear(LocalDateTime co2EmissionYear) {
		this.co2EmissionYear = co2EmissionYear;
	}

}