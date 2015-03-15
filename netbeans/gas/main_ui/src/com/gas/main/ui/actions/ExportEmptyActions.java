/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.main.ui.actions.imports.ExportCurrentEditorAction;
import com.gas.main.ui.actions.imports.ExportDocsAction;
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
        id = "com.gas.main.ui.actions.ExportEmptyActions")
@ActionRegistration(displayName = "#CTL_ExportEmptyActions")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 3440)
})
@NbBundle.Messages("CTL_ExportEmptyActions=Export")
public class ExportEmptyActions extends AbstractAction implements Presenter.Toolbar {

    private JPopupMenu popupMenu;
    
    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Component getToolbarPresenter() {
        JToggleButton ret = new JToggleButton();
        ret.setIcon(ImageHelper.createImageIcon(ImageNames.DOC_ARROW_UP_24));
        ret.setText("Export");
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);
        
        popupMenu = createPopupMenu(ret);
        Action action = new ExportCurrentEditorAction();
        popupMenu.add(action);
        action = new ExportDocsAction();
        popupMenu.add(action);
        return ret;
    }
    
    private JPopupMenu createPopupMenu(final JToggleButton btn) {
        final JPopupMenu ret = new JPopupMenu();
        btn.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                if (state == ItemEvent.SELECTED) {
                    /**
                     * show popup menu on toggleButton at position: (0, height)
                     */
                    ret.show(btn, 0, btn.getHeight());
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
