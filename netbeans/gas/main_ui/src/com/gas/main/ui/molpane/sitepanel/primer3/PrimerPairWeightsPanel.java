/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PrimerPairWeightsPanel.java
 *
 * Created on Jan 9, 2012, 10:26:54 PM
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.UserInput;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author dq
 */
public class PrimerPairWeightsPanel extends javax.swing.JPanel {

    /**
     * Creates new form PrimerPairWeightsPanel
     */
    public PrimerPairWeightsPanel() {
        initComponents();
    }
    
    protected void updateUserInputFromUI(){
        PenaltyWeightsPanel p = UIUtil.getParent(this, PenaltyWeightsPanel.class);
        UserInput userInput = p.getUserInput();
        
        userInput.set("PRIMER_PAIR_WT_PRODUCT_SIZE_LT", PRIMER_PAIR_WT_PRODUCT_SIZE_LT.getValue().toString());
        userInput.set("PRIMER_PAIR_WT_PRODUCT_SIZE_GT", PRIMER_PAIR_WT_PRODUCT_SIZE_GT.getValue().toString());
        userInput.set("PRIMER_PAIR_WT_PRODUCT_TM_LT", PRIMER_PAIR_WT_PRODUCT_TM_LT.getValue().toString());
        userInput.set("PRIMER_PAIR_WT_PRODUCT_TM_GT", PRIMER_PAIR_WT_PRODUCT_TM_GT.getValue().toString());
        userInput.set("PRIMER_PAIR_WT_COMPL_END_TH", PRIMER_PAIR_WT_COMPL_END_TH.getValue().toString());
        userInput.set("PRIMER_PAIR_WT_COMPL_ANY_TH", PRIMER_PAIR_WT_COMPL_ANY_TH.getValue().toString());
        userInput.set("PRIMER_PAIR_WT_TEMPLATE_MISPRIMING_TH", PRIMER_PAIR_WT_TEMPLATE_MISPRIMING_TH.getValue().toString());
    }
    
    protected void populateUI(){
        PenaltyWeightsPanel p = UIUtil.getParent(this, PenaltyWeightsPanel.class);
        UserInput userInput = p.getUserInput();
        
        String primerPairWtProductSiztLt = userInput.get("PRIMER_PAIR_WT_PRODUCT_SIZE_LT");
        String primerPairWtProductSiztGt = userInput.get("PRIMER_PAIR_WT_PRODUCT_SIZE_GT");
        String primerPairWtProductTmLt = userInput.get("PRIMER_PAIR_WT_PRODUCT_TM_LT");
        String primerPairWtProductTmGt = userInput.get("PRIMER_PAIR_WT_PRODUCT_TM_GT");
        String primerPairWtComplEndTH = userInput.get("PRIMER_PAIR_WT_COMPL_END_TH");
        String primerPairWtComplAnyTH = userInput.get("PRIMER_PAIR_WT_COMPL_ANY_TH");
        String primerPairWtTemplateMispriming = userInput.get("PRIMER_PAIR_WT_TEMPLATE_MISPRIMING_TH");
        
        PRIMER_PAIR_WT_PRODUCT_SIZE_LT.setValue(Float.parseFloat(primerPairWtProductSiztLt));
        PRIMER_PAIR_WT_PRODUCT_SIZE_GT.setValue(Float.parseFloat(primerPairWtProductSiztGt));
        
        PRIMER_PAIR_WT_PRODUCT_TM_LT.setValue(Float.parseFloat(primerPairWtProductTmLt));
        PRIMER_PAIR_WT_PRODUCT_TM_GT.setValue(Float.parseFloat(primerPairWtProductTmGt));
        
        PRIMER_PAIR_WT_COMPL_END_TH.setValue(Float.parseFloat(primerPairWtComplEndTH));
        PRIMER_PAIR_WT_COMPL_ANY_TH.setValue(Float.parseFloat(primerPairWtComplAnyTH));
        PRIMER_PAIR_WT_TEMPLATE_MISPRIMING_TH.setValue(Float.parseFloat(primerPairWtTemplateMispriming));        
    }

