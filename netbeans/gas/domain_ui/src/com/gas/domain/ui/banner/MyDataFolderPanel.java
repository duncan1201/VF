/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import com.gas.domain.core.IFolderElement;
import com.gas.domain.ui.IFolderPanel;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.dynamicTable.*;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 *
 * @author dunqiang
 */
public class MyDataFolderPanel extends JPanel implements IFolderPanel {

    private static Logger logger = Logger.getLogger(MyDataFolderPanel.class.getName());
    protected DynamicTable table;
    private String folderPath;
    private Folder folder;
    protected DynamicTableModel tableModel;
    private String statusLine = "";
    private Boolean busy = false;

    public MyDataFolderPanel(Folder folder) {
        super(new BorderLayout());
        this.folderPath = folder.getAbsolutePath();

        this.folder = folder;

        tableModel = new DynamicTableModel();
        List data = new ArrayList();
        data.addAll(folder.getElements());

        JScrollPane scrollPane = new JScrollPane();

        getTable();

        DynamicColumnFactoryCalibrator calibrator = new DynamicColumnFactoryCalibrator();
        calibrator.configureDisplayProperties(table);

        scrollPane.setViewportView(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        table.setModel(tableModel);
        table.setData(data);
        tableModel.fireTableDataChanged();
       
        int index = tableModel.getColumnIndex(DynamicTableModel.ICON_COL);
        if (index > -1) {
            table.getColumnExt(index).setSortable(false);
        }

        table.packAll();

    }

    @Override
    public boolean isNCBIFolder() {
        return false;
    }

    @Override
    public Boolean getBusy() {
        return busy;
    }

    @Override
    public void setBusy(Boolean busy) {
        Boolean old = this.busy;
        this.busy = busy;
        firePropertyChange("busy", old, this.busy);
    }

    @Override
    public String getStatusLine() {
        return statusLine;
    }

    @Override
    public void setStatusLine(String statusLine) {
        String old = this.statusLine;
        this.statusLine = statusLine;
        firePropertyChange("statusLine", old, this.statusLine);
    }

    @Override
    public void updateFolder(Folder folder) {
        this.folder = folder;
        table.addOrUpdateOrDeleteData(this.folder.getElements());
        TableColumn column = table.getSortedColumn();
        if(column != null){            
            DefaultRowSorter sorter = ((DefaultRowSorter)table.getRowSorter());
            sorter.sort();
        }
        table.packAll();
    }

    /**
     * should never be called because all domain object must have a hibernateId
     */
    @Override
    public boolean unselectRow(IFolderElement obj) {
        return false;
    }

    @Override
    public boolean checkRow(IFolderElement obj, boolean checked) {
        boolean ret = false;
        Integer modelIndex = tableModel.getModelId(obj);
        if (modelIndex != null) {
            table.updateCheckBox(modelIndex, checked, false);
            ret = true;
        }
        return ret;
    }
    
    @Override
    public boolean setSelected(IFolderElement fe){
        return table.setSelected(fe);        
    }

    public boolean checkRow(String hId, boolean checked) {
        boolean ret = false;
        Integer modelIndex = tableModel.getModelIdByHibernateId(hId);
        if (modelIndex != null) {
            table.updateCheckBox(modelIndex, checked, false);
            ret = true;
        }
        return ret;
    }

    /**
     * @param hId hibernateId
     */
    public boolean unselectRow(String hId) {
        boolean ret = false;
        Integer modelIndex = tableModel.getModelIdByHibernateId(hId);

        if (modelIndex != null) {
            int[] rows = table.getSelectedRows();
            Arrays.sort(rows);

            int viewId = table.convertRowIndexToView(modelIndex);
            table.updateCheckBox(modelIndex, false, false);
            int index = Arrays.binarySearch(rows, viewId);
            if (index > -1) {
                table.removeRowSelectionInterval(viewId, viewId);
                table.repaint();
                ret = true;
            }
        }
        return ret;
    }

    @Override
    public Folder getFolder() {
        return folder;
    }

    @Override
    public DynamicTable getTable() {
        if (table == null) {
            table = new DynamicTable();            
        }
        return table;
    }

    public String getFolderPath() {
        return folderPath;
    }

    @Override
    public String getAbsolutePath() {
        return folder.getAbsolutePath();
    }

    @Override
    public void setFolder(Folder folder) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
