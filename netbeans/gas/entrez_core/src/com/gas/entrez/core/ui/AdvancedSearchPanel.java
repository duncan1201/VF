/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ui;

import com.gas.common.ui.popupButton.IPopupBtnContent;
import com.gas.common.ui.util.DateUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.entrez.core.EInfo.api.EInfoResult;
import com.gas.entrez.core.EInfo.api.IEInfoCmd;
import com.toedter.calendar.JDateChooser;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.jdesktop.swingx.prompt.PromptSupport;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class AdvancedSearchPanel extends JPanel implements IPopupBtnContent {

    //private String db;
    private EInfoResult infoResult;
    private JScrollPane scrollPane;
    private JPanel contentPane;
    private boolean includeAllFields;
    private IEInfoCmd einfoCmd = Lookup.getDefault().lookup(IEInfoCmd.class);

    public AdvancedSearchPanel(String db) {
        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));
        infoResult = einfoCmd.getPreloaded(db);
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = UIUtil.getDefaultInsets();
        scrollPane = new JScrollPane(getContentPane());
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, c);
    }

    public JPanel getContentPane() {
        if (contentPane == null) {
            contentPane = new ContentPane();
        }
        return contentPane;
    }

    public boolean isIncludeAllFields() {
        return includeAllFields;
    }

    public void setIncludeAllFields(boolean includeAllFields) {
        this.includeAllFields = includeAllFields;
    }

    public String getTerm(boolean includeFirstOperator) {
        StringBuilder ret = new StringBuilder();
        List<Row> rows = UIUtil.getChildren(this, Row.class);
        Iterator<Row> itr = rows.iterator();
        boolean first = true;
        while (itr.hasNext()) {
            Row r = itr.next();
            String term = r.getTerm();
            String ope = r.getLogicalOperator();
            if (!term.isEmpty()) {
                ret.append(' ');
                if (first) {
                    if (includeFirstOperator) {
                        ret.append(ope.toUpperCase(Locale.ENGLISH));
                    }
                } else {
                    ret.append(ope.toUpperCase(Locale.ENGLISH));
                }
                ret.append(' ');
                ret.append(term);
                first = false;
            }
        }
        ret.trimToSize();
        return ret.toString();
    }

    @Override
    public void showing() {
    }

    @Override
    public void hiding() {
    }

    @Override
    public void shown() {
    }

    @Override
    public void hidden() {
    }

    private class ContentPane extends JPanel {

        public ContentPane() {
            LayoutManager layout = new GridBagLayout();
            setLayout(layout);
            GridBagConstraints c;

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = 2;
            c.weightx = 1.0;
            JSeparator separator = new JSeparator(JSeparator.HORIZONTAL);
            //add(new Ctrl(), c);

            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridwidth = 2;
            c.weightx = 1.0;
            //add(separator, c);

            int counter = 0;
            int COLUMN_NO = 3;
            if (infoResult.getDbName().equalsIgnoreCase("pubmed")) {
                COLUMN_NO = 4;
            }
            List<EInfoResult.Field> fields = infoResult.getFieldList();
            for (EInfoResult.Field field : fields) {
                boolean firstColumn = counter % COLUMN_NO == 0;
                int gridy = counter / COLUMN_NO;
                String fullName = field.getFullName();
                Row row;
                boolean isAllFields = fullName.equalsIgnoreCase("all fields");
                if (isAllFields) {
                    if (isIncludeAllFields()) {

                        c = new GridBagConstraints();
                        c.gridx = firstColumn ? 0 : GridBagConstraints.RELATIVE;
                        c.gridy = gridy;
                        c.fill = GridBagConstraints.HORIZONTAL;
                        c.weightx = 1.0;

                        row = new Row(field);
                        ContentPane.this.add(row, c);
                        counter++;
                    }
                } else {
                    c = new GridBagConstraints();
                    c.gridx = firstColumn ? 0 : GridBagConstraints.RELATIVE;
                    c.gridy = gridy;
                    c.fill = GridBagConstraints.HORIZONTAL;
                    c.weightx = 1.0;
                    row = new Row(field);
                    ContentPane.this.add(row, c);
                    counter++;
                }
            }
            adjustLabelPreferredSize();
        }

        private void adjustLabelPreferredSize() {
            List<Row> rows = UIUtil.getChildren(ContentPane.this, Row.class);
            List<Dimension> sizes = new ArrayList<Dimension>();
            for (Row r : rows) {
                Dimension s = r.getFieldNameLabel().getPreferredSize();
                sizes.add(s);
            }
            Dimension widest = UIUtil.widest(sizes);
            for (Row r : rows) {
                r.getFieldNameLabel().setPreferredSize(widest);
            }
        }
    }

    private class Ctrl extends JPanel {

        public Ctrl() {
            LayoutManager layout = new GridBagLayout();
            setLayout(layout);
            GridBagConstraints c = null;



            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 1.0;
            c.fill = GridBagConstraints.HORIZONTAL;
            Component comp = Box.createRigidArea(new Dimension(1, 1));
            add(comp, c);

            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 5);
            JButton resetBtn = new JButton("Reset all to default");
            resetBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ContentPane contentPane = UIUtil.getParent(Ctrl.this, ContentPane.class);
                    List<Row> rows = UIUtil.getChildren(contentPane, Row.class);
                    for (Row r : rows) {
                        r.reset();
                    }
                }
            });
            add(resetBtn, c);


        }
    }

    private class Row extends JPanel {

        private EInfoResult.Field field;
        private JLabel fieldNameLabel;
        private JDateChooser fromDateChooser;
        private JDateChooser toDateChooser;
        private JTextField textField;

        public Row(EInfoResult.Field f) {
            LayoutManager layout = new GridBagLayout();
            setLayout(layout);
            GridBagConstraints c;
            this.field = f;

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            fieldNameLabel = new JLabel();
            fieldNameLabel.setHorizontalAlignment(SwingConstants.LEFT);
            fieldNameLabel.setText(f.getFullName() + ":");
            fieldNameLabel.setToolTipText(f.getDesc());
            add(fieldNameLabel, c);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.anchor = GridBagConstraints.WEST;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;

            JPanel inputPanel = getInputPanel();
            add(inputPanel, c);

        }

        public void reset() {
            if (toDateChooser != null) {
                toDateChooser.setDate(new Date());
            }
            if (fromDateChooser != null) {
                fromDateChooser.setDate(getDefaultFromDate());
            }
            if (textField != null) {
                textField.setText("");
            }
        }

        private Date getDefaultFromDate() {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, 1980);
            calendar.set(Calendar.DAY_OF_YEAR, 1);
            Date date = calendar.getTime();
            return date;
        }

        private JPanel getInputPanel() {
            JPanel ret = new JPanel();
            LayoutManager layout = new GridBagLayout();
            ret.setLayout(layout);
            GridBagConstraints c;

            if (field.isDate()) {
                c = new GridBagConstraints();
                c.gridy = 0;
                Date fromDate = getDefaultFromDate();
                fromDateChooser = new JDateChooser(fromDate);
                fromDateChooser.setLocale(Locale.ENGLISH);
                fromDateChooser.getDateEditor().setEnabled(false);
                ret.add(fromDateChooser, c);

                c = new GridBagConstraints();
                c.gridy = 0;
                c.insets = new Insets(0, 5, 0, 5);
                JLabel to = new JLabel("to");
                ret.add(to, c);

                c = new GridBagConstraints();
                c.gridy = 0;
                toDateChooser = new JDateChooser(new Date());
                toDateChooser.setLocale(Locale.ENGLISH);
                toDateChooser.getDateEditor().setEnabled(false);
                ret.add(toDateChooser, c);

                c = new GridBagConstraints();
                c.gridx = GridBagConstraints.RELATIVE;
                c.gridy = 0;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 0.5;
                Component comp = Box.createRigidArea(new Dimension(1, 1));
                ret.add(comp, c);

            } else {
                c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 1.0;
                c.fill = GridBagConstraints.HORIZONTAL;
                textField = new JTextField();
                ret.add(textField, c);
                PromptSupport.setPrompt(field.getDesc(), textField);
            }
            return ret;
        }

        public void enableChildren(boolean enabled) {
            if (enabled) {
            } else {
            }
        }

        public JLabel getFieldNameLabel() {
            return fieldNameLabel;
        }

        public String getLogicalOperator() {
            String ret = "And";
            //ret = comboBox.getSelectedItem().toString();
            return ret;
        }

        public String getTerm() {
            StringBuilder ret = new StringBuilder();
            if (field.isDate()) {
                Date defaultFromDate = getDefaultFromDate();
                Date fromDate = fromDateChooser.getDate();
                Date toDate = toDateChooser.getDate();

                boolean sameFromDate = DateUtil.isSameDate(fromDate, defaultFromDate);
                boolean sameToDate = DateUtil.isSameDate(toDate, new Date());

                if (!sameFromDate || !sameToDate) {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                    String fromDateStr = dateFormat.format(fromDate);

                    ret.append("(");
                    ret.append(String.format("\"%s\"", fromDateStr));
                    ret.append('[');
                    ret.append(field.getFullName());
                    ret.append(']');

                    ret.append(' ').append(':').append(' ');


                    String toDateStr = dateFormat.format(toDate);
                    ret.append(String.format("\"%s\"", toDateStr));
                    ret.append('[');
                    ret.append(field.getFullName());
                    ret.append(']');
                    ret.append(")");
                }
            } else {
                String text = textField.getText().trim();
                if (!text.isEmpty()) {
                    ret.append(text);
                    ret.append('[');
                    ret.append(field.getFullName());
                    ret.append(']');
                }
            }
            ret.trimToSize();
            return ret.toString();
        }
    }
}
