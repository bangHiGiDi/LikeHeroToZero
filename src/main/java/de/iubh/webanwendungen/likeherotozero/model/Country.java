package de.iubh.webanwendungen.likeherotozero.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Country")
public class Country {
    @Id
    private int idCountry;

    private String name;

    private String countryCode;
    
    // Getter & Setter

	public int getIdCountry() {
		return idCountry;
	}

	public void setIdCountry(int idCountry) {
		this.idCountry = idCountry;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}



}
