/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.StrUtil;
import com.gas.domain.core.primer3.UserInput;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author dq
 */
public class UserInputTable extends JTable {

    public UserInputTable() {
        super(new UserInputTableModel());
    }

    public void setEditable(boolean editable) {
        UserInputTableModel model = (UserInputTableModel) getModel();
        model.setEditable(editable);
    }

    public void setUserInputs(List<UserInput> userInputs) {
        UserInputTableModel model = (UserInputTableModel) getModel();
        model.setUserInputs(userInputs);
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
        return ret;
    }

    public static class UserInputTableModel extends AbstractTableModel {

        private boolean editable = true;
        private List<UserInput> userInputs = new ArrayList<UserInput>();
        private final String DEFAULT_COL = "Default";
        private final String DESC_COL = "Description";
        private final String NAME_COL = "Name";
        private final String DATE_COL = "Last Updated";
        private String[] COL_NAMES = {NAME_COL, DESC_COL, DATE_COL};

        public UserInputTableModel() {
        }

        public void setUserInputs(List<UserInput> userInputs) {
            this.userInputs = userInputs;
        }

        @Override
        public Class getColumnClass(int column) {
            String name = getColumnName(column);
            if (name.equals(DEFAULT_COL)) {
                return Boolean.class;
            } else if (name.equals(NAME_COL)) {
                return String.class;
            } else if (name.equals(DESC_COL)) {
                return String.class;
            } else if (name.equals(DATE_COL)) {
                return Date.class;
            } else {
                throw new UnsupportedOperationException();
            }
        }

        @Override
        public int getRowCount() {
            return userInputs.size();
        }

        public boolean isEditable() {
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }

        @Override
        public int getColumnCount() {
            return COL_NAMES.length;
        }

        public List<UserInput> getUserInputs() {
            return userInputs;
        }

        @Override
        public String getColumnName(int column) {
            return COL_NAMES[column];
        }

        public UserInput getRow(int i) {
            UserInput ret = null;
            if (i < userInputs.size() && i > -1) {
                ret = userInputs.get(i);
            }
            return ret;
        }

        /**
         * @param i 0-based
         */
        protected void deleteRow(int i) {
            userInputs.remove(i);
            fireTableRowsDeleted(i, i);
        }

        protected void addRow(UserInput userInput) {
            userInputs.add(userInput);
            fireTableRowsInserted(1, 1);
        }

        protected String getNewName(String proposedName) {
            StringList nameExisting = new StringList();
            for (UserInput userInput : userInputs) {
                String name = userInput.getName();
                nameExisting.add(name);
            }
            return StrUtil.getNewName(proposedName, nameExisting);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            String colName = getColumnName(columnIndex);
            if (colName.equals(DEFAULT_COL)) {
                Boolean favorite = (Boolean) aValue;
                if (favorite) {
                    for (int i = 0; i < userInputs.size(); i++) {
                        UserInput userInput = userInputs.get(i);
                        if (i != rowIndex) {
                            userInput.setFavorite(false);
                        } else {
                            userInput.setFavorite(true);
                        }
                    }
                    fireTableDataChanged();
                }
            } else if (colName.equals(NAME_COL)) {
                UserInput userInput = getRow(rowIndex);
                userInput.setName((String) aValue);
            } else if (colName.equals(DESC_COL)) {
                UserInput userInput = getRow(rowIndex);
                userInput.setDescription((String) aValue);
            }
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (isEditable()) {
                String name = getColumnName(columnIndex);
                if (name.equals(DEFAULT_COL)) {
                    return true;
                } else if (name.equals(NAME_COL)) {
                    return true;
                } else if (name.equals(DESC_COL)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object ret = null;
            UserInput userInput = userInputs.get(rowIndex);
            String colName = getColumnName(columnIndex);
            if (colName.equals(DEFAULT_COL)) {
                ret = userInput.isFavorite();
            } else if (colName.equals(NAME_COL)) {
                ret = userInput.getName();
            } else if (colName.equals(DESC_COL)) {
                ret = userInput.getDescription();
            } else if (colName.equals(DATE_COL)) {
                ret = userInput.getUpdatedDate();
            }
            return ret;
        }
    }
}
