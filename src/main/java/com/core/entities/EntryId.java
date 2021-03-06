package com.core.entities;
// Generated 03/09/2016 23:20:16 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * EntryId generated by hbm2java
 */
@Embeddable
public class EntryId  implements java.io.Serializable {


     private String id;
     private String idProject;

    public EntryId() {
    }

    public EntryId(String id, String idProject) {
       this.id = id;
       this.idProject = idProject;
    }
   


    @Column(name="id", nullable=false, length=64)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }


    @Column(name="id_project", nullable=false, length=64)
    public String getIdProject() {
        return this.idProject;
    }
    
    public void setIdProject(String idProject) {
        this.idProject = idProject;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof EntryId) ) return false;
		 EntryId castOther = ( EntryId ) other; 
         
		 return ( (this.getId()==castOther.getId()) || ( this.getId()!=null && castOther.getId()!=null && this.getId().equals(castOther.getId()) ) )
 && ( (this.getIdProject()==castOther.getIdProject()) || ( this.getIdProject()!=null && castOther.getIdProject()!=null && this.getIdProject().equals(castOther.getIdProject()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getId() == null ? 0 : this.getId().hashCode() );
         result = 37 * result + ( getIdProject() == null ? 0 : this.getIdProject().hashCode() );
         return result;
   }   


}


