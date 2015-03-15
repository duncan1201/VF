/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ui;

import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.IFolderPanel;
import com.gas.domain.ui.dynamicTable.*;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.Arrays;
import javax.swing.BorderFactory;
import javax.swing.DefaultRowSorter;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IFolderPanel.class)
public class SearchPanel extends JPanel implements IFolderPanel {

    private Folder folder;
    private JPanel center;
    private DynamicTable table;
    private String statusLine = "";
    private Boolean busy = false;

    public SearchPanel() {
        LayoutManager layout = new BorderLayout();
        setLayout(layout);
        hookupListeners();
    }

    @Override
    public void updateFolder(Folder folder) {
        getTable().addOrUpdateOrDeleteData(folder.getElements());

        TableColumn column = getTable().getSortedColumn();
        if (column != null) {
            DefaultRowSorter sorter = ((DefaultRowSorter) getTable().getRowSorter());
            sorter.sort();
        }
    }

    @Override
    public boolean isNCBIFolder() {
        return true;
    }

    private void hookupListeners() {
        addPropertyChangeListener(new SearchPanelListeners.PtyChangeLisener());
    }

    @Override
    public Boolean getBusy() {
        return busy;
    }

    @Override
    public boolean setSelected(IFolderElement fe) {
        return table.setSelected(fe);
    }

    @Override
    public void setBusy(Boolean busy) {
        Boolean old = this.busy;
        this.busy = busy;
        firePropertyChange("busy", old, this.busy);
    }

    @Override
    public void setFolder(Folder folder) {
        this.folder = folder;

        add(getCenterPanel(), BorderLayout.CENTER);
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

    public SearchPanel(Folder folder) {
        super();
        this.folder = folder;
        LayoutManager layout = null;
        //layout = new VerticalLayout();
        layout = new BorderLayout();
        setLayout(layout);

        setFolder(folder);
    }

    @Override
    public Folder getFolder() {
        return folder;
    }

    private JPanel getCenterPanel() {
        if (center == null) {
            center = new JPanel();
            LayoutManager layout = new BorderLayout();
            center.setLayout(layout);

            SearchCtrlPanel searchControlPanel = new SearchCtrlPanel(folder);
            center.add(searchControlPanel, BorderLayout.NORTH);

            table = getTable();
            DynamicColumnFactoryCalibrator calibrator = new DynamicColumnFactoryCalibrator();
            configureDisplayProperties(table, calibrator);

            JScrollPane scrollPane = new JScrollPane();

            scrollPane.setViewportView(table);
            scrollPane.setBorder(BorderFactory.createEmptyBorder());
            center.add(scrollPane, BorderLayout.CENTER);
        }
        return center;
    }

    public DynamicTableModel getDynamicTableModel() {
        return (DynamicTableModel) getTable().getModel();
    }

    @Override
    public DynamicTable getTable() {
        if (table == null) {
            DynamicTableModel model = new DynamicTableModel();
            model.setNameEditable(false);
            model.setDescEditable(false);
            table = new DynamicTable();
            table.setModel(model);
        }
        return table;
    }

    /**
     * This is data-unrelated. It NEEDs to be invoked before setting the table
     * model
     */
    private void configureDisplayProperties(JXTable table, IColumnFactoryCalibrator calibrator) {
        //<snip> JXTable display properties
        // show column control
        table.setColumnControlVisible(true);
        // replace grid lines with striping 
        table.setShowGrid(false, false);
        table.addHighlighter(HighlighterFactory.createSimpleStriping());
        // initialize preferred size for table's viewable area
        table.setVisibleRowCount(10);
//        </snip>

        //<snip> JXTable column properties
        // create and configure a custom column factory
        CustomColumnFactory factory = new CustomColumnFactory();
        calibrator.configureColumnFactory(factory);

        // set the factory before setting the table model
        table.setColumnFactory(factory);
//        </snip>

    }

    @Override
    public String getAbsolutePath() {
        return folder.getAbsolutePath();
    }

    @Override
    public boolean unselectRow(IFolderElement obj) {
        boolean ret = false;
        DynamicTableModel tableModel = (DynamicTableModel) table.getModel();
        Integer modelIndex = tableModel.getModelId(obj);

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
    public boolean checkRow(IFolderElement obj, boolean checked) {
        boolean ret = false;
        DynamicTableModel tableModel = (DynamicTableModel) table.getModel();
        Integer modelIndex = tableModel.getModelId(obj);
        if (modelIndex != null) {
            table.updateCheckBox(modelIndex, checked, false);
            ret = true;
        }
        return ret;
    }
}
