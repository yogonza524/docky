/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.core.beans;

import com.core.entities.Entry;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.data.FilterEvent;

/**
 *
 * @author gonza
 */
@ManagedBean(name = "entryFilter")
@SessionScoped
public class FilterEntryBean implements Serializable{
    private Map<String, Object> filterState = new HashMap<>();
    private List<Entry> filteredValue;

    public void onFilterChange(FilterEvent filterEvent) {
        filterState = filterEvent.getFilters();
        filteredValue =(List<Entry>) filterEvent.getData();
    }

    public Object filterState(String column) {
        return filterState.get(column);
    }

    public List<Entry> getFilteredValue() {
        return filteredValue;
    }

    public void setFilteredValue(List<Entry> filteredValue) {
        this.filteredValue = filteredValue;
    }
}