    private void initComponents() {
       
        PRIMER_PAIR_WT_PRODUCT_SIZE_LT = new JSpinner();
        PRIMER_PAIR_WT_PRODUCT_SIZE_GT = new JSpinner();
        
        PRIMER_PAIR_WT_PRODUCT_TM_LT = new JSpinner();        
        PRIMER_PAIR_WT_PRODUCT_TM_GT = new JSpinner();        
        
        PRIMER_PAIR_WT_COMPL_END_TH = new JSpinner();
        PRIMER_PAIR_WT_TEMPLATE_MISPRIMING_TH = new JSpinner();
        PRIMER_PAIR_WT_COMPL_ANY_TH = new JSpinner();
        
        setBorder(BorderFactory.createTitledBorder( //
                new LineBorder(Color.LIGHT_GRAY), //
                "Primer Pairs", //
                TitledBorder.LEADING, //
                TitledBorder.TOP, 
                UIManager.getFont("TitledBorder.font")));
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel row1 = createPanel1();
        add(row1, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;        
        JPanel row2 = createPanel2();
        add(row2, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        add(filler, c);
    }

    private JPanel createPanel1() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(new JLabel("Tm"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.insets = UIUtil.getDefaultInsets();
        ret.add(new JLabel("Lt:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        PRIMER_PAIR_WT_PRODUCT_TM_LT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PAIR_WT_PRODUCT_TM_LT, 100);
        ret.add(PRIMER_PAIR_WT_PRODUCT_TM_LT, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.insets = UIUtil.getDefaultInsets();
        ret.add(new JLabel("Gt:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        PRIMER_PAIR_WT_PRODUCT_TM_GT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PAIR_WT_PRODUCT_TM_GT, 100);
        ret.add(PRIMER_PAIR_WT_PRODUCT_TM_GT, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;        
        ret.add(new JLabel("Size"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        ret.add(new JLabel("Lt:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        PRIMER_PAIR_WT_PRODUCT_SIZE_LT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PAIR_WT_PRODUCT_SIZE_LT, 100);
        ret.add(PRIMER_PAIR_WT_PRODUCT_SIZE_LT, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        ret.add(new JLabel("Gt:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        PRIMER_PAIR_WT_PRODUCT_SIZE_GT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PAIR_WT_PRODUCT_SIZE_GT, 100);
        ret.add(PRIMER_PAIR_WT_PRODUCT_SIZE_GT, c);

        return ret;
    }

    private JPanel createPanel2() {
        final Insets insetsDefault = UIUtil.getDefaultInsets();
        
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        int gridy = 0;
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = new Insets(0,0,0,insetsDefault.right);
        ret.add(new JLabel("Self Complementarity"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        PRIMER_PAIR_WT_COMPL_ANY_TH.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PAIR_WT_COMPL_ANY_TH, 100);
        ret.add(PRIMER_PAIR_WT_COMPL_ANY_TH, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;          
        c.insets = new Insets(0,0,0,insetsDefault.right);
        ret.add(new JLabel("3' Self Complementarity"), c);
        
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        PRIMER_PAIR_WT_COMPL_END_TH.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PAIR_WT_COMPL_END_TH, 100);
        ret.add(PRIMER_PAIR_WT_COMPL_END_TH, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;           
        c.insets = new Insets(0,0,0,insetsDefault.right);
        ret.add(new JLabel("Pair Template Mispriming"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        PRIMER_PAIR_WT_TEMPLATE_MISPRIMING_TH.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PAIR_WT_TEMPLATE_MISPRIMING_TH, 100);
        ret.add(PRIMER_PAIR_WT_TEMPLATE_MISPRIMING_TH, c);

        return ret;
    }
    private JSpinner PRIMER_PAIR_WT_COMPL_ANY_TH;
    private JSpinner PRIMER_PAIR_WT_COMPL_END_TH;
    private JSpinner PRIMER_PAIR_WT_PRODUCT_SIZE_GT;
    private JSpinner PRIMER_PAIR_WT_PRODUCT_SIZE_LT;
    private JSpinner PRIMER_PAIR_WT_PRODUCT_TM_GT;
    private JSpinner PRIMER_PAIR_WT_PRODUCT_TM_LT;
    private JSpinner PRIMER_PAIR_WT_TEMPLATE_MISPRIMING_TH;
}
