/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewFetureKeyPanel.java
 *
 * Created on Jan 18, 2012, 10:18:51 PM
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.dialog.DialogUtil;
import com.gas.common.ui.dialog.IVDialog;
import com.gas.domain.core.as.FetureKey;
import com.gas.domain.core.as.api.IFetureKeyService;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class NewFetureKeyPanel extends javax.swing.JPanel implements IVDialog {

    private String type;
    private FetureKey fetureKey;
    private DefaultListModel listModel = new DefaultListModel();

    /** Creates new form NewFetureKeyPanel */
    public NewFetureKeyPanel() {
        initComponents();
    }

    public FetureKey getFetureKey() {
        if(fetureKey == null){
            fetureKey = new FetureKey();
        }
        fetureKey.setName(type);
        int size = listModel.getSize();
        for (int i = 0; i < size; i++) {
            String str = (String)listModel.get(i);
            fetureKey.getQualifiers().add(str);
        }
        return fetureKey;
    }

    public void setFetureKey(FetureKey fetureKey) {
        this.fetureKey = fetureKey;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DefaultListModel getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel listModel) {
        this.listModel = listModel;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        typeText = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        qualifierList = new javax.swing.JList();
        addBtn = new javax.swing.JButton();
        delBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();

        jLabel1.setText("Type:"); // NOI18N

        jPanel1.setBorder(BorderFactory.createTitledBorder( //
                new LineBorder(Color.GRAY), //
                "Qualifiers", //
                TitledBorder.LEFT, //
                TitledBorder.TOP)); // NOI18N

        qualifierList.setModel(getListModel());
        qualifierList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        qualifierList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                qualifierListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(qualifierList);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
        );

        addBtn.setText("Add..."); // NOI18N
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        delBtn.setText(org.openide.util.NbBundle.getMessage(NewFetureKeyPanel.class, "NewFetureKeyPanel.delBtn.text")); // NOI18N
        delBtn.setEnabled(false);
        delBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delBtnActionPerformed(evt);
            }
        });

        editBtn.setText(org.openide.util.NbBundle.getMessage(NewFetureKeyPanel.class, "NewFetureKeyPanel.editBtn.text")); // NOI18N
        editBtn.setEnabled(false);
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(typeText, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 63, Short.MAX_VALUE)
                    .addComponent(editBtn, 0, 0, Short.MAX_VALUE)
                    .addComponent(delBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(addBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(editBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(delBtn))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

    }// </editor-fold>//GEN-END:initComponents

    private void delBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delBtnActionPerformed
        int index = qualifierList.getSelectedIndex();
        if (index > -1) {
            listModel.remove(index);
        }
        int size = listModel.getSize();
        if (size > 0) {
            if (index < size - 1) {
                qualifierList.setSelectedIndex(index);
            } else {
                qualifierList.setSelectedIndex(size - 1);
            }
        }
    }//GEN-LAST:event_delBtnActionPerformed

    private void qualifierListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_qualifierListValueChanged
        int size = listModel.getSize();
        int selected = qualifierList.getSelectedIndex();
        editBtn.setEnabled(size > 0 && selected > -1);
        delBtn.setEnabled(size > 0 && selected > -1);
    }//GEN-LAST:event_qualifierListValueChanged

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        NewQualifierNamePanel newQualifierNamePanel = new NewQualifierNamePanel("");

        Integer answer = DialogUtil.notify(newQualifierNamePanel, "New Qualifier");

        if (answer == JOptionPane.OK_OPTION) {
            String qName = newQualifierNamePanel.getQualifierName();
            listModel.insertElementAt(qName, 0);
            qualifierList.setSelectedIndex(0);
        }
    }//GEN-LAST:event_addBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        int index = qualifierList.getSelectedIndex();
        String name = (String)listModel.getElementAt(index);
        NewQualifierNamePanel newQualifierNamePanel = new NewQualifierNamePanel(name);
        DialogDescriptor dd = new DialogDescriptor(newQualifierNamePanel, "Edit Qualifier");
        DialogDisplayer.getDefault().notify(dd);
    }//GEN-LAST:event_editBtnActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JButton delBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList qualifierList;
    private javax.swing.JTextField typeText;
    // End of variables declaration//GEN-END:variables

    private List<String> errorMessages = new ArrayList<String>();
    
    @Override
    public boolean validateInput() {
        boolean valid = true;
        if(type == null || type.isEmpty()){
            errorMessages.add("Feature key is empty");
            valid = false;            
        }
        IFetureKeyService s = Lookup.getDefault().lookup(IFetureKeyService.class);
        boolean isPresent = s.isPresent(type);
        if (isPresent && valid) {            
            errorMessages.add(String.format("Feature key '%s' is present in the database already. Please choose a different name.", type));
            valid = false;
        }
        return valid;
    }

    @Override
    public List<String> getErrorMessage() {
        return errorMessages;
    }
    
    private int closedButton = Integer.MIN_VALUE;

    @Override
    public int getClosedButton() {
        return closedButton;
    }

    @Override
    public void setClosedButton(int closedButton) {
        this.closedButton = closedButton;
    }
}
