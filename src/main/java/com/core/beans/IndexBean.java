/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.beans;

import com.core.enums.Tag;
import com.core.model.Component;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.context.RequestContext;

/**
 *
 * @author gonzalo
 */
@ManagedBean(name="index")
@ViewScoped
public class IndexBean {
    
    private List<Component> components;
    private Tag[] tags;
    private Object aux;

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
            System.out.println("\n" + c.getParams().get(key) + " - " + key + ": " + value + "\n");
            c.getParams().put(key, value);
            Collections.replaceAll(components, c, c);
            RequestContext.getCurrentInstance().update("preview-form");
        }
        else{
              showMessage("Error", "Component is empty");
        }
    }
    
    private void showMessage(String title, String message){
        RequestContext.getCurrentInstance().execute("toastr.success('" + title + "', '" + message + "', {timeOut: 5000});");
                
    }
    
    public void newEntry(){
        this.components = new ArrayList<>();
        this.aux = null;
        RequestContext.getCurrentInstance().update("preview-form");
        RequestContext.getCurrentInstance().update("components-form");
    }
}
