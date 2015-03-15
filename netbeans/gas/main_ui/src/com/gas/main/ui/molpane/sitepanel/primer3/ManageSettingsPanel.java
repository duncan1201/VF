/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.primer.service.api.IUserInputService;
import com.gas.domain.core.primer3.UserInput;
import com.gas.main.ui.molpane.sitepanel.primer3.UserInputTable.UserInputTableModel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ManageSettingsPanel extends JPanel {

    private IUserInputService service = Lookup.getDefault().lookup(IUserInputService.class);
    private WeakReference<JButton> newBtnRef;
    private WeakReference<JButton> delBtnRef;
    private WeakReference<JButton> importBtnRef;
    private WeakReference<JButton> exportBtnRef;
    private WeakReference<UserInputTable> tableRef;
    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;

    public ManageSettingsPanel() {
        initComponents();
        hookupListeners();
    }

    private void hookupListeners() {
        tableRef.get().getSelectionModel().addListSelectionListener(new ManageSettingsPanelListeners.TableListener(this));

        newBtnRef.get().addActionListener(new ManageSettingsPanelListeners.NewListener(this));
        delBtnRef.get().addActionListener(new ManageSettingsPanelListeners.DelListener(this));
        importBtnRef.get().addActionListener(new ManageSettingsPanelListeners.ImportListener(this));
        exportBtnRef.get().addActionListener(new ManageSettingsPanelListeners.ExportListener(this));
    }

    public void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        this.notificationLineSupport = dialogDescriptor.createNotificationLineSupport();
    }

    protected void validateInput() {
        dialogDescriptor.setValid(true);       
        notificationLineSupport.clearMessages();        
    }

    protected JButton getImportBtn() {
        return importBtnRef.get();
    }

    protected JButton getExportBtn() {
        return exportBtnRef.get();
    }

    protected JButton getDelBtn() {
        return delBtnRef.get();
    }

    public List<UserInput> getUserInputs() {
        UserInputTableModel model = (UserInputTableModel) tableRef.get().getModel();
        return model.getUserInputs();
    }

    protected UserInput getSelectedUserInput() {
        int row = tableRef.get().getSelectedRow();
        UserInputTableModel model = (UserInputTableModel) tableRef.get().getModel();
        int indexModel = tableRef.get().convertRowIndexToModel(row);
        return model.getRow(indexModel);
    }

    protected int getSelectionIndex() {
        ListSelectionModel model = tableRef.get().getSelectionModel();
        return model.getLeadSelectionIndex();
    }

    protected void setFavoriteSetting() {
        UserInputTableModel tableModel = (UserInputTableModel) tableRef.get().getModel();
        tableModel.setValueAt(true, 0, 0);
    }

    /**
     * @param selectionIndex 0-based
     */
    protected void setSelection(int selectionIndex) {
        int rowCount = tableRef.get().getRowCount();
        ListSelectionModel model = tableRef.get().getSelectionModel();
        if (selectionIndex < rowCount) {
            model.setLeadSelectionIndex(0);
        } else {
            model.setLeadSelectionIndex(selectionIndex - 1);
        }
    }

    protected int getRowCount() {
        return tableRef.get().getRowCount();
    }

    protected void deleteSelectdUserInput() {
        int row = tableRef.get().getSelectedRow();
        UserInputTableModel model = (UserInputTableModel) tableRef.get().getModel();
        int indexModel = tableRef.get().convertRowIndexToModel(row);
        model.deleteRow(indexModel);
    }

    protected String getNewName(String proposedName) {
        UserInputTableModel model = (UserInputTableModel) tableRef.get().getModel();
        return model.getNewName(proposedName);
    }

    protected void addUserInput(UserInput userInput) {
        UserInputTableModel model = (UserInputTableModel) tableRef.get().getModel();
        model.addRow(userInput);
    }

    private void initComponents() {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        List<UserInput> userInput = service.getAll(true);
                
        UserInputTable table = new UserInputTable() ;
        table.setUserInputs(userInput);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableRef = new WeakReference<UserInputTable>(table);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        JPanel ctrlPanel = createCtrlPanel();
        add(ctrlPanel, c);
    }

    private JPanel createCtrlPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton newBtn = new JButton("New");
        newBtnRef = new WeakReference<JButton>(newBtn);
        ret.add(newBtn, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton delBtn = new JButton("Delete");
        delBtn.setEnabled(false);
        delBtnRef = new WeakReference<JButton>(delBtn);
        ret.add(delBtn, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton importBtn = new JButton("Import");
        importBtnRef = new WeakReference<JButton>(importBtn);
        ret.add(importBtn, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton exportBtn = new JButton("Export");
        exportBtn.setEnabled(false);
        exportBtnRef = new WeakReference<JButton>(exportBtn);
        ret.add(exportBtn, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1;
        Component comp = Box.createRigidArea(new Dimension(1, 1));
        ret.add(comp, c);

        return ret;
    }
}
