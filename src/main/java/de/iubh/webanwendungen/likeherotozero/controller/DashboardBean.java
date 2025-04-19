package de.iubh.webanwendungen.likeherotozero.controller;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

@Named
@RequestScoped
public class DashboardBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private LoginBean loginBean;
    @PostConstruct
    public void init() {
    	
    }
 public void test(){
	   System.out.println(loginBean.getLoggedInUser());
}

}