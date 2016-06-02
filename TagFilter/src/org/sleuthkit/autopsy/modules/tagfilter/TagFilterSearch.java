/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sleuthkit.autopsy.modules.tagfilter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.sleuthkit.autopsy.casemodule.Case;
import org.sleuthkit.autopsy.corecomponents.DataResultTopComponent;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.Content;
import org.sleuthkit.datamodel.SleuthkitCase;
import org.sleuthkit.datamodel.TskCoreException;

/**
 *
 * @author root
 */
public class TagFilterSearch {
	private ArrayList<AbstractTagFilter> tagFilters;
	private Content datasourceFilter = null;
	private boolean dbSearchJobRunning;
	private ArrayList<AbstractFile> results;
	
	
	public TagFilterSearch(List<AbstractTagFilter> pTagFilters){
		this(pTagFilters, null);
	}
	
	public TagFilterSearch(List<AbstractTagFilter> pTagFilters, Content pDatasource){
		tagFilters = new ArrayList<>(pTagFilters);
		results = new ArrayList<>();
		datasourceFilter = pDatasource;
	}
	
	private void executeSearch(){
		results.clear();
		try {
			SleuthkitCase.CaseDbQuery dbQuery = Case.getCurrentCase().getSleuthkitCase().executeQuery(createDbQuery());
			ResultSet rs = dbQuery.getResultSet();
			while (rs.next()) {
				int obj_id = rs.getInt("obj_id");
				AbstractFile af = Case.getCurrentCase().getSleuthkitCase().getAbstractFileById(obj_id);
				
				if(af != null && !results.contains(af)){
					results.add(af);
				}
			}
			
		} catch (TskCoreException | SQLException ex) {
			Exceptions.printStackTrace(ex);
		}
		
		
		
		System.out.println(">>>>>>>>>  Query: \n\r"+createDbQuery());
		System.out.println(">>>>>>>>>  Query Results: "+results.size());
		
		

	}
	
	private void createTopComponentPanel(){
		DataResultTopComponent searchResultWin = DataResultTopComponent.createInstance("Tag Filter");
		
		Node rootNode;
		if (results.size() > 0) {
			TagFilterResultFactory tfrFactory = new TagFilterResultFactory(results);
		    Children childNodes
			    = Children.create(tfrFactory, true);

		    rootNode = new AbstractNode(childNodes);
		} else {
		    rootNode = Node.EMPTY;
		}

		DataResultTopComponent.initInstance("Filter: "+createPathText(), rootNode, 10, searchResultWin);
	}
	
	private String createDbQuery(){
		//String dbQuery = "SELECT tf.obj_id FROM tsk_files as tf WHERE 1=1";// 
		String dbQuery = "SELECT tf.obj_id FROM tsk_files as tf INNER JOIN content_tags as ct ON tf.obj_id = ct.obj_id WHERE 1=1";
		
		if(datasourceFilter != null){
			dbQuery += " AND data_source_obj_id = '"+datasourceFilter.getId()+"' AND (1=1";
		}
		
		int filterCounter = 0;
		for(AbstractTagFilter filter : tagFilters){
			if(filter instanceof TagFilter){
				TagFilter tf = (TagFilter) filter;
				
				switch(tf.getFilterOperator()){
					case OR:
						dbQuery += " OR";
						break;
					case AND:
					default:
						dbQuery += " AND";
						break;
				}
				
				//dbQuery += " (SELECT COUNT(*) FROM content_tags as ct WHERE tf.obj_id = ct.obj_id AND tag_name_id = '"+tf.getTagName().getId()+"')";
				dbQuery += " ct.tag_name_id";
				switch(tf.getFilterType()){
					case CONTAINS_NOT:
						dbQuery += " !=";
						break;
					case CONTAINS:
					default:
						dbQuery += " =";
						break;
				}
				dbQuery += " '"+tf.getTagName().getId()+"'";
				
			}else if(filter instanceof TagFilterGroup){
				TagFilterGroup tfg = (TagFilterGroup) filter;
				
				if(tfg.getTagFilters().isEmpty()){
					continue;
				}
				
				switch(tfg.getFilterOperator()){
					case OR:
						dbQuery += " OR";
						break;
					case AND:
					default:
						dbQuery += " AND";
						break;
				}
				dbQuery += " (";
				
				int tgFilterCounter = 0;
				for(TagFilter tagFilter : tfg.getTagFilters()){
					if(tgFilterCounter > 0){
					switch(tagFilter.getFilterOperator()){
						case OR:
							dbQuery += " OR";
							break;
						case AND:
						default:
							dbQuery += " AND";
							break;
					}
					}

					//dbQuery += " (SELECT COUNT(ct.*) FROM content_tags as ct WHERE tf.obj_id = ct.obj_id WHERE tag_name_id = '"+tagFilter.getTagName().getId()+"')";

					dbQuery += " ct.tag_name_id";
					switch(tagFilter.getFilterType()){
						case CONTAINS_NOT:
							dbQuery += " !=";
							break;
						case CONTAINS:
						default:
							dbQuery += " =";
							break;
					}
					dbQuery += " '"+tagFilter.getTagName().getId()+"'";
					
					tgFilterCounter++;
				}
				
				dbQuery += " )";
			}
			filterCounter++;
		}
		
		if(datasourceFilter != null){
			dbQuery += ")";
		}
		
		return dbQuery;
	}
	
	public String createPathText(){
		String pathText = "";
		int filterCounter = 0;
		for(AbstractTagFilter filter : tagFilters){
			if(filter instanceof TagFilter){
				TagFilter tf = (TagFilter) filter;
				if(filterCounter != 0){
					pathText += " "+tf.getFilterOperator();
				}
				pathText += "File "+tf.getFilterType()+" Tag \""+tf.getTagName().getDisplayName()+"\"";
			}else if(filter instanceof TagFilterGroup){
				TagFilterGroup tfg = (TagFilterGroup) filter;
				
				if(tfg.getTagFilters().isEmpty()){
					continue;
				}
				
				if(filterCounter != 0){
					pathText += " "+tfg.getFilterOperator();
				}
				pathText += " (";
				
				//Add Filters of Filtergroup
				int tgFilterCounter = 0;
				for(TagFilter tagFilter : tfg.getTagFilters()){
					if(tgFilterCounter != 0){
						pathText += " "+tagFilter.getFilterOperator();
					}
					pathText += "File "+tagFilter.getFilterType()+" Tag \""+tagFilter.getTagName().getDisplayName()+"\"";
					tgFilterCounter++;
				}
				
				pathText += " )";
			}
			filterCounter++;
		}
		
		return pathText;
	}
	
	public void executeDbSearchJob() {
		if(!dbSearchJobRunning){
			dbSearchJobRunning = true;
			new Thread() {
			    @Override
			    public void run() {
				executeSearch();
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						executeDbSearchJobCallback();
					}
				});
			    }
			}.start();
		}
	}
	
	private void executeDbSearchJobCallback(){
		createTopComponentPanel();
	}
	
	
}
