/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.general;

import com.gas.common.ui.util.UIUtil;
import com.gas.common.ui.util.Pref;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author dq
 */
public class FontPanel extends JPanel {

    JSpinner rulerSizeSpinner;
    JSpinner baseSizeSpinner;
    JSpinner annotationSizeSpinner;

    public FontPanel() {
        initComponents();
        hookupListeners();
    }
    
    private void initComponents(){
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;
        int gridy = 0;

        // ruler
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;        
        add(new JLabel("Ruler:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;                
        rulerSizeSpinner = new JSpinner();
        SpinnerNumberModel numberModel = (SpinnerNumberModel)rulerSizeSpinner.getModel();       
        numberModel.setMinimum(8);
        numberModel.setValue(Pref.CommonPtyPrefs.getInstance().getRulerFontSize().intValue());
        numberModel.setMaximum(20);
        rulerSizeSpinner.addChangeListener(new FontPanelListeners.RulerSizeListener());
        UIUtil.setPreferredWidthByPrototype(rulerSizeSpinner, new Integer(100));
        add(rulerSizeSpinner, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(Box.createRigidArea(new Dimension(1,1)), c);

        // base
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;       
        add(new JLabel("Base:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;        
        baseSizeSpinner = new JSpinner();
        numberModel = (SpinnerNumberModel)baseSizeSpinner.getModel();
        numberModel.setMinimum(8);
        numberModel.setValue(Pref.CommonPtyPrefs.getInstance().getBaseFontSize().intValue());
        numberModel.setMaximum(20);
        baseSizeSpinner.addChangeListener(new FontPanelListeners.BaseSizeListener());
        UIUtil.setPreferredWidthByPrototype(baseSizeSpinner, new Integer(100));
        add(baseSizeSpinner, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(Box.createRigidArea(new Dimension(1,1)), c);        

        // annotation
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;        
        add(new JLabel("Annotation:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;           
        annotationSizeSpinner = new JSpinner();
        numberModel = (SpinnerNumberModel)annotationSizeSpinner.getModel();        
        numberModel.setMinimum(8);
        numberModel.setValue(Pref.CommonPtyPrefs.getInstance().getAnnotationLabelSize().intValue());
        numberModel.setMaximum(20);
        annotationSizeSpinner.addChangeListener(new FontPanelListeners.AnnotationLabelSizeListener());
        UIUtil.setPreferredWidthByPrototype(annotationSizeSpinner, new Integer(100));
        add(annotationSizeSpinner, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        add(Box.createRigidArea(new Dimension(1,1)), c);    
    }
    
    public void setAnnotationLabelSize(int size){
        this.annotationSizeSpinner.setValue(size);
    }
    
    public void setBaseFontSize(int size){
        this.baseSizeSpinner.setValue(size);
    }
    
    public void setRulerFontSize(int size){
        this.rulerSizeSpinner.setValue(size);
    }
    
    private void hookupListeners(){
    }
}
