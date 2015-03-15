/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.toolbar;

import com.gas.domain.ui.ToolbarSpacer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

@ActionID(category = "Tools",
id = "com.gas.main.ui.actions.toolbar.BeforeCloneSpacer")
@ActionRegistration(displayName = "#CTL_BeforeCloneSpacer")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 3300)
})
@NbBundle.Messages("CTL_BeforeCloneSpacer=")
public class BeforeCloneSpacer extends ToolbarSpacer {
    
}
