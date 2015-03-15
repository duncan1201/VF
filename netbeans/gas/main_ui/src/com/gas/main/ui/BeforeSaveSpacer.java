/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.domain.ui.ToolbarSpacer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;

/**
 *
 * @author dq
 */
@ActionID(category = "Tools",
id = "com.gas.main.ui.BeforeSaveSpacer")
@ActionRegistration(displayName = "#CTL_BeforeSaveSpacer")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 290)
})
@NbBundle.Messages("CTL_BeforeSaveSpacer=")
public class BeforeSaveSpacer extends ToolbarSpacer {
    public BeforeSaveSpacer(){
        super(false);
    }
}
