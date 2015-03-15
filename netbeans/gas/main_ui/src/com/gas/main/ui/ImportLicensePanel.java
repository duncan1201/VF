/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.util.FileHelper;
import com.gas.common.ui.util.UIUtil;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.swingx.JXHyperlink;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**
 *
 * @author dq
 */
class ImportLicensePanel extends JPanel implements ActionListener, DocumentListener {

    final static String TITLE = MSG.Import_A_LICENSE;
    JXHyperlink link;
    JTextArea textArea;
    DialogDescriptor dialogDescriptor;
    NotificationLineSupport notificationSupport;

    ImportLicensePanel() {
        UIUtil.setDefaultBorder(this);
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("Please enter or "), c);

        c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        link = new JXHyperlink();
        link.setText("import");
        link.setClicked(false);
        link.setFocusable(false);
        link.setOverrulesActionOnClick(true);        
        add(link, c);

        c = new GridBagConstraints();
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel(" a license"), c);

        c = new GridBagConstraints();
        c.gridwidth = 3;
        c.gridy = 1;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        textArea = new JTextArea(6, 40);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(false);
        add(textArea, c);

        hookupListeners();
    }

    private void hookupListeners() {
        link.addActionListener(this);
        textArea.getDocument().addDocumentListener(this);
    }

    void setDialogDescriptor(DialogDescriptor dialogDescriptor) {
        this.dialogDescriptor = dialogDescriptor;
        notificationSupport = this.dialogDescriptor.createNotificationLineSupport();
    }

    void validateInput() {
        if (dialogDescriptor == null) {
            return;
        }
        String text = textArea.getText().trim();
        if (text.isEmpty()) {
            dialogDescriptor.setValid(false);
            notificationSupport.setInformationMessage(MSG.Please_enter_a_license);
        } else {
            dialogDescriptor.setValid(true);
            notificationSupport.clearMessages();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setMultiSelectionEnabled(false);
        fc.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter nameFilter = new FileNameExtensionFilter("VectorFriends license", "txt");
        fc.addChoosableFileFilter(nameFilter);
        int answer = UIUtil.showDialog(fc, this);        
        if (answer == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            String str = FileHelper.toStringBuffer(file).toString();
            textArea.setText(str);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        validateInput();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        validateInput();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        validateInput();
    }
}
