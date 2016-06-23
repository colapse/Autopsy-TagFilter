/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sleuthkit.autopsy.modules.tagfilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.lookup.Lookups;
import org.sleuthkit.autopsy.datamodel.FileNode;
import org.sleuthkit.autopsy.datamodel.FilterNodeLeaf;
import org.sleuthkit.autopsy.datamodel.KeyValue;
import org.sleuthkit.autopsy.datamodel.KeyValueNode;
import org.sleuthkit.datamodel.AbstractFile;
import org.sleuthkit.datamodel.Content;

/**
 *
 * @author root
 */
public class TagFilterResultFactory extends ChildFactory<AbstractFile> {
	private ArrayList<AbstractFile> abstractFiles;
	
	public TagFilterResultFactory(ArrayList<AbstractFile> pAbstractFiles){
		abstractFiles = pAbstractFiles;
	}
	
	@Override
	protected boolean createKeys(List<AbstractFile> list) {
		for(AbstractFile af : abstractFiles){
			if(!list.contains(af))
			list.add(af);
		}
		//list.addAll(abstractFiles);
		return true;
	}
	
	
	@Override
	protected Node createNodeForKey(AbstractFile key) {
		return new FilterNodeLeaf(new FileNode(key, false));/*
		Node kvNode = new KeyValueNode(new KeyValueAbstractFile(key), Children.LEAF);
		
		return kvNode;*/
	}
	
	/**
	* Used to display keyword search results in table. Eventually turned into a
	* node.
	*/
       class KeyValueAbstractFile extends KeyValue {

	   private AbstractFile abstractFile;

	   public KeyValueAbstractFile(AbstractFile pFile) {
		super(pFile.getName(), (int)pFile.getId());
		
		abstractFile = pFile;
	       
	   }
	   
		public AbstractFile getAbstractFile(){
		   return abstractFile;
		}
	  
       }
	
	
	
}
