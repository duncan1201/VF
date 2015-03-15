/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.dynamicTable;

import com.gas.common.ui.util.ColorCnst;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.IFolderElementList;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.ui.banner.TableActions;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.renderer.JRendererLabel;
import org.jdesktop.swingx.sort.TableSortController;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class DynamicTable extends JXTable {

    private boolean doubleClickEnabled = true;
    private boolean openEditorSupported = true;
    IDynamicPopup popup;

    public DynamicTable() {
        super();
        setColumnSelectionAllowed(false);
        setRowSelectionAllowed(true);
        getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(DynamicTable.this, DnDConstants.ACTION_MOVE, new DragGestureListener() {
            @Override
            public void dragGestureRecognized(DragGestureEvent dge) {
                MultiTransferable transferable = (MultiTransferable) createMultiTransferable();

                dge.startDrag(null, transferable);
            }
        });
        IDynamicPopupCreator popupCreator = Lookup.getDefault().lookup(IDynamicPopupCreator.class);
        popup = popupCreator.create(this);
        setupListeners();
        setupMappings();
    }
    
    public boolean isDoubleClickEnabled() {
        return doubleClickEnabled;
    }

    public void setDoubleClickEnabled(boolean doubleClickEnabled) {
        this.doubleClickEnabled = doubleClickEnabled;
    }

    public boolean isOpenEditorSupported() {
        return openEditorSupported;
    }

    public void setOpenEditorSupported(boolean openEditorSupported) {
        this.openEditorSupported = openEditorSupported;
    }

    @Override
    public void setModel(TableModel dataModel) {
        super.setModel(dataModel);
        getModel().addTableModelListener(new DynamicTableListeners.ModelListener());
    }

    private void setupListeners() {
        addMouseListener(new DynamicTableListeners.TableMouseInputAdpt());

    }

    public List<Integer> getCheckedRows() {
        List<Integer> ret = new ArrayList<Integer>();
        DynamicTableModel model = (DynamicTableModel) getModel();
        int count = model.getRowCount();
        for (int i = 0; i < count; i++) {
            Boolean checked = (Boolean) model.getValueAt(i, 0);
            if (checked) {
                ret.add(i);
            }
        }
        return ret;
    }
    
    public boolean setSelected(IFolderElement fe){
        boolean ret = false;
        DynamicTableModel model = (DynamicTableModel) getModel();
        Integer modelId = model.getModelId(fe);
        if(modelId != null){
            int view = convertRowIndexToView(modelId);
            setRowSelectionInterval(view, view);
            ret = true;
        }
        return ret;
    }

    public void updateCheckBox(int modelIndex, boolean checked, boolean exclusively) {
        DynamicTableModel model = (DynamicTableModel) getModel();
        model.updateCheckBox(checked, modelIndex, false);
    }

    public void replaceDatumByHibernateId(IFolderElement obj) {
        validateModel();
        DynamicTableModel m = (DynamicTableModel) getModel();
        m.updateRowByHibernateId(obj);
    }

    public void addDataFront(IFolderElementList data) {
        validateModel();
        DynamicTableModel m = (DynamicTableModel) getModel();
        if (m == null) {
            throw new IllegalStateException("No data model set");
        }
        m.addDataFront(data, true);
        disableFirstColumnSorting();
    }

    public void setData(List data) {
        validateModel();
        DynamicTableModel m = (DynamicTableModel) getModel();
        if (m == null) {
            throw new IllegalStateException("No data model set");
        }
        m.setData(data);
        disableFirstColumnSorting();
    }

    private void disableFirstColumnSorting() {
        DynamicTableModel m = (DynamicTableModel) getModel();
        if (m.getColumnCount() > 0) {
            TableSortController rowSorter = (TableSortController) getRowSorter();
            rowSorter.setSortable(0, false);
        }
    }

    private void validateModel() {
        TableModel model = getModel();
        if (!(model instanceof DynamicTableModel)) {
            throw new IllegalStateException("Must be 'DynamicTableModel'");
        }
    }

    public void addOrUpdateOrDeleteData(IFolderElementList data) {
        validateModel();
        DynamicTableModel m = (DynamicTableModel) getModel();
        if (m == null) {
            throw new IllegalStateException("No data model set");
        }
        m.addOrUpdateOrDeleteData(data);
    }

    protected MultiTransferable createMultiTransferable() {

        List<TableTransferable> ts = new ArrayList<TableTransferable>();
        List<Integer> rows = DynamicTable.this.getCheckedRows();

        for (Integer row : rows) {
            TableTransferable transferable = new TableTransferable();
            int modelIndex = row;
            DynamicTableModel model = (DynamicTableModel) DynamicTable.this.getModel();
            Object obj = model.getRow(modelIndex);
            transferable.addTransferData(obj);
            ts.add(transferable);
        }
        MultiTransferable ret = new MultiTransferable(ts.toArray(new TableTransferable[ts.size()]));
        return ret;
    }

    private void setupMappings() {
        setTransferHandler(new TableTransferHandler());

        final InputMap inputMap = getInputMap();

        //set up delete action
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), TableActions.RecycleAction.class.getName());
        getActionMap().put(TableActions.RecycleAction.class.getName(), new TableActions.RecycleAction(this));

        // setupt select all action
        KeyStroke selectAllKS = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK);
        inputMap.put(selectAllKS, TableActions.SelectAllAction.class.getSimpleName());
        getActionMap().put(TableActions.SelectAllAction.class.getSimpleName(), new TableActions.SelectAllAction(this));
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK),
                TransferHandler.getCutAction().getValue(Action.NAME));
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK),
                TransferHandler.getCopyAction().getValue(Action.NAME));
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK),
                TransferHandler.getPasteAction().getValue(Action.NAME));


    }

    public void updateCheckBoxs(boolean checked) {
        selectionModel.setSelectionInterval(0, getModel().getRowCount() - 1);
        DynamicTableModel model = (DynamicTableModel) dataModel;
        model.updateCheckBoxs(checked);
    }

    public List getSelectedObjects() {
        List ret = new ArrayList();
        int[] rows = getSelectedRows();
        DynamicTableModel m = (DynamicTableModel) getModel();
        for (int r : rows) {
            int modelIndex = convertRowIndexToModel(r);
            Object row = m.getRow(modelIndex);
            ret.add(row);
        }
        return ret;
    }
    
    public <T> List<T> getSelectedObjects(Class<T> retType){
        List<T> ret = new ArrayList<T>();
        List selected = getSelectedObjects();
        for(Object obj: selected){
            if(obj.getClass().getName().equals(retType.getName())){
                ret.add((T)obj);
            }
        }
        return ret;
    }

    public <T> List<T> getCheckedObjects(Class<T> clazz) {
        List<T> ret = new ArrayList<T>();
        List objs = getCheckedObjects();
        for (Object obj : objs) {
            if (clazz.isAssignableFrom(obj.getClass())) {
                ret.add((T) obj);
            }
        }
        return ret;
    }

    public List<AnnotatedSeq> getCheckedNucleotides() {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        List objs = getCheckedObjects();
        for (Object obj : objs) {
            if (obj instanceof AnnotatedSeq) {
                AnnotatedSeq as = (AnnotatedSeq) obj;
                if (as.isNucleotide()) {
                    ret.add(as);
                }
            }
        }
        return ret;
    }

    public List<AnnotatedSeq> getCheckedProteins() {
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        List objs = getCheckedObjects();
        for (Object obj : objs) {
            if (obj instanceof AnnotatedSeq) {
                AnnotatedSeq as = (AnnotatedSeq) obj;
                if (as.isProtein()) {
                    ret.add(as);
                }
            }
        }
        return ret;
    }

    public IFolderElementList getCheckedObjects() {
        IFolderElementList ret = new IFolderElementList();
        List<Integer> rows = getCheckedRows();
        DynamicTableModel m = (DynamicTableModel) getModel();
        for (int r : rows) {
            IFolderElement row = m.getRow(r);
            ret.add(row);
        }
        return ret;

    }

    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row,
            int column) {
        Component ret = super.prepareRenderer(renderer, row, column);

        if (!ret.getBackground().equals(getSelectionBackground())) {
            if (row % 2 == 0) {
                ret.setBackground(ColorCnst.ALICE_BLUE);
            } else {
                ret.setBackground(Color.WHITE);
            }
        }

        if (column >= 1) {
            DynamicTableModel model = (DynamicTableModel) getModel();
            int rowModel = this.convertRowIndexToModel(row);
            IFolderElement element = (IFolderElement) model.getRow(rowModel);
            if (ret instanceof JRendererLabel) {
                JRendererLabel r = (JRendererLabel) ret;
                Font boldFont = r.getFont().deriveFont(Font.BOLD);
                Font plainFont = r.getFont().deriveFont(Font.PLAIN);
                if (element.isRead()) {
                    r.setFont(plainFont);
                } else {
                    r.setFont(boldFont);
                }
            }
        }
        return ret;
    }
}
