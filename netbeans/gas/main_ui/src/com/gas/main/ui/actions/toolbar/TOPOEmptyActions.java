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
import com.gas.topo.core.blunt.BluntTOPOCloningAction;
import com.gas.topo.core.actions.CreateDirTOPOInsertAction;
import com.gas.topo.core.actions.CreateTAInsertAction;
import com.gas.topo.core.actions.DirTOPOCloningAction;
import com.gas.topo.core.actions.TOPOTACloningAction;
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

@ActionID(category = "Tools",
id = "com.gas.main.ui.actions.toolbar.TOPOEmptyActions")
@ActionRegistration(displayName = "#CTL_TOPOEmptyActions")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 3325)
})
@NbBundle.Messages("CTL_TOPOEmptyActions=TOPO")
public class TOPOEmptyActions extends AbstractAction implements Presenter.Toolbar {

    private JPopupMenu popup;
    CreateTAInsertAction createTAInsertAction;
    CreateDirTOPOInsertAction createDirTOPOInsertAction;

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Component getToolbarPresenter() {
        final JToggleButton ret = new JToggleButton();
        ret.setText("TOPO");
        ret.setIcon(ImageHelper.createImageIcon(ImageNames.TOPO_24));
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);
        
        popup = create(ret);
        BluntTOPOCloningAction bluntTOPOCloningAction = new BluntTOPOCloningAction();
        popup.add(bluntTOPOCloningAction);
        
        popup.add(new JPopupMenu.Separator());
        
        createTAInsertAction = new CreateTAInsertAction() ;
        popup.add(createTAInsertAction);
        
        TOPOTACloningAction topoTACloningAction = new TOPOTACloningAction();
        popup.add(topoTACloningAction);
        
        popup.add(new JPopupMenu.Separator());
        
        createDirTOPOInsertAction = new CreateDirTOPOInsertAction();
        popup.add(createDirTOPOInsertAction);
                        
        DirTOPOCloningAction dirTOPOCloningAction = new DirTOPOCloningAction();
        popup.add(dirTOPOCloningAction);        
        return ret;
    }
    
    private JPopupMenu create(final JToggleButton btn){
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
