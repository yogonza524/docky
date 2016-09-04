/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.beans;

import com.core.controller.Kimera;
import com.core.entities.Entry;
import com.core.entities.EntryId;
import com.core.entities.Project;
import com.core.util.HibernateUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
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
    
    public void addProject(){
        if (newProject.getName() != null && !newProject.getName().isEmpty()) {
            if (k.add(newProject)) {
                newProject = new Project();
                showMessageSuccess("Added", "Congratulations!");
                projects = k.all(Project.class);
                RequestContext.getCurrentInstance().update("new_project_form");
                RequestContext.getCurrentInstance().update("projects-form");
                RequestContext.getCurrentInstance().execute("$('#projects_link').trigger('click')");
            }
            else{
                showMessageError("Problems adding", "Your project doesn't added");
            }
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
    
    private void update(String... component){
        for(String s : component){
            System.out.println("Updating: " + s);
            RequestContext.getCurrentInstance().update(s);
        }
    }
    
    public String formatDate(Date d){
        DateTime time = new DateTime(d);
        return time.getDayOfMonth() + "/" + (time.getMonthOfYear() < 10 ? "0" : "") + time.getMonthOfYear() + "/" + time.getYear();
    }
    
    public void removeEntry(String pid, String eid, String name){
//        showMessageWarning("Not implemented", "Please wait to implementation");
        if (eid != null && !eid.isEmpty()) {
            EntryId id = new EntryId(eid, pid);
            List<Criterion> restrictions = new ArrayList<>();
            restrictions.add(Restrictions.eq("id", eid));
            restrictions.add(Restrictions.eq("id_project", pid));
            Entry e = k.entityByRestrictions(restrictions, Entry.class);
            if (e != null) {
                if (k.remove(e)) {
                    showMessageSuccess("Removed", "The entry was killed");
                    projects = k.all(Project.class);
                    update("projects-form");
                }
                else{
                    showMessageError("Problems removing", "Intern error");
                }
            }
            else{
                showMessageError("Entry to remove not found", "Please select a entry");
            }
        }
        else{
            showMessageError("Entry ID is empty", "Please put an ID");
        }
    }
    
    public void deleteProject(String pid){
        if (pid != null && !pid.isEmpty()) {
            Project pro = k.entityById("id", pid, Project.class);
            if (pro != null) {
                if (k.remove(pro)) {
                    showMessageSuccess("Removed", "The project was killed");
                    projects = k.all(Project.class);
                    update("projects-form");
                }
                else{
                    showMessageError("Problems removing", "Intern error");
                }
            }
            else{
                showMessageError("Project to remove not found", "Please select a project");
            }
        }
        else{
            showMessageError("Project ID is empty", "Please put an PID");
        }
    }
}
