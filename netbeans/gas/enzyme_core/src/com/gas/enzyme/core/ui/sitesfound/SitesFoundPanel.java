/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SitesFoundContentPanel.java
 *
 * Created on Nov 27, 2011, 5:57:47 AM
 */
package com.gas.enzyme.core.ui.sitesfound;

import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.editor.IMolPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultRowSorter;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author dunqiang
 */
public class SitesFoundPanel extends javax.swing.JPanel {

    private WeakReference<JScrollPane> scrollPaneRef;
    private JTable sitesFoundTable;

    /**
     * Creates new form SitesFoundContentPanel
     */
    public SitesFoundPanel() {
        initComponents();
        hookupListeners();
    }

    public JTable getSitesFoundTable() {
        return sitesFoundTable;
    }

    public void setRMap(RMap rmap) {
        int totalPos = getMolPane().getAs().getLength();
        ((SitesFoundTableModel) sitesFoundTable.getModel()).setRMap(rmap, totalPos);
        
        DefaultRowSorter sorter = (DefaultRowSorter) sitesFoundTable.getRowSorter();
        List<RowSorter.SortKey> sortKeys = new ArrayList<RowSorter.SortKey>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
        sorter.setSortKeys(sortKeys);            
    }
    
    private IMolPane getMolPane(){
        IMolPane ret = UIUtil.getParent(this, IMolPane.class);
        return ret;
    }

    private void hookupListeners() {
        scrollPaneRef.get().addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension size = scrollPaneRef.get().getSize();
                Dimension pSize = sitesFoundTable.getPreferredSize();
                if (pSize.height > size.height) {
                    sitesFoundTable.setPreferredScrollableViewportSize(new Dimension(pSize.width - 10, size.height));
                } else {
                    sitesFoundTable.setPreferredScrollableViewportSize(new Dimension(pSize.width, size.height));
                }
            }
        });
    }

    public RMap.EntryList getSelectedRMapEntries() {
        SitesFoundTableModel model = (SitesFoundTableModel) sitesFoundTable.getModel();
        int[] rows = sitesFoundTable.getSelectedRows();
        for (int i = 0; i < rows.length; i++) {
            rows[i] = sitesFoundTable.convertRowIndexToModel(rows[i]);
        }
        RMap.EntryList entries = model.getRMapEntries(rows);
        return entries;
    }

    private void initComponents() {

        JScrollPane scrollPane = new javax.swing.JScrollPane();
        scrollPaneRef = new WeakReference<JScrollPane>(scrollPane);
        sitesFoundTable = new JTable() {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row,
                    int column) {
                JComponent ret = (JComponent)super.prepareRenderer(renderer, row, column);

                if (!ret.getBackground().equals(getSelectionBackground())) {
                    if (row % 2 == 0) {
                        ret.setBackground(ColorCnst.ALICE_BLUE);
                    } else {
                        ret.setBackground(Color.WHITE);
                    }
                }
                ret.setToolTipText(getValueAt(row, column).toString());
                return ret;
            }
        };

        setLayout(new BorderLayout());

        SitesFoundTableModel model = new SitesFoundTableModel();
        sitesFoundTable.setModel(model);
        sitesFoundTable.setAutoCreateRowSorter(true);
        sitesFoundTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        sitesFoundTable.getSelectionModel().addListSelectionListener(new SitesFoundPanelListeners.SelectionListener(sitesFoundTable));

        FontMetrics fm = FontUtil.getFontMetrics(sitesFoundTable);
        TableColumn column = sitesFoundTable.getColumnModel().getColumn(0);
        column.setPreferredWidth(fm.stringWidth("ECORI"));
        column = sitesFoundTable.getColumnModel().getColumn(1);
        column.setPreferredWidth(fm.stringWidth("ATCGATCGAA"));
        column = sitesFoundTable.getColumnModel().getColumn(2);
        column.setPreferredWidth(fm.stringWidth("2000-2000"));
        Dimension pSize = sitesFoundTable.getPreferredSize();
        sitesFoundTable.setPreferredScrollableViewportSize(new Dimension(pSize.width, 0));
        scrollPane.setViewportView(sitesFoundTable);

        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        add(scrollPane, c);
    }
}
