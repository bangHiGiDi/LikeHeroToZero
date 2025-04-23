package de.iubh.webanwendungen.likeherotozero.controller;

import de.iubh.webanwendungen.likeherotozero.dao.CountryDAO;
import de.iubh.webanwendungen.likeherotozero.dao.UserDAO;
import de.iubh.webanwendungen.likeherotozero.model.User;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Named
@SessionScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

    // Login & Registrierung
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String passwordConfirm;
    private int citizenshipId;

    private User loggedInUser;
    private Map<String, Integer> countryOptions = new LinkedHashMap<>();

    private UserDAO userDAO;

    @PostConstruct
    public void init() {
        userDAO = new UserDAO();

        try (CountryDAO dao = new CountryDAO()) {
            Map<Integer, String> countries = dao.getCountryNamesMap();
            for (Map.Entry<Integer, String> entry : countries.entrySet()) {
                countryOptions.put(entry.getValue(), entry.getKey());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public String getUserNameById(int userId) {
        UserDAO userDAO = new UserDAO();
        User user = userDAO.findById(userId);
        if (user != null) {
            return user.getFirstname() + " " + user.getLastname();
        }
        return "Unbekannt";
    }

    public String login() {
        User user = userDAO.findByEmail(email);

        if (user != null && user.getPasswordHash().equals(hashPassword(password))) {
            loggedInUser = user;
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Anmeldung erfolgreich!", null));
            
            user.setLastLogin(LocalDateTime.now());
            userDAO.update(user);
            
            return "dashboard.xhtml?faces-redirect=true";
        } else {
            addError("Login fehlgeschlagen: Ungültige E-Mail oder Passwort.");
            return null;
        }
    }

    public String register() {
        if (!password.equals(passwordConfirm)) {
            addError("Passwörter stimmen nicht überein.");
            return null;
        }

        if (userDAO.existsByEmail(email)) {
            addError("E-Mail ist bereits registriert.");
            return null;
        }

        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            addError("Bitte geben Sie eine gültige E-Mail-Adresse ein.");
            return null;
        }

        if (password.length() < 8) {
            addError("Das Passwort muss mindestens 8 Zeichen lang sein.");
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

    public String logout() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
        return null;
    }

    private void addError(String msg) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, null));
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

    public void checkAccess() {
        if (loggedInUser == null) {
            try {
                FacesContext.getCurrentInstance()
                        .getExternalContext()
                        .redirect("login.xhtml");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public boolean isAdmin() {
        return loggedInUser != null && loggedInUser.getPermisionLevel() == 1;
    }

    public boolean isScientist() {
        return loggedInUser != null && loggedInUser.getPermisionLevel() == 2;
    }

    public boolean isVisitor() {
        return loggedInUser != null && loggedInUser.getPermisionLevel() == 3;
    }

    public boolean isReviewer() {
        return loggedInUser != null && loggedInUser.getPermisionLevel() == 4;
    }
    
    public String getLoggedInUserCountryName() {
        if (loggedInUser == null) return "Unbekanntes Land";

        try (CountryDAO dao = new CountryDAO()) {
            return dao.getCountryNamesMap()
                    .getOrDefault(loggedInUser.getCitizenshipID(), "Unbekanntes Land");
        } catch (Exception e) {
            e.printStackTrace();
            return "Fehler beim Abrufen";
        }
    }

    // Getter und Setter
    public String getFirstname() {
    	return firstname; 
    }
    
    public void setFirstname(String firstname) {
    	this.firstname = firstname; 
    }

    public String getLastname() {
    	return lastname; 
    }
    
    public void setLastname(String lastname) { 
    	this.lastname = lastname; 
    }

    public String getEmail() { 
    	return email; 
    }
    
    public void setEmail(String email) { 
    	this.email = email; 
    }

    public String getPassword() { 
    	return password; 
    }
    
    public void setPassword(String password) { 
    	this.password = password; 
    }

    public String getPasswordConfirm() { 
    	return passwordConfirm; 
    }
    
    public void setPasswordConfirm(String passwordConfirm) { 
    	this.passwordConfirm = passwordConfirm; 
    }

    public int getCitizenshipId() { 
    	return citizenshipId; 
    }
    
    public void setCitizenshipId(int citizenshipId) { 
    	this.citizenshipId = citizenshipId; 
    }

    public Map<String, Integer> getCountryOptions() { 
    	return countryOptions; 
    }
}
