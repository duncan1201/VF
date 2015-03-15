/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.dynamicTable;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.database.core.api.IDomainUtil;
import com.gas.domain.ui.editor.AbstractSavableEditor;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class DynamicTableListeners {

    static class ModelListener implements TableModelListener {

        private IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);

        @Override
        public void tableChanged(TableModelEvent e) {
            int type = e.getType();
            if (type == TableModelEvent.UPDATE) {
                DynamicTableModel model = (DynamicTableModel) e.getSource();
                final int firstRow = e.getFirstRow();

                int column = e.getColumn();
                if (column == TableModelEvent.ALL_COLUMNS) {
                    // ignore
                } else if(column == 0){
                    BannerTC.getInstance().refreshHeader();
                } else if (firstRow > -1 && column > 0) { // the first column is the check box
                    Object obj = model.getRow(firstRow);
                    IFolderElement fe = (IFolderElement) obj;
                    if (fe.getHibernateId() != null) {
                        domainUtil.merge(obj);
                        AbstractSavableEditor editor = AbstractSavableEditor.getEditorByHibernateId(fe.getHibernateId());
                        if(editor != null){
                            editor.getFolderElement().setName(fe.getName());
                            editor.getFolderElement().setDesc(fe.getDesc());
          
                            UIUtil.setTopCompName(editor, fe.getName());                            
                        }
                    }
                }
            }
        }
    }

    static class TableMouseInputAdpt extends MouseInputAdapter {

        private javax.swing.Timer timer;
        private final int TIME_LAP = 110;
        private DynamicTable table;
        private IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);

        public TableMouseInputAdpt() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            table = (DynamicTable) e.getSource();

            if (table == null) {
                return;
            }
            Point point = e.getPoint();
            final int col = table.columnAtPoint(point);
            final int row = table.rowAtPoint(point);

            if (row < 0 || col < 0) {
                return;
            }

            boolean doubleClick = e.getClickCount() >= 2;

            final int selectionMode = table.getSelectionMode();
            DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) table.getSelectionModel();
            final DynamicTableModel model = (DynamicTableModel) table.getModel();
            int leadSelectionIndex = selectionModel.getLeadSelectionIndex();
            if (leadSelectionIndex < 0) {
                return;
            }
            final int modelIndex = table.convertRowIndexToModel(leadSelectionIndex);
            final Boolean checked = (Boolean) model.getValueAt(modelIndex, 0);

            if (!doubleClick) {

                if (col != 0) {
                    if (leadSelectionIndex > -1 && leadSelectionIndex < model.getRowCount()) {
                        Object obj = model.getRow(modelIndex);
                        if (table.isOpenEditorSupported()) {
                            domainUtil.openEditor(obj);
                            setTimer(model, true, modelIndex, selectionMode == ListSelectionModel.SINGLE_SELECTION, true);
                        }
                    }
                } else {
                    if (table.isEditable()) {
                        setTimer(model, !checked, modelIndex, selectionMode == ListSelectionModel.SINGLE_SELECTION);
                    }
                }
            } else if (doubleClick && table.isDoubleClickEnabled()) {
                clearTimer();
                if (!checked) {
                    if (table.isEditable()) {
                        setTimer(model, true, modelIndex, selectionMode == ListSelectionModel.SINGLE_SELECTION);
                    }
                }
            }
        }

        private void setTimer(final DynamicTableModel model, final boolean checked, final int modelIndex, final boolean exclusively) {
            setTimer(model, checked, modelIndex, exclusively, false);
        }

        private void setTimer(final DynamicTableModel model, final boolean checked, final int modelIndex, final boolean exclusively, boolean immediately) {
            if (immediately) {
                model.updateCheckBox(checked, modelIndex, exclusively);
            } else {
                clearTimer();
                timer = new javax.swing.Timer(TIME_LAP, new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        model.updateCheckBox(checked, modelIndex, exclusively);
                    }
                });
                timer.setRepeats(false);
                timer.start();
            }
        }

        private void clearTimer() {
            if (timer != null){
                timer.stop();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            boolean isPopupTrigger = e.isPopupTrigger();
            if (isPopupTrigger) {
                triggerPopup(e);
            }
        }

        private void triggerPopup(MouseEvent e) {
            DynamicTable obj = (DynamicTable) e.getSource();
            if (obj == null) {
                return;
            } else {
                table = obj;
            }

            Folder folder = ExplorerTC.getInstance().getSelectedFolder();
            triggerPopupMenu(folder, e);

        }

        void triggerPopupMenu(Folder folder, MouseEvent e) {

            JPopupMenu popupMenu = createPopupMenu(folder);

            Point p = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(p, table);
            UIUtil.showPopupMenu(popupMenu, table, p.x, p.y);

        }

        private JPopupMenu createPopupMenu(Folder folder) {
            table.popup.setFolder(folder);
            return (JPopupMenu)table.popup;
        }
    }
}