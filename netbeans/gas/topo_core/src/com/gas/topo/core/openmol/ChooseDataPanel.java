/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.openmol;

import com.gas.common.ui.treecombo.JTreeComboFactory;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.filesystem.FolderHelper;
import com.gas.domain.core.filesystem.FolderNames;
import com.gas.domain.ui.dynamicTable.DynamicColumnFactoryCalibrator;
import com.gas.domain.ui.dynamicTable.DynamicTable;
import com.gas.domain.ui.dynamicTable.DynamicTableModel;
import com.gas.domain.ui.explorer.FolderMutableTreeNode;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ChooseDataPanel extends JPanel {

    private DynamicTable dynamicTable;
    private JComboBox comboBox;
    private Folder.TYPE[] includeTypes;
    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;
    private List<IChooseDataPanelValidator> validators = new ArrayList<IChooseDataPanelValidator>();

    public ChooseDataPanel() {
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        final int charWidth = FontUtil.getFontMetrics(getDynamicTable()).charWidth('A');
        final Insets insets = new Insets(charWidth / 2, charWidth, 0, charWidth);
        c.insets = insets;
        JPanel panel = createCtrlPanel();
        add(panel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = insets;
        JScrollPane scrollPane = new JScrollPane(getDynamicTable());
        final int width = (2 * 4 + 6 * 15) * charWidth;
        UIUtil.setPreferredWidth(scrollPane, width);
        add(scrollPane, c);

        comboBox.setSelectedIndex(0);
        hookupListeners();
    }

    private void hookupListeners() {
        comboBox.addActionListener(new ChooseDataPanelListeners.InsertComboListener(this));
        getDynamicTable().getSelectionModel().addListSelectionListener(new ChooseDataPanelListeners.TableSelectionModel(this));
    }

    public List<IChooseDataPanelValidator> getValidators() {
        return validators;
    }

    public void setValidators(List<IChooseDataPanelValidator> validators) {
        this.validators = validators;
        for (IChooseDataPanelValidator validator : validators) {
            validator.setRef(new WeakReference<ChooseDataPanel>(this));
        }
    }

    public void validateInput() {
        boolean valid = true;
        for (IChooseDataPanelValidator validator : validators) {
            boolean result = validator.validate();
            if (!result) {
                valid = false;
                notificationLineSupport.setInformationMessage(validator.getFriendlyMessage());
                break;
            }
        }
        if (valid) {
            notificationLineSupport.clearMessages();
        }
        dialogDescriptor.setValid(valid);
    }

    public void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        this.dialogDescriptor.setValid(false);
        notificationLineSupport = this.dialogDescriptor.createNotificationLineSupport();
    }

    public Folder.TYPE[] getIncludeTypes() {
        return includeTypes;
    }

    public void setIncludeTypes(Folder.TYPE... includeTypes) {
        this.includeTypes = includeTypes;
    }

    private JPanel createCtrlPanel() {
        JPanel ret = new JPanel();
        LayoutManager layout = new GridBagLayout();
        ret.setLayout(layout);
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        ret.add(new JLabel("Selected Folder:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.5;
        c.insets = new Insets(0, 3, 0, 3);
        comboBox = JTreeComboFactory.createTreeCombo(createMyDataTreeModel());

        ret.add(comboBox, c);

        return ret;
    }

    public DynamicTable getDynamicTable() {
        if (dynamicTable == null) {
            dynamicTable = new DynamicTable();
            dynamicTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            dynamicTable.setDoubleClickEnabled(false);
            dynamicTable.setOpenEditorSupported(false);
            dynamicTable.setEditable(false);
            dynamicTable.setColumnControlVisible(false);
            
            DynamicColumnFactoryCalibrator calibrator = new DynamicColumnFactoryCalibrator();
            calibrator.configureDisplayProperties(dynamicTable);

            DynamicTableModel model = new DynamicTableModel();
            dynamicTable.setModel(model);
            dynamicTable.setColumnControlVisible(false);
        }
        return dynamicTable;
    }

    private DefaultTreeModel createMyDataTreeModel() {
        IFolderService s = Lookup.getDefault().lookup(IFolderService.class);
        final Folder folderTree = s.getFolderTree();
        folderTree.removeChild(FolderNames.NCBI_ROOT);
        folderTree.getChild(FolderNames.MY_DATA).removeChild(FolderNames.RECYCLE_BIN);
        FolderHelper.filterByTypes(folderTree, Folder.TYPE.DNA, Folder.TYPE.RNA);
        FolderMutableTreeNode node = new FolderMutableTreeNode(folderTree.getChild(FolderNames.MY_DATA));
        DefaultTreeModel ret = new DefaultTreeModel(node);
        return ret;
    }    
}
