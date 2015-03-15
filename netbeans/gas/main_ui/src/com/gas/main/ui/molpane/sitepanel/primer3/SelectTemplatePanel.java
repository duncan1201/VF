/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.database.core.primer.service.api.IUserInputService;
import com.gas.domain.core.primer3.UserInput;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.ref.WeakReference;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class SelectTemplatePanel extends JPanel {

    private IUserInputService service = Lookup.getDefault().lookup(IUserInputService.class);
    private WeakReference<UserInputTable> tableRef;
    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;    
    
    SelectTemplatePanel() {
        super(new GridBagLayout());

        UserInputTable table = new UserInputTable();
        table.setEditable(false);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                validateInput();
            }
        });
        tableRef = new WeakReference<UserInputTable>(table);
        List<UserInput> userInputs = service.getAll(true);
        table.setUserInputs(userInputs);

        GridBagConstraints c = new GridBagConstraints();
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, c);
    }

    public void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        this.notificationLineSupport = dialogDescriptor.createNotificationLineSupport();
    }
    
    void validateInput(){
        UserInput userInput = getSelectedUserInput();
        if(userInput == null){
            dialogDescriptor.setValid(false);
            notificationLineSupport.setInformationMessage("Please select a setting");
        }else{
            dialogDescriptor.setValid(true);
            notificationLineSupport.clearMessages();
        }
    }
    
    public UserInput getSelectedUserInput() {
        UserInputTable table = tableRef.get();
        UserInputTable.UserInputTableModel model = (UserInputTable.UserInputTableModel) table.getModel();
        int selected = table.getSelectedRow();        
        UserInput ret = model.getRow(table.convertRowIndexToModel(selected));
        return ret;
    }
}