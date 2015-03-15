/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.openmol;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.pdb.PDBDoc;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.ren.RENList;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.ui.dynamicTable.DynamicTable;
import com.gas.domain.ui.dynamicTable.DynamicTableModel;
import com.gas.domain.ui.explorer.FolderMutableTreeNode;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 *
 * @author dq
 */
class ChooseDataPanelListeners {

    static class TableSelectionModel implements ListSelectionListener {

        private ChooseDataPanel ref;

        TableSelectionModel(ChooseDataPanel ref) {
            this.ref = ref;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            boolean valueAdjusting = e.getValueIsAdjusting();
            if (!valueAdjusting) {
                ref.validateInput();
            }
        }
    }

    static class InsertComboListener implements ActionListener {

        private ChooseDataPanel panel;

        InsertComboListener(ChooseDataPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox combo = (JComboBox) e.getSource();
            FolderMutableTreeNode node = (FolderMutableTreeNode) combo.getSelectedItem();
            Folder folder = node.getUserObject();
            DynamicTable table = panel.getDynamicTable();

            List list = folder.getElements();
            List filtered = filter(list, panel.getIncludeTypes());
            List data = new ArrayList(filtered);
            table.setData(data);
            TableColumnExt columnExt = table.getColumnExt(DynamicTableModel.CHECK_COL);
            if (columnExt != null && columnExt.isVisible()) {
                columnExt.setVisible(false);
            }

            table.packAll();
            panel.validateInput();
        }

        private List filter(List list, Folder.TYPE... types) {
            if (types == null) {
                return list;
            }
            Arrays.sort(types);
            List ret = new ArrayList();
            for (Object obj : list) {
                if (obj instanceof AnnotatedSeq) {
                    AnnotatedSeq as = (AnnotatedSeq) obj;
                    if (as.isDNA()) {
                        if (Arrays.binarySearch(types, Folder.TYPE.DNA) > -1) {
                            ret.add(obj);
                        }
                    } else if (as.isRNA()) {
                        if (Arrays.binarySearch(types, Folder.TYPE.RNA) > -1) {
                            ret.add(obj);
                        }
                    } else if (as.isProtein()) {
                        if (Arrays.binarySearch(types, Folder.TYPE.PROTEIN) > -1) {
                            ret.add(obj);
                        }
                    }
                } else if (obj instanceof RENList) {
                    if (Arrays.binarySearch(types, Folder.TYPE.ENZYMES) > -1) {
                        ret.add(obj);
                    }
                } else if (obj instanceof PubmedArticle) {
                    if (Arrays.binarySearch(types, Folder.TYPE.PubmedArticle) > -1) {
                        ret.add(obj);
                    }
                } else if (obj instanceof TigrProject) {
                    if (Arrays.binarySearch(types, Folder.TYPE.TIGR) > -1) {
                        ret.add(obj);
                    }
                } else if (obj instanceof PDBDoc) {
                    if (Arrays.binarySearch(types, Folder.TYPE.PDB) > -1) {
                        ret.add(obj);
                    }
                }
            }
            return ret;
        }
    }
}
