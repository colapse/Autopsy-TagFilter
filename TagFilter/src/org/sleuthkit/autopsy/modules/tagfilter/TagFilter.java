/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sleuthkit.autopsy.modules.tagfilter;

import org.sleuthkit.datamodel.TagName;

/**
 *
 * @author root
 */
public class TagFilter extends AbstractTagFilter{
	public static enum FilterType{CONTAINS("Contains"), CONTAINS_NOT("Doesn't Contain");
		private final String name;

		private FilterType(final String name) {
		    this.name = name;
		}

		@Override
		public String toString() {
		    return name;
		}
	};
	public static enum FilterOperator{AND("AND") , OR("OR");
		private final String name;

		private FilterOperator(final String name) {
		    this.name = name;
		}

		@Override
		public String toString() {
		    return name;
		}};
	
	private TagName tagName = null;
	private FilterType filterType = FilterType.CONTAINS;
	private FilterOperator filterOperator = FilterOperator.AND;
	private boolean active = true;
	
	public TagFilter(){
		active = false;
	}
	
	public TagFilter(TagName pTagName, FilterType pFilterType, FilterOperator pFilterOperator){
		tagName = pTagName;
		filterType = pFilterType;
		filterOperator = pFilterOperator;
		active = true;
	}
	
	public TagName getTagName() {
		return tagName;
	}

	public void setTagName(TagName tagName) {
		this.tagName = tagName;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public void setFilterType(FilterType filterType) {
		this.filterType = filterType;
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
	
	
	
}
