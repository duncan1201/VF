/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewQualifierPanel.java
 *
 * Created on Jan 17, 2012, 9:58:21 AM
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.api.IFetureKeyService;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class NewQualPanel extends JPanel {

    JCheckBox featureTypeOnly;
    JComboBox qualifierComboBox;
    private JTextField valueTextField;
    private String value;
    private String qName;
    String fk;
    private NotificationLineSupport notificationLineSupport;
    private DialogDescriptor dialogDescriptor;
    private IFetureKeyService service = Lookup.getDefault().lookup(IFetureKeyService.class);

    /**
     * Creates new form NewQualifierPanel
     */
    public NewQualPanel(String fk, String qualifierLine) {
        this.fk = fk;
        initComponents(fk);

        //AutoCompleteDecorator.decorate(fetureTypeComboBox);
        AutoCompleteDecorator.decorate(qualifierComboBox);
        if (qualifierLine != null) {
            String[] splits = qualifierLine.split("=");
            qualifierComboBox.setSelectedItem(splits[0]);
            setValue(splits[1]);
            valueTextField.setText(splits[1]);
        }
        if (qualifierComboBox.getItemCount() > 0) {
            setqName(qualifierComboBox.getSelectedItem().toString());
        }
        setEditOnly(fk != null && qualifierLine != null);
        hookupListener();
    }
    
    void setDialogDescriptor(DialogDescriptor dialogDescriptor){
        this.dialogDescriptor = dialogDescriptor;
        notificationLineSupport = dialogDescriptor.createNotificationLineSupport();
    }

    void validateInput(){
        if(dialogDescriptor == null){
            return;
        }
        if(valueTextField.getText().isEmpty()){
            notificationLineSupport.setInformationMessage("Value cannot be empty");
        }else{
            notificationLineSupport.clearMessages();            
        }
        dialogDescriptor.setValid(!valueTextField.getText().isEmpty());
    }

    private void hookupListener() {
        featureTypeOnly.addItemListener(new NewQualPanelListeners.FeatureTypeOnlyListener(this));
        valueTextField.getDocument().addDocumentListener(new NewQualPanelListeners.ValueListener(this));
        qualifierComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                qualifierComboBoxActionPerformed(evt);
            }
        });
    }

    public void setEditOnly(boolean editOnly) {
        if (editOnly) {
            qualifierComboBox.setEnabled(false);
            featureTypeOnly.setEnabled(false);
        } else {
            qualifierComboBox.setEnabled(true);
            featureTypeOnly.setEnabled(true);
        }
    }

    public String getqName() {
        return qName;
    }

    public String getQNameLine() {
        StringBuilder ret = new StringBuilder();
        ret.append(qName);
        ret.append("=");
        ret.append(value);
        return ret.toString();
    }

    public void setqName(String qName) {
        this.qName = qName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private void initComponents(String fk) {
        UIUtil.setDefaultBorder(this);
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        featureTypeOnly = new JCheckBox(String.format("Qualifiers of type %s only", fk));
        add(featureTypeOnly, c);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.gridy = gridy;
        add(new JLabel("Name:"), c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridy = gridy++;
        qualifierComboBox = new JComboBox();
        qualifierComboBox.setEditable(true);
        List<String> allQualifiers = service.getAllQualifiers();
        DefaultComboBoxModel qualComboBoxModel = new DefaultComboBoxModel(allQualifiers.toArray(new String[allQualifiers.size()]));
        qualifierComboBox.setModel(qualComboBoxModel);
        add(qualifierComboBox, c);

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        c.gridy = gridy;
        add(new JLabel("Value:"), c);

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridy = gridy++;
        valueTextField = new JTextField();
        add(valueTextField, c);
    }

    private void qualifierComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        if (qualifierComboBox != null) {
            String selected = qualifierComboBox.getSelectedItem().toString();
            setqName(selected);
        }
    }

}
