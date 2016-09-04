/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.beans;

import com.core.controller.Kimera;
import com.core.entities.Project;
import com.core.util.HibernateUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author gonza
 */
@ManagedBean(name="home")
@ViewScoped
public class HomeBean {
    
    private Kimera k;
    private List<Project> projects;
    private Project newProject;

    public Project getNewProject() {
        return newProject;
    }

    public void setNewProject(Project newProject) {
        this.newProject = newProject;
    }
    
    @PostConstruct
    public void init(){
        k = new Kimera(HibernateUtil.getSessionFactory());
        projects = k.all(Project.class);
        newProject = new Project();
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
    
    public void addEntry(){
        if (newProject.getName() != null && !newProject.getName().isEmpty()) {
            
        }
        else{
            showMessageError("Name is mandatory", "Please insert a name");
        }
    }
    
    private void showMessageSuccess(String title, String message){
        RequestContext.getCurrentInstance().execute("toastr.options.positionClass = 'toast-bottom-right';toastr.success('" + title + "', '" + message + "', {timeOut: 5000});");        
    }
    
    private void showMessageWarning(String title, String message){
        RequestContext.getCurrentInstance().execute("toastr.options.positionClass = 'toast-bottom-right';toastr.warning('" + title + "', '" + message + "', {timeOut: 5000});");        
    }
    
    private void showMessageError(String title, String message){
        RequestContext.getCurrentInstance().execute("toastr.options.positionClass = 'toast-bottom-right';toastr.error('" + title + "', '" + message + "', {timeOut: 5000});");        
    }
}
