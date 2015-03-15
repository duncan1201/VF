/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import com.gas.common.ui.core.CharList;
import com.gas.common.ui.core.CharSet;
import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.UIUtil;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.jdesktop.swingx.JXTextField;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

public class InputPanel extends JPanel {

    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;
    private WeakReference<JTextField> fieldRef;
    private WeakReference<JTextField> fieldDescRef;
    private String inputLabel;
    private String descLabel;
    private StringList existingNames = new StringList();
    private CharList forbiddenChars = new CharList();

    public InputPanel(String input) {
        this(input, null);
    }
    
    public InputPanel(String inputLabel, String descLabel) {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        this.inputLabel = inputLabel;
        this.descLabel = descLabel;
        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel(inputLabel), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        Dimension size = UIUtil.getSize("Prototype Prototype 12", JTextField.class);
        JTextField field = new JTextField();
        fieldRef = new WeakReference<JTextField>(field);
        UIUtil.setPreferredWidth(field, size.width);
        add(field, c);
        
        if(this.descLabel != null){
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 1;
            c.anchor = GridBagConstraints.EAST;
            add(new JLabel(this.descLabel), c);
            
            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.gridy = 1;            
            c.fill = GridBagConstraints.HORIZONTAL;
            Dimension sizeDesc = UIUtil.getSize("Prototype Prototype 1234567890", JTextField.class);
            JTextField fieldDesc = new JXTextField();
            fieldDescRef = new WeakReference<JTextField>(fieldDesc);
            UIUtil.setPreferredWidth(fieldDesc, sizeDesc.width);
            add(fieldDesc, c);
        }
    }

    public CharList getForbiddenChars() {
        return forbiddenChars;
    }

    public void setForbiddenChars(CharList forbiddenChars) {
        this.forbiddenChars = forbiddenChars;
    }

    public void setForbiddenChars(char... chars) {
        this.forbiddenChars = new CharList(chars);
    }

    public void setInitInputText(String initInputText) {
        fieldRef.get().setText(initInputText);
        fieldRef.get().selectAll();
    }

    public void setExistingNames(String... existingNames) {
        this.existingNames = new StringList(existingNames);
    }

    public void setExistingNames(Collection<String> names) {
        this.existingNames = new StringList(names);
    }

    private void hookupListeners() {
        fieldRef.get().getDocument().addDocumentListener(new InputPanelListeners.FieldListener(this));
    }

    public String getInputText() {
        return fieldRef.get().getText();
    }
    
    public String getDescText(){
        String ret = null;
        if(fieldDescRef != null && fieldDescRef.get() != null){
            ret = fieldDescRef.get().getText();
        }
        return ret;
    }

    public void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        notificationLineSupport = this.dialogDescriptor.createNotificationLineSupport();
    }

    public void validateInput() {
        String txt = getInputText();
        if (dialogDescriptor == null || notificationLineSupport == null) {
            return;
        }
        CharSet intersect = forbiddenChars.intersect(txt.toCharArray());
        if (txt.isEmpty()) {
            dialogDescriptor.setValid(false);
            notificationLineSupport.setInformationMessage("Cannot have an empty name");
        } else if (existingNames.contains(txt)) {
            dialogDescriptor.setValid(false);
            notificationLineSupport.setInformationMessage("Duplicate name");
        } else if (!intersect.isEmpty()) {
            dialogDescriptor.setValid(false);
            notificationLineSupport.setInformationMessage(String.format("\"%s\" %s not allowed", intersect.toString(), intersect.size() > 1 ? "are" : "is"));
        } else {
            dialogDescriptor.setValid(true);
            notificationLineSupport.clearMessages();
        }
    }
}