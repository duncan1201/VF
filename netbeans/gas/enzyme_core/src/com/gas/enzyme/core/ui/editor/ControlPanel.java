/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.editor;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
class ControlPanel extends JPanel {
    
    JButton newBtn;
    JButton editBtn;
    JButton removeBtn;
    
    public ControlPanel(){
        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        newBtn = new JButton("New List", ImageHelper.createImageIcon(ImageNames.PLUS_16));
        FlatBtn.decorate(newBtn, true);
        add(newBtn, c);
        
        editBtn = new JButton("Edit List", ImageHelper.createImageIcon(ImageNames.EDIT_16));
        FlatBtn.decorate(editBtn, true);
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        add(editBtn, c);
        
        removeBtn = new JButton("Remove selected enzymes", ImageHelper.createImageIcon(ImageNames.MINUS_16));
        FlatBtn.decorate(removeBtn, true);
        removeBtn.setEnabled(false);
        c = new GridBagConstraints();        
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        add(removeBtn, c);
        
        Component filler = Box.createRigidArea(new Dimension(1,1));
        c = new GridBagConstraints();        
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(filler, c);
        
        hookupListeners();
    }
    
    private void hookupListeners(){
        newBtn.addActionListener(new ControlPanelListeners.NewListener());
        editBtn.addActionListener(new ControlPanelListeners.EditListener());
        removeBtn.addActionListener(new ControlPanelListeners.DelListener());
    }
}
