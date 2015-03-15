/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.main.ui.AboutAction;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractAction;
import javax.swing.Action;
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

@ActionID(category = "File",
        id = "com.gas.main.ui.actions.HelpEmptyActions")
@ActionRegistration(displayName = "#CTL_HelpEmptyActions")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 3460)
})
@NbBundle.Messages("CTL_HelpEmptyActions=Help")
public class HelpEmptyActions extends AbstractAction implements Presenter.Toolbar {

    private JPopupMenu popup;

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Component getToolbarPresenter() {
        JToggleButton ret = new JToggleButton(ImageHelper.createImageIcon(ImageNames.INFO_24));
        ret.setText("Help");
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);
        popup = createPopupMenu(ret);
        
        popup.add(new PreferencesAction());
        
        Action action = new ResourcesAction();
        popup.add(action);
        
        action = new Check4UpdatesAction();
        popup.add(action);

        action = new AboutAction();
        popup.add(action);

        return ret;
    }

    private JPopupMenu createPopupMenu(final JToggleButton btn) {
        JPopupMenu ret = new JPopupMenu();
        btn.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    /**
                     * show popup menu on toggleButton at position: (0, height)
                     */
                    popup.show(btn, 0, btn.getHeight());
                }
            }
        });
        ret.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                btn.setSelected(false);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
                btn.setSelected(false);
            }
        });

        return ret;
    }
}
