/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.util.actions.Presenter;

/**
 *
 * @author dq
 */
@ActionID(category = "Tools",
id = "com.gas.main.ui.actions.license.LicenseEmptyAction")
@ActionRegistration(displayName = "#CTL_LicenseEmptyAction")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 3450)
})
@NbBundle.Messages("CTL_LicenseEmptyAction=License")
public class LicenseEmptyAction extends AbstractAction implements Presenter.Toolbar{

    private JPopupMenu popup;
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Component getToolbarPresenter() {
        final JToggleButton ret = new JToggleButton();
        ret.setIcon(ImageHelper.createImageIcon(ImageNames.KEY_24));
        ret.setText("License");
                ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);
        
        ret.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    /** show popup menu on toggleButton at position: (0, height) */
                    popup.show(ret, 0, ret.getHeight());
                }
            }
        });
        
        popup = new JPopupMenu();
        popup.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                ret.setSelected(false);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                ret.setSelected(false);
            }
        });
        
        popup.add(new RequestTrialAction());
        popup.addSeparator();
        popup.add(new GenerateRegisAction());
        popup.add(new ImportLicenseAction());        
        popup.add(new ReleaseAction());
        popup.addSeparator();
        popup.add(new BuyAction());
        
        return ret;
    }
    
}
