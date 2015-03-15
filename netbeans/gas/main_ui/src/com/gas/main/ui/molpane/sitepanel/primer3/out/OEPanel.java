/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.out;

import com.gas.common.ui.util.FontUtil;
import com.gas.domain.core.primer3.Oligo;
import com.gas.domain.core.primer3.OligoElement;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
class OEPanel extends JPanel {
    
    Oligo oligo;
    OligoElement oligoElement;    
    WeakReference<JLabel> nameLabelRef;
    WeakReference<JLabel> seqLabelRef;
    WeakReference<JLabel> tmLabelRef;
    WeakReference<JLabel> gcLabelRef;
    WeakReference<JLabel> selfAnyLabelRef;
    WeakReference<JLabel> selfEndLabelRef;
    WeakReference<JLabel> hairpinLabelRef;
    WeakReference<JLabel> complAnyLabelRef;
    WeakReference<JLabel> complEndLabelRef;
    
    OEPanel(){
        setOpaque(false);
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        final int strWidth = FontUtil.getDefaultFontMetrics().stringWidth("AB");
        final Insets INDENT = new Insets(0,strWidth,0,0);
        int gridx = 0;
        
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;        
        JLabel nameLabel = new JLabel();
        nameLabelRef = new WeakReference<JLabel>(nameLabel);
        add(nameLabel, c);          
        
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = INDENT;
        JLabel seqLabel = new JLabel();
        seqLabelRef = new WeakReference<JLabel>(seqLabel);
        add(seqLabel, c);        
        
        c = new GridBagConstraints();        
        c.gridx = gridx;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = INDENT;
        JLabel tmLabel = new JLabel();
        tmLabelRef = new WeakReference<JLabel>(tmLabel);
        add(tmLabel, c);
        
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = INDENT;
        JLabel gcLabel = new JLabel();
        gcLabelRef = new WeakReference<JLabel>(gcLabel);
        add(gcLabel, c);
        
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = INDENT;
        JLabel selfAnyLabel = new JLabel();
        selfAnyLabelRef = new WeakReference<JLabel>(selfAnyLabel);
        add(selfAnyLabel, c);   
        
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = INDENT;
        JLabel selfEndLabel = new JLabel();
        selfEndLabelRef = new WeakReference<JLabel>(selfEndLabel);
        add(selfEndLabel, c);
        
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = INDENT;
        JLabel complAnyLabel = new JLabel();
        complAnyLabelRef = new WeakReference<JLabel>(complAnyLabel);
        add(complAnyLabel, c);
        
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = INDENT;
        JLabel complEndLabel = new JLabel();
        complEndLabelRef = new WeakReference<JLabel>(complEndLabel);
        add(complEndLabel, c);        
        
        c = new GridBagConstraints();
        c.gridx = gridx;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = INDENT;
        JLabel hairpinLabel = new JLabel();
        hairpinLabelRef = new WeakReference<JLabel>(hairpinLabel);
        add(hairpinLabel, c);  
        
        hookupListeners();
    }
    
    private void hookupListeners(){
        addPropertyChangeListener(new OEPanelListeners.PtyListener());
    }
    
    void setOligo(Oligo oligo){
        Oligo old = this.oligo;
        this.oligo = oligo;
        firePropertyChange("oligo", old, this.oligo);
    }

    void setOligoElement(OligoElement oligoElement) {
        OligoElement oe = this.oligoElement;
        this.oligoElement = oligoElement;
        firePropertyChange("oligoElement", oe, this.oligoElement);
    }
        
}
