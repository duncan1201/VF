/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.contigs;

import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.FontUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author dq
 */
public class VariantsTable extends JTable {

    private VariantsTableModel model;
    private VariantMapMdl variantMapMdl;

    public VariantsTable() {
        model = new VariantsTableModel();
        setModel(model);
        setAutoCreateRowSorter(false);
        TableRowSorter<VariantsTableModel> sorter =
                new TableRowSorter<VariantsTableModel>(model);

        for (int c = 0; c < model.getColumnCount(); c++) {
            sorter.setSortable(c, false);
        }

        setRowSorter(sorter);

        getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getSelectionModel().addListSelectionListener(new VariantsTableListeners.SelectionListener(this));

        addPropertyChangeListener(new VariantsTableListeners.PtyChangeListener(this));

        Font font = tableHeader.getFont();
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final String PROTOTYPE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        float charWidth = 1.0f * fm.stringWidth(PROTOTYPE) / PROTOTYPE.length();
        for (int col = 0; col < getColumnCount(); col++) {
            String colName = getColumnName(col);
            TableColumn tableColumn = columnModel.getColumn(col);
            tableHeader.setResizingColumn(tableColumn);
            tableColumn.setWidth(Math.round((colName.length() + 2) * charWidth));
            tableColumn.setCellRenderer(new Renderer());
        }
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        Dimension ret = this.getPreferredSize();
        ret.height = getRowHeight() * 8;
        return ret;
    }

    public VariantMapMdl getVariantMapMdl() {
        return variantMapMdl;
    }

    @Override
    public VariantsTableModel getModel() {
        return (VariantsTableModel) model;
    }

    public void setVariantMapMdl(VariantMapMdl mdl) {
        VariantMapMdl old = this.variantMapMdl;
        this.variantMapMdl = mdl;
        firePropertyChange("variantMapMdl", old, this.variantMapMdl);
    }

    private static class Renderer extends DefaultTableCellRenderer {

        Renderer() {
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!comp.getBackground().equals(table.getSelectionBackground())) {
                if (row % 2 == 0) {
                    comp.setBackground(ColorCnst.ALICE_BLUE);
                } else {
                    comp.setBackground(Color.WHITE);
                }
            }
            if (comp instanceof JLabel) {
                JLabel l = (JLabel) comp;
                l.setHorizontalAlignment(SwingConstants.LEFT);
            }
            return comp;
        }
    }
}
