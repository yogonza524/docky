/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.beans;

import com.core.controller.Kimera;
import com.core.entities.Entry;
import com.core.entities.EntryId;
import com.core.enums.Tag;
import com.core.model.Component;
import com.core.util.HibernateUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.primefaces.context.RequestContext;

/**
 *
 * @author gonza
 */
@ManagedBean(name="edit")
@ViewScoped
public class EditEntryBean {
    
    private Entry selected;
    private Kimera k;
    private String idProject;
    private String idEntry;
    private List<Component> components;
    private String aux;

    public String getAux() {
        return aux;
    }

    public void setAux(String aux) {
        this.aux = aux;
    }

    public List<Component> getComponents() {
        return components;
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public String getIdProject() {
        return idProject;
    }

    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }

    public String getIdEntry() {
        return idEntry;
    }

    public void setIdEntry(String idEntry) {
        this.idEntry = idEntry;
    }
    
    @PostConstruct
    public void init(){
        k = new Kimera(HibernateUtil.getSessionFactory());
        components = new ArrayList<>();
    }

    public Entry getSelected() {
        return selected;
    }

    public void setSelected(Entry selected) {
        this.selected = selected;
    }
    
    public void onload(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            List<Criterion> restrictions = new ArrayList<>();
            restrictions.add(Restrictions.eq("id.id", idEntry));
            restrictions.add(Restrictions.eq("id.idProject", idProject));
            Entry e = k.entityByRestrictions(restrictions, Entry.class);
            if (e != null) {
                selected = e;
                Document doc = Jsoup.parseBodyFragment(e.getContent());
                Iterator<Element> i = doc.body().children().select("*").listIterator();
                while(i.hasNext()){
                    Element elem = i.next();
                    String tag = elem.tagName();
                    Component c = new Component();
                    c.setValue(elem.text());
                    switch(tag){
                        case "img": 
                            c.setTag(Tag.Image);
                            c.setValue(elem.attr("src"));
                            ;break;
                        case "p" : 
                            c.setTag(Tag.Paragraph);
                            ;break;
                        case "h1":
                            c.setTag(Tag.Header1);
                            ;break;
                        case "h2":
                            c.setTag(Tag.Header2);
                            ;break;
                        case "h3":
                            c.setTag(Tag.Header3);
                            ;break;
                        case "h4":
                            c.setTag(Tag.Header4);
                            ;break;
                        case "pre":
                            c.setTag(Tag.Code);
                            c.setValue(elem.children().html());
                            ;break;
                        case "a":
                            c.setTag(Tag.Link);
                            ;break;
                    }
                    for(Attribute attr : elem.attributes()){
                        c.getParams().put(attr.getKey(), attr.getValue());
                    }
                    components.add(c);
                }
                update("components-form");
            }
            else{
                showMessageError("Error finding entry","Entry not found in DB");
            }
        }
    }
    
    private void update(String... component){
        for(String s : component){
            RequestContext.getCurrentInstance().update(s);
        }
    }
    
    public void add(String component){
        Tag t = Tag.valueOf(component);
        components.add(new Component(t, ""));
        RequestContext.getCurrentInstance().update("components-form");
        RequestContext.getCurrentInstance().execute("$('#callout-components').scrollTop($('#callout-components')[0].scrollHeight);");
        RequestContext.getCurrentInstance().execute("$('.input_c:last:first').focus()");
    }
    
    public void updateEntry(String content) throws IOException{
        if (selected != null) {
            selected.setContent(content);
            if (k.update(selected)) {
                components = new ArrayList<>();
                String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().getExternalContext().redirect(path);
            }
            else{
                showMessageError("Problems updating", "The server doesn't save the entry");
            }
        }
        else{
            showMessageError("Empty entry", "Please select an entry");
        }
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
    
    public void update(Component c){
        Collections.replaceAll(components, c, c);
        RequestContext.getCurrentInstance().update("preview-form");
    }
    
    public void remove(Component c){
        components.remove(c);
        RequestContext.getCurrentInstance().update("components-form");
        RequestContext.getCurrentInstance().update("preview-form");
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
    
    public void focusJQueryComponent(String comp){
        RequestContext.getCurrentInstance().execute("$('" + comp +"').focus();");
    }
}
