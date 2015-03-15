/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.toolbar;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.as.ligate.actions.LigateAction;
import com.gas.domain.ui.editor.as.ligate.actions.RENAnalysisAction;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.openide.awt.ActionRegistration;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionID;
import org.openide.util.NbBundle.Messages;
import org.openide.util.actions.Presenter;

@ActionID(category = "Tools",
        id = "com.gas.main.ui.actions.toolbar.CloneEmptyActions")
@ActionRegistration(displayName = "#CTL_CloneEmptyActions")
@ActionReferences({
    @ActionReference(path = "Toolbars/CUSTOME", position = 3320)
})
@Messages("CTL_CloneEmptyActions=Clone")
public final class CloneEmptyActions extends AbstractAction implements Presenter.Toolbar {

    private JPopupMenu popup;
    RENAnalysisAction renAnalysisAction;
    LigateAction ligateAction;

    @Override
    public void actionPerformed(ActionEvent e) {
    }

    @Override
    public Component getToolbarPresenter() {
        final JToggleButton ret = new JToggleButton();
        ret.setText("Cloning");
        ret.setIcon(ImageHelper.createImageIcon(ImageNames.CLONING_24));
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);

        popup = create(ret);

        renAnalysisAction = new RENAnalysisAction();
        popup.add(renAnalysisAction);

        ligateAction = new LigateAction();
        popup.add(ligateAction);

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
                updateEnablement();
            }
        });

        return ret;
    }

    private void updateEnablement() {        
        renAnalysisAction.setEnabled(RENAnalysisAction.getEnablement());
        
        List<AnnotatedSeq> objs = BannerTC.getInstance().getCheckedObjects(AnnotatedSeq.class);
        ligateAction.setEnabled(LigateAction.validate(objs, false));
        
    }
}
