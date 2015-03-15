/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.UIUtil;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import static javax.swing.JComponent.TOOL_TIP_TEXT_KEY;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**
 *
 * @author dq
 */
public class DelConfirmPanel extends JPanel {

    private List<JCheckBox> cbs = new ArrayList<JCheckBox>();
    private DialogDescriptor dialogDescriptor;
    private NotificationLineSupport notificationLineSupport;

    DelConfirmPanel(String msg, StringList strList) {
        this(msg, null, strList);
    }

    DelConfirmPanel(String msg, Icon icon, StringList strList) {
        UIUtil.setDefaultBorder(this);
        Insets insets = UIUtil.getDefaultInsets();
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.insets = new Insets(0, 0, insets.bottom * 2, 0);
        JLabel label = new JLabel(msg);
        if (icon != null) {
            label.setIcon(icon);
        }
        add(label, c);
        
        for (String str : strList) {
            c = new GridBagConstraints();
            c.gridx = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.insets = new Insets(0, insets.left, 0, 0);
            JCheckBox cb = new JCheckBox(str, true);
            cbs.add(cb);
            add(cb, c);
        }
        hookupListeners();
    }
    
    private void hookupListeners(){
        DelConfirmPanelListeners.BoxListener boxListener = new DelConfirmPanelListeners.BoxListener(this);
        for(JCheckBox cb: cbs){
            cb.addItemListener(boxListener);
        }
    }

    public void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        notificationLineSupport = this.dialogDescriptor.createNotificationLineSupport();
    }

    void validateInput() {
        StringList selectedText = getSelectedText();
        if (selectedText.isEmpty()) {
            notificationLineSupport.setInformationMessage("Please select at least one item");
            this.dialogDescriptor.setValid(false);
        } else {
            notificationLineSupport.clearMessages();
            this.dialogDescriptor.setValid(true);
        }
    }

    public StringList getSelectedText() {
        StringList ret = new StringList();
        for (JCheckBox cb : cbs) {
            if (cb.isSelected()) {
                ret.add(cb.getText());
            }
        }
        return ret;
    }

    public StringList getUnselectedText() {
        StringList ret = new StringList();
        for (JCheckBox cb : cbs) {
            if (!cb.isSelected()) {
                ret.add(cb.getText());
            }
        }
        return ret;
    }
}
