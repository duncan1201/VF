/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.enzymesused;

import com.gas.common.ui.combo.BorderComboRenderer;
import com.gas.common.ui.jcomp.CardPanel;
import com.gas.common.ui.jcomp.StringListPanel;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.ren.IRENListService;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class EnzymesUsedPanelListeners {

    static class PtyListener implements PropertyChangeListener{

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            EnzymesUsedPanel src = (EnzymesUsedPanel)evt.getSource();
            Object v = evt.getNewValue();
            if(pName.equals("inputParams")){
                RMap.InputParams inputParams = src.getInputParams();
                src.setSelectedRENList(inputParams.getRenListName());
                src.setSelectedRENs(inputParams.getRenNames());
            }
        }
    }
    
    static class NewBtnListener implements ActionListener {

        private EnzymesUsedPanel enzymesUsedPanel;
        private IRENListService renListService = Lookup.getDefault().lookup(IRENListService.class);
        private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton)e.getSource();
            if(enzymesUsedPanel == null){
                enzymesUsedPanel = UIUtil.getParent(btn, EnzymesUsedPanel.class);
            }
                       
            List<String> names = renListService.getAllNames();

            RENListPanel newRENListPanel = new RENListPanel();
            newRENListPanel.setExistingNames(names);
            DialogDescriptor dd = new DialogDescriptor(newRENListPanel, "New Restriction Enzyme List");
            newRENListPanel.setDialogDescriptor(dd);

            newRENListPanel.validateInput();

            Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);
            if (answer.equals(DialogDescriptor.OK_OPTION)) {
                RENList retList = newRENListPanel.getRENList();
                Folder folder = ExplorerTC.getInstance().getSelectedFolder();
                folder.addObject(retList);
                folderService.merge(folder);
                Folder mergedFolder = folderService.loadWithDataAndParents(folder.getHibernateId());
                ExplorerTC.getInstance().updateFolder(mergedFolder);
                enzymesUsedPanel.reinitEnzymeCombo();
                BannerTC.getInstance().updateFolder(mergedFolder);
            }
        }
    }

    static class EnzymeComboListener implements ActionListener {

        private EnzymesUsedPanel enzymesUsedPanel;

        public EnzymeComboListener(EnzymesUsedPanel enzymesUsedPanel) {
            this.enzymesUsedPanel = enzymesUsedPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = enzymesUsedPanel.getEnzymeCombo().getSelectedIndex();
            Object element = enzymesUsedPanel.getEnzymeCombo().getModel().getElementAt(index);
            if (element instanceof BorderComboRenderer.Item) {
                BorderComboRenderer.Item item = (BorderComboRenderer.Item) element;
                Object data = item.getData();
                if (data instanceof RENList) {
                    RENList renList = (RENList) data;
                    CardPanel cardPanel = enzymesUsedPanel.getEnzymeListCardPanel();
                    String hibernateId = renList.getHibernateId();
                    String name = renList.getName();
                    if (!cardPanel.containsCard(name)) {
                        StringListPanel card = new StringListPanel();
                        RENList fullRenList = enzymesUsedPanel.renListService.getFullRENListByHibernateId(hibernateId);
                        Iterator<REN> renItr = fullRenList.getIterator();
                        while (renItr.hasNext()) {
                            REN ren = renItr.next();
                            card.addString(ren.getName());
                        }
                        card.revalidate();

                        cardPanel.add(card, name);

                    } else {
                        //System.out.println("cardPanel.containsCard("+name+")");
                    }
                    cardPanel.revalidate();
                    cardPanel.show(name);
                    TitledPanel titledPanel = UIUtil.getParent(enzymesUsedPanel, TitledPanel.class);
                    if(titledPanel != null){
                        UIUtil.setBackground(titledPanel.getContentPane(), CNST.BG, JPanel.class);
                        UIUtil.setBackground(titledPanel.getContentPane(), CNST.BG, JViewport.class);
                    }
                    StringListPanel slp = (StringListPanel) cardPanel.getCard(name);
                    slp.revalidate();
                } else if (data instanceof String) {
                }
            } else {
            }
        }
    }
}
