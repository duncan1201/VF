/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InternalOligoWeightsPanel.java
 *
 * Created on Jan 9, 2012, 5:55:38 PM
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
public class InternalOligoWeightsPanel extends javax.swing.JPanel {

    /**
     * Creates new form InternalOligoWeightsPanel
     */
    public InternalOligoWeightsPanel() {
        initComponents();
    }

    public void setChildrenEnabled(boolean enabled) {
        List<JSpinner> spinners = UIUtil.getChildren(this, JSpinner.class);
        for (JSpinner s : spinners) {
            s.setEnabled(enabled);
        }
    }

    private void initComponents() {

        PRIMER_INTERNAL_WT_TM_LT = new JSpinner();
        PRIMER_INTERNAL_WT_TM_GT = new JSpinner();
        PRIMER_INTERNAL_WT_SIZE_LT = new JSpinner();
        PRIMER_INTERNAL_WT_SIZE_GT = new JSpinner();
        PRIMER_INTERNAL_WT_GC_PERCENT_LT = new JSpinner();
        PRIMER_INTERNAL_WT_GC_PERCENT_GT = new JSpinner();        
        
        PRIMER_INTERNAL_WT_NUM_NS = new JSpinner();

        setBorder(BorderFactory.createTitledBorder( //
                new LineBorder(Color.LIGHT_GRAY), //
                "DNA Probes", //
                TitledBorder.LEADING, // 
                TitledBorder.TOP, //
                UIManager.getFont("TitledBorder.font")));

        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel panel1 = createPanel1();
        add(panel1, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel panel2 = createPanel2();
        add(panel2, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        add(filler, c);
    }

    protected void updateUserInputFromUI(){
        PenaltyWeightsPanel panel = UIUtil.getParent(this, PenaltyWeightsPanel.class);
        UserInput userInput = panel.getUserInput();
        
        userInput.set("PRIMER_INTERNAL_WT_TM_LT", PRIMER_INTERNAL_WT_TM_LT.getValue().toString());
        userInput.set("PRIMER_INTERNAL_WT_TM_GT", PRIMER_INTERNAL_WT_TM_GT.getValue().toString());
        userInput.set("PRIMER_INTERNAL_WT_SIZE_LT", PRIMER_INTERNAL_WT_SIZE_LT.getValue().toString());
        userInput.set("PRIMER_INTERNAL_WT_SIZE_GT", PRIMER_INTERNAL_WT_SIZE_GT.getValue().toString());
        userInput.set("PRIMER_INTERNAL_WT_GC_PERCENT_LT", PRIMER_INTERNAL_WT_GC_PERCENT_LT.getValue().toString());
        userInput.set("PRIMER_INTERNAL_WT_GC_PERCENT_GT", PRIMER_INTERNAL_WT_GC_PERCENT_GT.getValue().toString());
        userInput.set("PRIMER_INTERNAL_WT_SELF_ANY_TH", PRIMER_INTERNAL_WT_SELF_ANY_TH.getValue().toString());
        userInput.set("PRIMER_INTERNAL_WT_SELF_END_TH", PRIMER_INTERNAL_WT_SELF_END_TH.getValue().toString());
        userInput.set("PRIMER_INTERNAL_WT_HAIRPIN_TH", PRIMER_INTERNAL_WT_HAIRPIN_TH.getValue().toString());        
        userInput.set("PRIMER_INTERNAL_WT_NUM_NS", PRIMER_INTERNAL_WT_NUM_NS.getValue().toString());
    }
    
    protected void populateUI() {
        PenaltyWeightsPanel panel = UIUtil.getParent(this, PenaltyWeightsPanel.class);
        UserInput userInput = panel.getUserInput();

        String primerInteranlWtTmLt = userInput.get("PRIMER_INTERNAL_WT_TM_LT");
        String primerInteranlWtTmGt = userInput.get("PRIMER_INTERNAL_WT_TM_GT");
        String primerInteranlWtSizeLt = userInput.get("PRIMER_INTERNAL_WT_SIZE_LT");
        String primerInteranlWtSizeGt = userInput.get("PRIMER_INTERNAL_WT_SIZE_GT");
        String primerInteranlWtGcPercentLt = userInput.get("PRIMER_INTERNAL_WT_GC_PERCENT_LT");
        String primerInteranlWtGcPercentGt = userInput.get("PRIMER_INTERNAL_WT_GC_PERCENT_GT");
        String primerInteranlWtSelfAnyTH = userInput.get("PRIMER_INTERNAL_WT_SELF_ANY_TH");
        String primerInteranlWtSelfEndTH = userInput.get("PRIMER_INTERNAL_WT_SELF_END_TH");
        String primerInteranlWtHairpinTH = userInput.get("PRIMER_INTERNAL_WT_HAIRPIN_TH");
        String primerInteranlWtNumNs = userInput.get("PRIMER_INTERNAL_WT_NUM_NS");

        if(!primerInteranlWtTmLt.isEmpty()){
            PRIMER_INTERNAL_WT_TM_LT.setValue(Float.parseFloat(primerInteranlWtTmLt));
        }
        if(!primerInteranlWtTmGt.isEmpty()){
            PRIMER_INTERNAL_WT_TM_GT.setValue(Float.parseFloat(primerInteranlWtTmGt));
        }
        if(!primerInteranlWtSizeLt.isEmpty()){
            PRIMER_INTERNAL_WT_SIZE_LT.setValue(Float.parseFloat(primerInteranlWtSizeLt));
        }
        if(!primerInteranlWtSizeGt.isEmpty()){
            PRIMER_INTERNAL_WT_SIZE_GT.setValue(Float.parseFloat(primerInteranlWtSizeGt));
        }
        if(!primerInteranlWtGcPercentLt.isEmpty()){
            PRIMER_INTERNAL_WT_GC_PERCENT_LT.setValue(Float.parseFloat(primerInteranlWtGcPercentLt));
        }
        if(!primerInteranlWtGcPercentGt.isEmpty()){
            PRIMER_INTERNAL_WT_GC_PERCENT_GT.setValue(Float.parseFloat(primerInteranlWtGcPercentGt));
        }
        if(!primerInteranlWtSelfAnyTH.isEmpty()){
            PRIMER_INTERNAL_WT_SELF_ANY_TH.setValue(Float.parseFloat(primerInteranlWtSelfAnyTH));
        }
        if(!primerInteranlWtSelfEndTH.isEmpty()){
            PRIMER_INTERNAL_WT_SELF_END_TH.setValue(Float.parseFloat(primerInteranlWtSelfEndTH));
        }
        if(!primerInteranlWtHairpinTH.isEmpty()){
            PRIMER_INTERNAL_WT_HAIRPIN_TH.setValue(Float.parseFloat(primerInteranlWtHairpinTH));
        }
        if(!primerInteranlWtNumNs.isEmpty()){
            PRIMER_INTERNAL_WT_NUM_NS.setValue(Float.parseFloat(primerInteranlWtNumNs));
        }
    }

    private JPanel createPanel1() {
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
        ret.add(new JLabel("Tm"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.insets = insetsDefault;
        ret.add(new JLabel("Lt:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        PRIMER_INTERNAL_WT_TM_LT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_WT_TM_LT, 100);
        ret.add(PRIMER_INTERNAL_WT_TM_LT, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.insets = insetsDefault;
        ret.add(new JLabel("Gt:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        PRIMER_INTERNAL_WT_TM_GT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_WT_TM_GT, 100);
        ret.add(PRIMER_INTERNAL_WT_TM_GT, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Size"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        ret.add(new JLabel("Lt:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        PRIMER_INTERNAL_WT_SIZE_LT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_WT_SIZE_LT, 100);
        ret.add(PRIMER_INTERNAL_WT_SIZE_LT, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        ret.add(new JLabel("Gt:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        PRIMER_INTERNAL_WT_SIZE_GT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_WT_SIZE_GT, 100);
        ret.add(PRIMER_INTERNAL_WT_SIZE_GT, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("GC%"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        ret.add(new JLabel("Lt:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        PRIMER_INTERNAL_WT_GC_PERCENT_LT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_WT_GC_PERCENT_LT, 100);
        ret.add(PRIMER_INTERNAL_WT_GC_PERCENT_LT, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        ret.add(new JLabel("Gt:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        PRIMER_INTERNAL_WT_GC_PERCENT_GT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_WT_GC_PERCENT_GT, 100);
        ret.add(PRIMER_INTERNAL_WT_GC_PERCENT_GT, c);

        return ret;
    }

    private JPanel createPanel2() {
        JPanel ret = new JPanel(new GridBagLayout());

        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(new JLabel("Self Complementarity"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        PRIMER_INTERNAL_WT_SELF_ANY_TH = new JSpinner();
        PRIMER_INTERNAL_WT_SELF_ANY_TH.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_WT_SELF_ANY_TH, 100);
        ret.add(PRIMER_INTERNAL_WT_SELF_ANY_TH, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(new JLabel("3' Self Complementarity"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        PRIMER_INTERNAL_WT_SELF_END_TH = new JSpinner();
        PRIMER_INTERNAL_WT_SELF_END_TH.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_WT_SELF_END_TH, 100);
        ret.add(PRIMER_INTERNAL_WT_SELF_END_TH, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(new JLabel("Hairpin"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        PRIMER_INTERNAL_WT_HAIRPIN_TH = new JSpinner();
        PRIMER_INTERNAL_WT_HAIRPIN_TH.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_WT_HAIRPIN_TH, 100);
        ret.add(PRIMER_INTERNAL_WT_HAIRPIN_TH, c);        
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("# N's"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        PRIMER_INTERNAL_WT_NUM_NS.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_WT_NUM_NS, 100);
        ret.add(PRIMER_INTERNAL_WT_NUM_NS, c);

        return ret;
    }
    private JSpinner PRIMER_INTERNAL_WT_SELF_ANY_TH;
    private JSpinner PRIMER_INTERNAL_WT_GC_PERCENT_GT;
    private JSpinner PRIMER_INTERNAL_WT_GC_PERCENT_LT;
    private JSpinner PRIMER_INTERNAL_WT_NUM_NS;
    private JSpinner PRIMER_INTERNAL_WT_SELF_END_TH;
    private JSpinner PRIMER_INTERNAL_WT_HAIRPIN_TH;    
    private JSpinner PRIMER_INTERNAL_WT_SIZE_GT;
    private JSpinner PRIMER_INTERNAL_WT_SIZE_LT;
    private JSpinner PRIMER_INTERNAL_WT_TM_GT;
    private JSpinner PRIMER_INTERNAL_WT_TM_LT;
    // End of variables declaration//GEN-END:variables
}
