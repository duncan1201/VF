/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.contigs;

import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.FontUtil;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.MouseEvent;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import org.jdesktop.swingx.JXTable;

/**
 *
 * @author dq
 */
public class ContigsTable extends JTable {

    static Logger log = Logger.getLogger(ContigsTable.class.getName());

    public ContigsTable(ContigsTableModel model) {
        super(model);
        setFillsViewportHeight(true);
        setAutoCreateRowSorter(true);

        Font font = tableHeader.getFont();
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final String PROTOTYPE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        float charWidth = 1.0f * fm.stringWidth(PROTOTYPE) / PROTOTYPE.length();
        for (int col = 0; col < getColumnCount(); col++) {
            String colName = getColumnName(col);
            TableColumn tableColumn = columnModel.getColumn(col);
            tableHeader.setResizingColumn(tableColumn);
            //int width = (colName.length() + 2) * Math.round(charWidth);
            int width = Math.round(fm.stringWidth(colName) + fm.charWidth('W') * 1.14f);
            tableColumn.setWidth(width);
            tableColumn.setPreferredWidth(width);
            tableColumn.setCellRenderer(new Renderer());
        }   
        
        

    }
    @Override
    protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(columnModel) {
            @Override
            public String getToolTipText(MouseEvent e) {
                String tip = null;
                java.awt.Point p = e.getPoint();
                int index = columnModel.getColumnIndexAtX(p.x);
                int realIndex = 
                        columnModel.getColumn(index).getModelIndex();
                ContigsTableModel mdl = ContigsTable.this.getModel();
                mdl.getColumnName(realIndex);
                return mdl.getTooltip(realIndex);
            }
        };
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

    @Override
    public ContigsTableModel getModel() {
        return (ContigsTableModel) super.getModel();
    }  
}
