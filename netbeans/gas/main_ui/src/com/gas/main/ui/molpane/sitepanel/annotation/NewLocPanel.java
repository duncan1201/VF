/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewLocPanel.java
 *
 * Created on Jan 17, 2012, 3:59:59 PM
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.dialog.IVDialog;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Pozition;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.openide.DialogDescriptor;
import org.openide.NotificationLineSupport;

/**

 @author dq
 */
public class NewLocPanel extends JPanel  {
    
    JSpinner fromSpinner;
    private JCheckBox knownEndCheckBox;
    private JCheckBox knownStartCheckBox;
    private JSpinner toSpinner;
    private boolean circular;
    private int totalPos;
    private Pozition poz;
    private LocList prohibited;
    private DialogDescriptor dialogDescriptor ;
    private NotificationLineSupport notificationLineSupport;

    /**
     Creates new form NewLocPanel
     */
    public NewLocPanel(Pozition poz, int totalPos, boolean circular, LocList prohibited) {
        this.poz = poz;
        this.circular = circular;
        this.totalPos = totalPos;
        this.prohibited = prohibited;
        initComponents();
        
        hookupListeners();

        initValues();
    }
    
    private void initValues(){
        final SpinnerNumberModel fromModel = (SpinnerNumberModel) fromSpinner.getModel();
        fromModel.setValue(poz.getStart());
        if (!circular) {
            fromModel.setMinimum(1);            
            fromModel.setMaximum(poz.getEnd());            
        } else {
            fromModel.setMinimum(0);
            fromModel.setMaximum(totalPos + 1);
        }

        final SpinnerNumberModel toModel = (SpinnerNumberModel) toSpinner.getModel();
            toModel.setValue(poz.getEnd());        
        if (!circular) {
            toModel.setMaximum(totalPos);
            toModel.setMinimum(poz.getStart());
        } else {
            toModel.setMinimum(0);
            toModel.setMaximum(totalPos + 1);
        }

        knownStartCheckBox.setSelected(!poz.isFuzzyStart());
        knownEndCheckBox.setSelected(!poz.isFuzzyEnd());    
        
    }
    
    protected void setDialogDescriptor(DialogDescriptor dialogDescriptor){
        this.dialogDescriptor = dialogDescriptor;
        this.notificationLineSupport = dialogDescriptor.createNotificationLineSupport();
    }

    public boolean isCircular() {
        return circular;
    }
    
    protected JSpinner getToSpinner(){
        return toSpinner;
    }
   
    public int getTotalPos() {
        return totalPos;
    }

    private void hookupListeners() {
        fromSpinner.addChangeListener(new NewLocPanelListeners.FromListener(new WeakReference<NewLocPanel>(this)));
        toSpinner.addChangeListener(new NewLocPanelListeners.ToListener(new WeakReference<NewLocPanel>(this)));
    }

    public Pozition getPozition() {
        poz = poz.clone();
        poz.setStart((Integer) fromSpinner.getValue());
        poz.setEnd((Integer) toSpinner.getValue());
        poz.setFuzzyStart(!knownStartCheckBox.isSelected());
        poz.setFuzzyEnd(!knownEndCheckBox.isSelected());
        poz.setType("..");
        return poz;
    }

    private void initComponents() {

        setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));
        
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("From:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        fromSpinner = new JSpinner();
        fromSpinner.setModel(new SpinnerNumberModel(1, 1, null, 1));
        add(fromSpinner, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        knownStartCheckBox = new JCheckBox("Known boundary");
        add(knownStartCheckBox, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        add(new JLabel("To:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        toSpinner = new JSpinner();
        toSpinner.setModel(new SpinnerNumberModel(1, 1, null, 1));
        add(toSpinner, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        knownEndCheckBox = new JCheckBox("Known boundary");
        add(knownEndCheckBox, c);
           
        Dimension spinnerSize = UIUtil.getSize(totalPos * 10, JSpinner.class);
        
        UIUtil.setPreferredWidth(toSpinner, spinnerSize.width);
        UIUtil.setPreferredWidth(fromSpinner, spinnerSize.width);
    }
  

    protected void validateInput() {
        if(notificationLineSupport == null || dialogDescriptor == null){
            return ;
        }
        Integer from = (Integer) fromSpinner.getValue();
        Integer to = (Integer)toSpinner.getValue();
        
        Loc intersect = prohibited.intersect(from, to);
        
        if(intersect != null){
            notificationLineSupport.setInformationMessage(String.format("The location overlaps with (%s)", intersect.toString(false, false)));
        }else{
            notificationLineSupport.clearMessages();            
        }
        
        dialogDescriptor.setValid(intersect == null);        
    }

}
