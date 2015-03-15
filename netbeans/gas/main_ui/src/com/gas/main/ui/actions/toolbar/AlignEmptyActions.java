/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.toolbar;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.msa.actions.MSAAction;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

@ActionID(category = "Tools",
        id = "com.gas.main.ui.actions.toolbar.AlignEmptyActions")
@ActionRegistration(displayName = "#CTL_AlignEmptyActions")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 3340)
})
@Messages("CTL_AlignEmptyActions=Align")
public final class AlignEmptyActions extends AbstractAction implements Presenter.Toolbar {

    private JPopupMenu popup;

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Component getToolbarPresenter() {
        final JToggleButton ret = new JToggleButton();
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);
        ret.setText("Alignment");
        ret.setIcon(ImageHelper.createImageIcon(ImageNames.ATG_24));

        popup = create(ret);        

        MSAAction msaAction = new MSAAction();
        popup.add(msaAction);

        return ret;
    }

    private JPopupMenu create(final JToggleButton btn) {
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
            public void popupMenuCanceled(PopupMenuEvent e) {
                btn.setSelected(false);
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                btn.setSelected(false);
            }

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                Folder folder = ExplorerTC.getInstance().getSelectedFolder();
                UIUtil.enabledRecursively(ret, !folder.isNCBIFolder() && !folder.isRecycleBin());                
            }
        });

        return ret;
    }
}
