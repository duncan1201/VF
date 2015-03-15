/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.new_;

import java.awt.GridBagLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

/**
 *
 * @author dq
 */
public class SelectOrganismPanel extends JPanel {
    
    private JComboBox organismCombo;
    
    SelectOrganismPanel(){
        createComponents();
        hookupListeners();
    }
    
    public String getSelectedOrganism(){
        return (String)organismCombo.getSelectedItem();
    }
    
    private void createComponents(){
        organismCombo = new JComboBox();
        String[] tset = {"abc", "abccc", "bcde", "cdef", "defe", "eefe"};
        DefaultComboBoxModel model = new DefaultComboBoxModel(tset);
        organismCombo.setModel(model);
        add(organismCombo);
        
        AutoCompleteDecorator.decorate(organismCombo);
    }
    
    private void hookupListeners(){
    }
}
