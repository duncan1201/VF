/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.editor;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.ren.IRENListService;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.editor.renlist.api.IRENListEditor;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.enzyme.core.ui.enzymesused.RENListPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class ControlPanelListeners {

    static class NewListener implements ActionListener {

        public NewListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Folder folder = BannerTC.getInstance().getFolderPanel().getFolder();
            RENList renList = new RENList();
            String pName = folder.getNewElementName("New List");
            renList.setName(pName);

            RENListPanel p = new RENListPanel(renList);

            StringList existingNames = folder.getElementNames(RENList.class);
            p.setExistingNames(existingNames);
            DialogDescriptor d = new DialogDescriptor(p, "New Restriction Enzyme List");
            p.setDialogDescriptor(d);
            p.validateInput();

            Object answer = DialogDisplayer.getDefault().notify(d);
            if (answer.equals(DialogDescriptor.OK_OPTION)) {
                renList = p.getRENList();
                IRENListService svc = Lookup.getDefault().lookup(IRENListService.class);

                renList.setFolder(folder);
                svc.persist(renList);

                IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
                Folder updatedFolder = folderService.loadWithDataAndParents(folder.getHibernateId());

                ExplorerTC.getInstance().updateFolder(folder);
                BannerTC.getInstance().updateFolder(updatedFolder);
            }
        }
    }

    static class EditListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            StringList existingNames = BannerTC.getInstance().getFolderPanel().getFolder().getElementNames(RENList.class);
            JComponent src = (JComponent) e.getSource();
            RENListEditor editor = UIUtil.getParent(src, RENListEditor.class);
            RENListPanel p = new RENListPanel(editor.getRENList());
            existingNames.remove(editor.getRENList().getName());
            p.setExistingNames(existingNames);
            DialogDescriptor d = new DialogDescriptor(p, "Edit Restriction Enzyme List");
            p.setDialogDescriptor(d);
            p.validateInput();
            Object answer = DialogDisplayer.getDefault().notify(d);
            if (answer.equals(DialogDescriptor.OK_OPTION)) {
                RENList renList = p.getRENList();
                IRENListService svc = Lookup.getDefault().lookup(IRENListService.class);
                RENList updated = svc.merge(renList);
                editor.setRENList(renList);
                BannerTC.getInstance().updataRowByHibernateId(updated);
            }
        }
    }

    static class DelListener implements ActionListener {

        private IRENListEditor editor;

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            if (editor == null) {
                editor = UIUtil.getParent(btn, IRENListEditor.class);
            }

            RENList renList = editor.getRENList();
            JTable table = editor.getRenTable();
            int leadSelIndex = table.getSelectionModel().getLeadSelectionIndex();
            RENListTableModel model = (RENListTableModel) table.getModel();
            int[] selRowIndices = table.getSelectedRows();
            List<Integer> modelIndices = new ArrayList<Integer>();
            for (int rowIndex : selRowIndices) {
                int modelIndex = table.convertRowIndexToModel(rowIndex);
                RENListTableModel.Row row = model.getRow(modelIndex);
                String name = row.getName();
                renList.removeREN(name);

                modelIndices.add(modelIndex);
            }
            Collections.sort(modelIndices, Collections.reverseOrder());
            for (Integer modelIndex : modelIndices) {
                model.removeRow(modelIndex);
            }
            if (leadSelIndex < model.getRowCount()) {
                table.setRowSelectionInterval(leadSelIndex, leadSelIndex);
            }
            editor.setCanSave();
        }
    }
}
