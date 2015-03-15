/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.enzymesused;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.ren.IRENListService;
import com.gas.domain.core.ren.REN;
import com.gas.domain.core.ren.RENList;
import com.gas.enzyme.core.ui.editor.RENListTableFilter;
import com.gas.enzyme.core.ui.editor.RENListTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.*;
import javax.swing.Box.Filler;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class RENListPanel extends JPanel {

    private IRENListService renListService = Lookup.getDefault().lookup(IRENListService.class);
    private JButton addBtn;
    private JButton removeBtn;
    private FiltersPanel filtersPanelRight;
    private FiltersPanel filtersPanelLeft;
    private RENListTableFilter filterLeft;
    private RENListTableFilter filterRight;
    protected JTable allTable;
    protected JTable newTable;
    RENList renList;
    protected JTextField nameField;
    private DialogDescriptor dialogDescriptor ;
    private java.util.List<String> existingNames = new ArrayList<String>();
    private NotificationLineSupport notificationLineSupport;
    
    public RENListPanel() {
        this(null);
    }

    public RENListPanel(RENList renList) {
        if(renList == null){
            this.renList = new RENList();
        }else{
            this.renList = renList;
        }
        UIUtil.setDefaultBorder(this);
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c ;

        c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        JPanel leftPanel = new JPanel();
        leftPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "All Restriction Enzymes", TitledBorder.LEFT, TitledBorder.TOP));
        initLeftPanel(leftPanel);
        add(leftPanel, c);


        c = new GridBagConstraints();
        c.gridy = 0;
        JPanel middlePanel = new JPanel();
        initMiddlePanel(middlePanel);
        add(middlePanel, c);

        c = new GridBagConstraints();
        c.gridy = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        JPanel rightPanel = new JPanel();
        rightPanel.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Restriction Enzyme List", TitledBorder.LEFT, TitledBorder.TOP));
        initRightPanel(rightPanel);
        add(rightPanel, c);

        initComp();
        hookupListeners();
    }

    public void setExistingNames(java.util.List<String> existingNames) {
        this.existingNames = existingNames;
    }

    public DialogDescriptor getDialogDescriptor() {
        return dialogDescriptor;
    }

    public void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        this.dialogDescriptor.setValid(false);
        this.notificationLineSupport = dialogDescriptor.createNotificationLineSupport();
    }
    
    private void initComp() {
        filtersPanelLeft.bluntBtn.setSelected(filterLeft.isIncludeBlunt());
        filtersPanelLeft.fiveBtn.setSelected(filterLeft.isInclude5());
        filtersPanelLeft.threeBtn.setSelected(filterLeft.isInclude3());
        filtersPanelLeft.palindromicBtn.setSelected(filterLeft.isPalindromesOnly());
        
        filtersPanelRight.bluntBtn.setSelected(filterRight.isIncludeBlunt());
        filtersPanelRight.fiveBtn.setSelected(filterRight.isInclude5());
        filtersPanelRight.threeBtn.setSelected(filterRight.isInclude3());
        filtersPanelRight.palindromicBtn.setSelected(filterRight.isPalindromesOnly());
    }

    private void hookupListeners() {
        addBtn.addActionListener(new RENListPanelListeners.AddBtnListener(this));
        removeBtn.addActionListener(new RENListPanelListeners.RemoveBtnListener(this));
        filtersPanelLeft.bluntBtn.addItemListener(new RENListPanelListeners.FiltersBtnListener(allTable));
        filtersPanelLeft.fiveBtn.addItemListener(new RENListPanelListeners.FiveBtnListener(allTable));
        filtersPanelLeft.threeBtn.addItemListener(new RENListPanelListeners.ThreeBtnListener(allTable));
        filtersPanelLeft.palindromicBtn.addItemListener(new RENListPanelListeners.FiltersBtnListener(allTable));
        
        filtersPanelRight.bluntBtn.addItemListener(new RENListPanelListeners.FiltersBtnListener(newTable));
        filtersPanelRight.fiveBtn.addItemListener(new RENListPanelListeners.FiveBtnListener(newTable));
        filtersPanelRight.threeBtn.addItemListener(new RENListPanelListeners.ThreeBtnListener(newTable));
        filtersPanelRight.palindromicBtn.addItemListener(new RENListPanelListeners.FiltersBtnListener(newTable));
        
        nameField.getDocument().addDocumentListener(new RENListPanelListeners.DocListener(this));
    }

    private void initLeftPanel(JPanel parent) {
        Insets insets = UIUtil.getDefaultHorizontalInsets();
        LayoutManager layout = new GridBagLayout();
        parent.setLayout(layout);
        GridBagConstraints c ;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = insets;
        parent.add(new JLabel("Name:"), c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        parent.add(new JLabel("All Enzymes"), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = insets;
        parent.add(new JLabel("Filter:"), c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        filtersPanelLeft = new FiltersPanel();
        parent.add(filtersPanelLeft, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        allTable = new JTable(){
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
        };

        RENListTableModel tableModel = new RENListTableModel();
        RENList all = renListService.getDefaultAllEnzymeList();
        if(!renList.isEmpty()){
            all.removeByNames(renList.getNames());
        }
        tableModel.set(new ArrayList<REN>(all.getReadOnlyRens()));
        allTable.setModel(tableModel);

        // work on RowSorter only after setting the Model
        allTable.setAutoCreateRowSorter(true);
        TableRowSorter rowSorter = (TableRowSorter) allTable.getRowSorter();
        filterLeft = new RENListTableFilter();
        rowSorter.setRowFilter(filterLeft);

        JScrollPane scrollPane = new JScrollPane(allTable);
        parent.add(scrollPane, c);

    }

    private void initRightPanel(JPanel parent) {
        Insets insets = UIUtil.getDefaultHorizontalInsets();
        LayoutManager layout = new GridBagLayout();
        parent.setLayout(layout);
        GridBagConstraints c ;

        // row one
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.insets = insets;
        parent.add(new JLabel("Name:"), c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        nameField = new JTextField(18);
        if(renList.getName() == null){
            nameField.setText("Untitled");
        }else{
            nameField.setText(renList.getName());
        }
        parent.add(nameField, c);

        // row two
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.insets = insets;
        parent.add(new JLabel("Filter:"), c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        filtersPanelRight = new FiltersPanel();
        parent.add(filtersPanelRight, c);

        // row three
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        newTable = new JTable(){
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
        };
        RENListTableModel tableModel = new RENListTableModel();
        if(!renList.getReadOnlyRens().isEmpty()){
            tableModel.set(new ArrayList<REN>(renList.getReadOnlyRens()));
        }
        newTable.setModel(tableModel);

        // row sorter
        newTable.setAutoCreateRowSorter(true);
        TableRowSorter rowSorter = (TableRowSorter) newTable.getRowSorter();
        filterRight = new RENListTableFilter();
        rowSorter.setRowFilter(filterRight);

        JScrollPane scrollPane = new JScrollPane(newTable);
        parent.add(scrollPane, c);
    }

    private void initMiddlePanel(JPanel parent) {
        LayoutManager layout = null;
        layout = new GridBagLayout();
        parent.setLayout(layout);
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        addBtn = new JButton(ImageHelper.createImageIcon(ImageNames.ARROW_BLACK_RIGHT_16));
        parent.add(addBtn, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;        
        parent.add(new JLabel(), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        removeBtn = new JButton(ImageHelper.createImageIcon(ImageNames.ARROW_BLACK_LEFT_16));
        parent.add(removeBtn, c);
    }

    public RENList getRENList() {  
        renList.setName(nameField.getText());
        if(renList.getCreationDate() == null){
            renList.setCreationDate(new Date());
        }
        renList.setLastModifiedDate(new Date());
        RENListTableModel tableModel = (RENListTableModel)newTable.getModel();
        for(int i = 0; i < tableModel.getRowCount(); i++){
            REN ren = tableModel.getRow(i).getRen();
            renList.addRenIfNecessary(ren);
        }
        StringList namesInTable = tableModel.getNames();
        StringList names = renList.getNames();
        names.removeAll(namesInTable);
        renList.removeByNames(names);
        return renList;
    }
    
    public void validateInput(){
        boolean valid = true;
        String name = nameField.getText();
        if(newTable.getModel().getRowCount() < 1){
            notificationLineSupport.setInformationMessage("Enzyme list is empty");
            valid = false;
        }else if(name.isEmpty()){
            notificationLineSupport.setInformationMessage("Enzyme list name is empty");
            valid = false;
        }else{
            for(String n : existingNames){
                if(n.equalsIgnoreCase(name)){
                    notificationLineSupport.setInformationMessage(String.format("Name '%s' already exists.", name));
                    valid = false;
                    break;
                }
            }
        }
        if(valid){
            notificationLineSupport.clearMessages();
        }
        dialogDescriptor.setValid(valid);
    }

    protected static class FiltersPanel extends JPanel {

        protected JCheckBox bluntBtn;
        protected JCheckBox fiveBtn;
        protected JCheckBox threeBtn;
        protected JCheckBox palindromicBtn;

        public FiltersPanel() {
            LayoutManager layout = null;
            layout = new GridBagLayout();
            setLayout(layout);
            GridBagConstraints c = null;

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 3);
            bluntBtn = new JCheckBox("Blunt end");
            bluntBtn.setActionCommand("blunt");
            add(bluntBtn, c);

            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 3);
            fiveBtn = new JCheckBox("5' end");
            fiveBtn.setActionCommand("five");
            add(fiveBtn, c);

            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 3);
            threeBtn = new JCheckBox("3' end");
            threeBtn.setActionCommand("three");
            add(threeBtn, c);
            
            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.gridy = 0;
            c.insets = new Insets(0, 0, 0, 3);
            palindromicBtn = new JCheckBox("Palindromic Only");
            palindromicBtn.setActionCommand("palindromic");
            add(palindromicBtn, c);

            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            Box.Filler filler = (Filler) Box.createRigidArea(new Dimension(1, 1));
            add(filler, c);
        }
    }
}
