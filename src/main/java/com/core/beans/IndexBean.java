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
import com.core.enums.Tag;
import com.core.model.Component;
import com.core.util.HibernateUtil;
import java.io.IOException;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.context.RequestContext;

/**
 *
 * @author gonzalo
 */
@ManagedBean(name="index")
@SessionScoped
public class IndexBean {
    
    private List<Component> components;
    private Tag[] tags;
    private Object aux;
    private String title;
    private String pid;
    private Project p;
    private Kimera k;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Object getAux() {
        return aux;
    }

    public void setAux(Object aux) {
        this.aux = aux;
    }

    public Tag[] getTags() {
        return tags;
    }

    public void setTags(Tag[] tags) {
        this.tags = tags;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }
    
    @PostConstruct
    public void init(){
        components = new ArrayList<>();
        tags = Tag.values();
        title = "";
        k = new Kimera(HibernateUtil.getSessionFactory());
        findProject();
    }
    
    public void add(String component){
        Tag t = Tag.valueOf(component);
        components.add(new Component(t, ""));
        RequestContext.getCurrentInstance().update("components-form");
    }
    
    public void remove(Component c){
        components.remove(c);
        RequestContext.getCurrentInstance().update("components-form");
        RequestContext.getCurrentInstance().update("preview-form");
    }
    
    public void update(Component c){
        Collections.replaceAll(components, c, c);
        RequestContext.getCurrentInstance().update("preview-form");
    }
    
    public void setParam(Component c, String key, String value){
        if (c != null) {
//            System.out.println("\n" + c.getParams().get(key) + " - " + key + ": " + value + "\n");
            c.getParams().put(key, value);
            Collections.replaceAll(components, c, c);
            RequestContext.getCurrentInstance().update("preview-form");
        }
        else{
              showMessageError("Error", "Component is empty");
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
    
    public void newEntry(){
        this.components = new ArrayList<>();
        this.aux = null;
        RequestContext.getCurrentInstance().update("preview-form");
        RequestContext.getCurrentInstance().update("components-form");
    }
    
    public void save(String title, String content) throws IOException, InterruptedException{
        if (title != null && !title.isEmpty()) {
            if (content != null && !content.isEmpty()) {
                Entry e = new Entry();
                if (p != null) {
                    EntryId id = new EntryId();
                    id.setIdProject(p.getId());
                    e.setId(id);
                    e.setDate(new Date());
                    e.setTitle(title);
                    e.setContent(content);
                    if (k.add(e)) {
                        p = null;
                        title = "";
                        components = new ArrayList<>();
                        RequestContext.getCurrentInstance().update("components-form");
                        RequestContext.getCurrentInstance().update("title-form");
                        RequestContext.getCurrentInstance().update("preview-form");
                        showMessageSuccess("Great!", "New entry stored");
                        Thread.sleep(1000);
                        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                        FacesContext.getCurrentInstance().getExternalContext().redirect(path);
                    }
                    else{
                        showMessageError("Problems adding entry", "Intern error");
                    }
                }else{
                    showMessageError("Project empty", "I need a project ID");
                }
            }
            else{
                showMessageError("Bad content", "Please insert some content");
            }
        }
        else{
            showMessageError("Title is mandatory", "Please put a title");
            focusJQueryComponent("input[id=\"title-form:title\"]");
        }
    }
    
    public void focusJQueryComponent(String comp){
        RequestContext.getCurrentInstance().execute("$('" + comp +"').focus();");
    }
    
    public void findProject(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            if (pid != null && !pid.isEmpty()) {
                p = k.entityById("id", pid, Project.class);
                if (p != null) {
                    //your code here
                    showMessageSuccess("Project setted", "Success");
                }
                else{
                    showMessageError("The project doesn't exist", "Checkout the PID");
                }
            }
            else{
                showMessageError("Project ID is mandatory", "Please send the PID");
            }
        }
    }
    
    public void view(Project project) throws IOException{
        this.p = project;
//        System.out.println("Project: " + project.getName());
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        FacesContext.getCurrentInstance().getExternalContext().redirect(path + "/faces/pages/new_entry.xhtml?pid=" + p.getId());
    }
}
