/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sleuthkit.autopsy.modules.tagfilter;

import org.sleuthkit.datamodel.Content;

/**
 *
 * @author root
 */
public class DataSourceCBWrapper {
	private Content content;
	private String customName;
	
	public DataSourceCBWrapper(Content pContent){
		this(pContent, "");
	}
	
	public DataSourceCBWrapper(Content pContent, String pCustomName){
		content = pContent;
		customName = pCustomName;
	}
	
	public Content getContent(){
		return content;
	}
	
	public String getCustomName(){
		return customName;
	}
	
	@Override
	public String toString(){
		return !customName.equals("")?customName:content.getName();
	}
}
