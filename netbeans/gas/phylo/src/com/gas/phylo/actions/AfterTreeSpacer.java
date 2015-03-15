/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.phylo.actions;

import com.gas.domain.ui.ToolbarSpacer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

@ActionID(category = "Tools",
id = "com.gas.phylo.actions.AfterTreeSpacer")
@ActionRegistration(displayName = "#CTL_AfterTreeSpacer")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 3355)
})
@NbBundle.Messages("CTL_AfterTreeSpacer=")
public class AfterTreeSpacer extends ToolbarSpacer{
    
}
