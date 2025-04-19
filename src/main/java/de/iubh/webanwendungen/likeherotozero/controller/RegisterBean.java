package de.iubh.webanwendungen.likeherotozero.controller;

import de.iubh.webanwendungen.likeherotozero.dao.CountryDAO;
import de.iubh.webanwendungen.likeherotozero.dao.UserDAO;
import de.iubh.webanwendungen.likeherotozero.model.User;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedHashMap;
import java.util.Map;

@Named
@RequestScoped
public class RegisterBean implements Serializable {

    private static final long serialVersionUID = 1L;
    
	private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String passwordConfirm;
    private int citizenshipId;

    private Map<String, Integer> countryOptions = new LinkedHashMap<>();

    private CountryDAO countryDAO;
    private UserDAO userDAO;

    @PostConstruct
    public void init() {
        countryDAO = new CountryDAO();
        userDAO = new UserDAO();

        Map<Integer, String> countries = countryDAO.getCountryNamesMap();
        for (Map.Entry<Integer, String> entry : countries.entrySet()) {
            countryOptions.put(entry.getValue(), entry.getKey());
        }
    }

    public String register() {
        if (!password.equals(passwordConfirm)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwörter stimmen nicht überein.", null));
            return null;
        }

        if (userDAO.existsByEmail(email)) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "E-Mail ist bereits registriert.", null));
            return null;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Bitte geben Sie eine gültige E-Mail-Adresse ein.", null));
            return null;
        }


        if (password.length() < 8) {
            FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Das Passwort muss mindestens 8 Zeichen lang sein.", null));
            return null;
        }

        User user = new User();
        user.setFirstname(firstname);
        user.setLastname(lastname);
        user.setEmail(email);
        user.setPasswordHash(hashPassword(password));
        user.setCitizenshipID(citizenshipId);

        userDAO.save(user);

        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_INFO, "Registrierung erfolgreich!", null));
        return "login.xhtml?faces-redirect=true";
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Fehler beim Hashen des Passworts", e);
        }
    }

    // Getter und Setter
    public String getFirstname() { return firstname; }
    public void setFirstname(String firstname) { this.firstname = firstname; }

    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPasswordConfirm() { return passwordConfirm; }
    public void setPasswordConfirm(String passwordConfirm) { this.passwordConfirm = passwordConfirm; }

    public int getCitizenshipId() { return citizenshipId; }
    public void setCitizenshipId(int citizenshipId) { this.citizenshipId = citizenshipId; }

    public Map<String, Integer> getCountryOptions() {
        return countryOptions;
    }
}