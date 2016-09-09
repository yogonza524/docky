/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.beans;

import com.core.controller.Kimera;
import com.core.entities.Entry;
import com.core.enums.Tag;
import com.core.model.Component;
import com.core.util.HibernateUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    }

    public Entry getSelected() {
        return selected;
    }

    public void setSelected(Entry selected) {
        this.selected = selected;
    }
    
    public void onload(){
        if (!FacesContext.getCurrentInstance().isPostback()) {
            components = new ArrayList<>();
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
                    }
                    for(Attribute attr : elem.attributes()){
                        c.getParams().put(attr.getKey(), attr.getValue());
                    }
                    components.add(c);
                }
            }
        }
    }
    
}
