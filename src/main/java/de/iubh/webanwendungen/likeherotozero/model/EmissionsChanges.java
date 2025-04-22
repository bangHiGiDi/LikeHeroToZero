package de.iubh.webanwendungen.likeherotozero.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "EmissionsChanges")
public class EmissionsChanges implements Serializable {
   
	private static final long serialVersionUID = 1L;

	@Id
    private int idEmissionsChanges;

    private int co2EmissionsID;
    private int changedByUserID;
    private String fieldChange;
    private String oldValue;
    private String newValue;
    
	public int getIdEmissionsChanges() {
		return idEmissionsChanges;
	}
	public void setIdEmissionsChanges(int idEmissionsChanges) {
		this.idEmissionsChanges = idEmissionsChanges;
	}
	public int getCo2EmissionsID() {
		return co2EmissionsID;
	}
	public void setCo2EmissionsID(int co2EmissionsID) {
		this.co2EmissionsID = co2EmissionsID;
	}
	public int getChangedByUserID() {
		return changedByUserID;
	}
	public void setChangedByUserID(int changedByUserID) {
		this.changedByUserID = changedByUserID;
	}
	public String getFieldChange() {
		return fieldChange;
	}
	public void setFieldChange(String fieldChange) {
		this.fieldChange = fieldChange;
	}
	public String getOldValue() {
		return oldValue;
	}
	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}
	public String getNewValue() {
		return newValue;
	}
	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

    
}
