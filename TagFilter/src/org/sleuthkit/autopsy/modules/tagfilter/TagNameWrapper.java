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
public class TagNameWrapper {
	private TagName tagName;
	
	public TagNameWrapper(TagName pTagName){
		tagName = pTagName;
	}

	@Override
	public String toString() {
		return tagName.getDisplayName();
	}
	
	public TagName getTagName(){
		return tagName;
	}
	
	
}
