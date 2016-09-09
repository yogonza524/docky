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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.servlet.ServletContext;
import javax.servlet.http.Part;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
import org.primefaces.context.RequestContext;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author gonza
 */
@ManagedBean(name="home")
@SessionScoped
public class HomeBean {
    
    private Kimera k;
    private List<Project> projects;
    private Project newProject;
    private Entry entry;
    private String eid;
    private String pid;
    private UploadedFile file;
    private final String directory = "/usr/local/share/uploads";
    private List<StreamedContent> files;

    public List<StreamedContent> getFiles() {
        return files;
    }

    public void setFiles(List<StreamedContent> files) {
        this.files = files;
    }
 
    public UploadedFile getFile() {
        return file;
    }
 
    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }
    
    
    @ManagedProperty(value="#{viewEntry}")
    private ViewEntryBean viewEntry;

    public ViewEntryBean getViewEntry() {
        return viewEntry;
    }

    public void setViewEntry(ViewEntryBean viewEntry) {
        this.viewEntry = viewEntry;
    }

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
        try {
            listFiles();
        } catch (IOException ex) {
            showMessageError("List file error", ex.getMessage());
        }
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
            RequestContext.getCurrentInstance().update(s);
        }
    }
    
    public String formatDate(Date d){
        DateTime time = new DateTime(d);
        return time.getDayOfMonth() + "/" + (time.getMonthOfYear() < 10 ? "0" : "") + time.getMonthOfYear() + "/" + time.getYear() + " " + (time.getHourOfDay() < 9? "0" : "") + time.getHourOfDay() + ":" + (time.getMinuteOfHour() < 9? "0" : "") + time.getMinuteOfHour();
    }
    
    public void removeEntry(String pid, String eid, String name){
//        showMessageWarning("Not implemented", "Please wait to implementation");
        if (eid != null && !eid.isEmpty()) {
            EntryId id = new EntryId(eid, pid);
            List<Criterion> restrictions = new ArrayList<>();
            restrictions.add(Restrictions.eq("id.id", eid));
            restrictions.add(Restrictions.eq("id.idProject", pid));
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
    
    public List sort(Set set){
        List<Entry> list = new ArrayList<>(set);
        Collections.sort(list, new Comparator<Entry>() {
            @Override
            public int compare(Entry o1, Entry o2) {
                return o2.getDate().compareTo(o1.getDate());
            }
        });
        return list;
    }
    
    public void setParams(String pid, String eid){
        this.eid = eid;
        this.pid = pid;
    }
    
    public void loadEntry(String pid, String eid) throws IOException{
        setParams(pid, eid);
//        System.out.println(pid);
//        System.out.println(eid);
        if (pid != null && eid != null) {
            List<Criterion> restrictions = new ArrayList<>();
            restrictions.add(Restrictions.eq("id.id", eid));
            restrictions.add(Restrictions.eq("id.idProject", pid));
            Entry e = k.entityByRestrictions(restrictions, Entry.class);
//            System.out.println("Entry db: " + e);
            if (e != null) {
                this.entry = e;
//                e.setContent(replaceCarriage(e.getContent()));
                String path = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
                FacesContext.getCurrentInstance().getExternalContext().redirect(path + "/faces/pages/view_entry.xhtml?pid=" + pid + "&id=" + eid);
            }
            else{
                showMessageError("Entry not found", "Verify the ID");
            }
        }
        else{
            System.out.println("params null");
        }
//        onload();
    }
    
    public void updateView(){
        projects = k.all(Project.class);
//        update("projects-form");
    }
    
    public void upload() {
        if(file != null) {
            //create an InputStream from the uploaded file
            InputStream inputStr = null;
            try {
                inputStr = file.getInputstream();
            } catch (IOException e) {
                //log error
            }
            
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            String filename = FilenameUtils.getName(file.getFileName());
            File destFile = new File(directory, filename);

            //use org.apache.commons.io.FileUtils to copy the File
            try {
                FileUtils.copyInputStreamToFile(inputStr, destFile);
                showMessageSuccess("Success!", file.getFileName() + " is uploaded.");
                listFiles();
                update("list_files");
            } catch (IOException e) {
                //log error
                showMessageError("Upload exception", e.getMessage());
            }
        }
        else{
            System.out.println("File null!");
        }
    }
    
    public void listFiles() throws IOException{
        files = new ArrayList<>();
        listFilesForFolder(new File(directory));
        update("list_files");
    }
    
    public void listFilesForFolder(final File folder) throws IOException {
    for (final File fileEntry : folder.listFiles()) {
        if (fileEntry.isDirectory()) {
            listFilesForFolder(fileEntry);
        } 
        else {
            InputStream stream = new FileInputStream(new File(directory + "/" + fileEntry.getName()));
            StreamedContent f = new DefaultStreamedContent(stream, Files.probeContentType(fileEntry.toPath()), fileEntry.getName());
            files.add(f);
            }
        }
    }
    
    public String fileToURL(StreamedContent file) throws MalformedURLException{
        String output = "";
        if (file != null) {
            output = new File(directory + "/" + file.getName()).toURI().toURL().toString();
            System.out.println(output);
        }
        return output;
    }
    
    public StreamedContent loadImage(StreamedContent img) throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            // So, we're rendering the view. Return a stub StreamedContent so that it will generate right URL.
            return img;
        }
        return img;
    }
    
    public String subString(String name){
        String output = name;
        if (name != null && !name.isEmpty() && name.length() > 10) {
            output = name.substring(0,10) + "...";
        }
        return output;
    }
}
