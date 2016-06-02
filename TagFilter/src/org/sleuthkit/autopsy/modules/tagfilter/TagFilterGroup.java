/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sleuthkit.autopsy.modules.tagfilter;

import java.util.ArrayList;
import org.sleuthkit.autopsy.modules.tagfilter.TagFilter.FilterOperator;
import org.sleuthkit.autopsy.modules.tagfilter.TagFilter.FilterType;
import org.sleuthkit.datamodel.TagName;

/**
 *
 * @author root
 */
public class TagFilterGroup extends AbstractTagFilter{
	private FilterOperator filterOperator = FilterOperator.AND;
	private boolean active = true;
	private ArrayList<TagFilter> tagFilters;
	
	public TagFilterGroup(){
		tagFilters = new ArrayList<>();
		active = true;
	}
	
	public TagFilterGroup(FilterOperator pFilterOperator){
		tagFilters = new ArrayList<>();
		filterOperator = pFilterOperator;
		active = true;
	}

	public FilterOperator getFilterOperator() {
		return filterOperator;
	}

	public void setFilterOperator(FilterOperator filterOperator) {
		this.filterOperator = filterOperator;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public ArrayList<TagFilter> getTagFilters(){
		return tagFilters;
	}
	
	public void addTagFilter(TagFilter pTagFilter){
		tagFilters.add(pTagFilter);
	}
	
	public void removeTagFilter(TagFilter pTagFilter){
		tagFilters.remove(pTagFilter);
	}
}
