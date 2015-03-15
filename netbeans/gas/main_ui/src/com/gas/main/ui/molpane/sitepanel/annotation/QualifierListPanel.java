/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * QualifierListPanel.java
 *
 * Created on Jan 21, 2012, 1:21:03 PM
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.core.StringComparator;
import com.gas.common.ui.dialog.IVDialog;
import com.gas.domain.core.as.api.IFetureKeyService;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class QualifierListPanel extends javax.swing.JPanel implements IVDialog{

    private DefaultListModel listModel ;
    
    /** Creates new form QualifierListPanel */
    public QualifierListPanel() {
        initComponents();
    }

    public DefaultListModel getListModel() {
        if(listModel == null){
            IFetureKeyService s = Lookup.getDefault().lookup(IFetureKeyService.class);
            List<String> qualifiers = s.getAllQualifiers();
            Collections.sort(qualifiers, new StringComparator());
            listModel = new DefaultListModel();
            for(String qualifier : qualifiers){
                listModel.addElement(qualifier);
            }
        }
        return listModel;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        qualifierList = new javax.swing.JList();

        jPanel1.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.LIGHT_GRAY), "All Qualifiers", TitledBorder.LEADING, TitledBorder.TOP));

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    public String getSelected() {
        return selected;
    }
    
    private void qualifierListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_qualifierListValueChanged
        
        int index = qualifierList.getSelectedIndex();
        selected = (String)listModel.getElementAt(index);                    
    }//GEN-LAST:event_qualifierListValueChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList qualifierList;
    // End of variables declaration//GEN-END:variables
    private String selected ;
    
    
    @Override
    public boolean validateInput() {
        return true;
    }

    private List<String> errorMessage = new ArrayList<String>();
    
    @Override
    public List<String> getErrorMessage() {
        return errorMessage;
    }

    private int closedButton = Integer.MAX_VALUE;
    
    @Override
    public int getClosedButton() {
        return closedButton;
    }

    @Override
    public void setClosedButton(int closedButton) {
    }
}