/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sleuthkit.autopsy.modules.tagfilter;

import org.openide.util.HelpCtx;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * @author root
 */
public class TagFilterConfigurationAction extends CallableSystemAction {
	private static final String ACTION_NAME = org.openide.util.NbBundle.getMessage(TagFilterToolbar.class, "TagFilterConfig");
    private TagFilterConfiguration panel;//TODO ändern zur Filter Klasse (Panel)

    @Override
    public void performAction() {
        final TagFilterConfiguration panel = getPanel();
       
    }

    private TagFilterConfiguration getPanel() {
        if (panel == null) {
            panel = new TagFilterConfiguration();
        }
        return panel;
    }

    @Override
    public String getName() {
        return ACTION_NAME;
    }

    @Override
    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
