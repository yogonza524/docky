/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.beans;

import com.core.controller.Kimera;
import com.core.entities.Entry;
import com.core.util.HibernateUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.primefaces.context.RequestContext;

/**
 *
 * @author gonza
 */
@ManagedBean(name="viewEntry")
@SessionScoped
public class ViewEntryBean {
    
    private String eid;
    private String pid;
    private Entry entry;
    private Kimera k;

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }
    
    @PostConstruct
    public void init(){
        k = new Kimera(HibernateUtil.getSessionFactory());
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
    
    public void update(String... component){
        for(String s : component){
            RequestContext.getCurrentInstance().update(s);
        }
    }
    
    public void onload(){
        System.out.println(pid);
        System.out.println(eid);
        if (pid != null && eid != null) {
            List<Criterion> restrictions = new ArrayList<>();
            restrictions.add(Restrictions.eq("id.id", eid));
            restrictions.add(Restrictions.eq("id.idProject", pid));
            Entry e = k.entityByRestrictions(restrictions, Entry.class);
            System.out.println("Entry db: " + e);
            if (e != null) {
                this.entry = e;
            }
            else{
                showMessageError("Entry not found", "Verify the ID");
            }
        }
        else{
            System.out.println("params null");
        }
    }
    
    public void setParams(String pid, String eid){
        this.eid = eid;
        this.pid = pid;
    }
    
    public void loadEntry(String pid, String eid) throws IOException{
        setParams(pid, eid);
        String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        FacesContext.getCurrentInstance().getExternalContext().redirect(path + "/faces/pages/view_entry.xhtml?pid=" + pid + "&id=" + eid);
//        onload();
    }
    
}
