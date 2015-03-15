/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PrimerPairWeightsPanel.java
 *
 * Created on Jan 9, 2012, 9:48:39 PM
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.UserInput;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author dq
 */
public class PrimerWeightsPanel extends javax.swing.JPanel {

    /**
     * Creates new form PrimerPairWeightsPanel
     */
    public PrimerWeightsPanel() {
        initComponents();
    }

    protected void updateUserInputFromUI() {
        PenaltyWeightsPanel p = UIUtil.getParent(this, PenaltyWeightsPanel.class);
        UserInput ui = p.getUserInput();

        ui.set("PRIMER_WT_TM_LT", PRIMER_WT_TM_LT.getValue().toString());
        ui.set("PRIMER_WT_TM_GT", PRIMER_WT_TM_GT.getValue().toString());
        ui.set("PRIMER_WT_SIZE_LT", PRIMER_WT_SIZE_LT.getValue().toString());
        ui.set("PRIMER_WT_SIZE_GT", PRIMER_WT_SIZE_GT.getValue().toString());
        ui.set("PRIMER_WT_GC_PERCENT_LT", PRIMER_WT_GC_PERCENT_LT.getValue().toString());
        ui.set("PRIMER_WT_GC_PERCENT_GT", PRIMER_WT_GC_PERCENT_GT.getValue().toString());
        ui.set("PRIMER_WT_SELF_ANY_TH", PRIMER_WT_SELF_ANY_TH.getValue().toString());
        ui.set("PRIMER_WT_SELF_END_TH", PRIMER_WT_SELF_END_TH.getValue().toString());
        ui.set("PRIMER_WT_TEMPLATE_MISPRIMING_TH", PRIMER_WT_TEMPLATE_MISPRIMING_TH.getValue().toString());
        ui.set("PRIMER_WT_HAIRPIN_TH", PRIMER_WT_HAIRPIN_TH.getValue().toString());
        ui.set("PRIMER_WT_NUM_NS", PRIMER_WT_NUM_NS.getValue().toString());
    }

    protected void populateUI() {
        PenaltyWeightsPanel p = UIUtil.getParent(this, PenaltyWeightsPanel.class);
        UserInput ui = p.getUserInput();

        String primerWtTmLt = ui.get("PRIMER_WT_TM_LT");
        String primerWtTmGt = ui.get("PRIMER_WT_TM_GT");
        String primerWtSiztLt = ui.get("PRIMER_WT_SIZE_LT");
        String primerWtSiztGt = ui.get("PRIMER_WT_SIZE_GT");
        String primerWtGcPercentLt = ui.get("PRIMER_WT_GC_PERCENT_LT");
        String primerWtGcPercentGt = ui.get("PRIMER_WT_GC_PERCENT_GT");
        String primerWtSelfAnyTH = ui.get("PRIMER_WT_SELF_ANY_TH");
        String primerWtSelfEndTH = ui.get("PRIMER_WT_SELF_END_TH");
        String primerWtTemplateMisprimingTH = ui.get("PRIMER_WT_TEMPLATE_MISPRIMING_TH");
        String primerWtHairpinTH = ui.get("PRIMER_WT_HAIRPIN_TH");
        String primerWtNumNs = ui.get("PRIMER_WT_NUM_NS");

        PRIMER_WT_TM_LT.setValue(Float.parseFloat(primerWtTmLt));
        PRIMER_WT_TM_GT.setValue(Float.parseFloat(primerWtTmGt));
        PRIMER_WT_SIZE_LT.setValue(Float.parseFloat(primerWtSiztLt));
        PRIMER_WT_SIZE_GT.setValue(Float.parseFloat(primerWtSiztGt));
        PRIMER_WT_GC_PERCENT_LT.setValue(Float.parseFloat(primerWtGcPercentLt));
        PRIMER_WT_GC_PERCENT_GT.setValue(Float.parseFloat(primerWtGcPercentGt));


        PRIMER_WT_SELF_ANY_TH.setValue(Float.parseFloat(primerWtSelfAnyTH));
        PRIMER_WT_SELF_END_TH.setValue(Float.parseFloat(primerWtSelfEndTH));
        PRIMER_WT_TEMPLATE_MISPRIMING_TH.setValue(Float.parseFloat(primerWtTemplateMisprimingTH));
        PRIMER_WT_HAIRPIN_TH.setValue(Float.parseFloat(primerWtHairpinTH));
        PRIMER_WT_NUM_NS.setValue(Float.parseFloat(primerWtNumNs));
    }

    public void setChildrenEnabled(boolean enabled) {
        List<JSpinner> spinners = UIUtil.getChildren(this, JSpinner.class);
        for (JSpinner s : spinners) {
            s.setEnabled(enabled);
        }
    }

    private void initComponents() {

        PRIMER_WT_GC_PERCENT_LT = new JSpinner();
        PRIMER_WT_GC_PERCENT_GT = new JSpinner();
        PRIMER_WT_SELF_ANY_TH = new JSpinner();
        PRIMER_WT_SELF_END_TH = new JSpinner();
        PRIMER_WT_SIZE_LT = new JSpinner();
        PRIMER_WT_SIZE_GT = new JSpinner();
        PRIMER_WT_TM_LT = new JSpinner();
        PRIMER_WT_TM_GT = new JSpinner();
        PRIMER_WT_NUM_NS = new JSpinner();

        setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.LIGHT_GRAY), "Individual Primer", TitledBorder.LEADING, TitledBorder.TOP));

        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel row1 = createRow1();
        add(row1, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel panel2 = createPanel2();
        add(panel2, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.VERTICAL;
        c.weighty = 1;
        Component filler = Box.createRigidArea(new Dimension(1, 1));
        add(filler, c);

    }

    private JPanel createRow1() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.2;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Tm"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.insets = UIUtil.getDefaultInsets();
        ret.add(new JLabel("Lt:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        PRIMER_WT_TM_LT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_WT_TM_LT, 100);
        ret.add(PRIMER_WT_TM_LT, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.insets = UIUtil.getDefaultInsets();
        ret.add(new JLabel("Gt:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        PRIMER_WT_TM_GT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_WT_TM_GT, 100);
        ret.add(PRIMER_WT_TM_GT, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Size"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        ret.add(new JLabel("Lt:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        PRIMER_WT_SIZE_LT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_WT_SIZE_LT, 100);
        ret.add(PRIMER_WT_SIZE_LT, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        ret.add(new JLabel("Gt:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        PRIMER_WT_SIZE_GT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_WT_SIZE_GT, 100);
        ret.add(PRIMER_WT_SIZE_GT, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("GC%"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        ret.add(new JLabel("Lt:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        PRIMER_WT_GC_PERCENT_LT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_WT_GC_PERCENT_LT, 100);
        ret.add(PRIMER_WT_GC_PERCENT_LT, c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        ret.add(new JLabel("Gt:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        PRIMER_WT_GC_PERCENT_GT.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_WT_GC_PERCENT_GT, 100);
        ret.add(PRIMER_WT_GC_PERCENT_GT, c);

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
        PRIMER_WT_SELF_ANY_TH.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_WT_SELF_ANY_TH, 100);
        ret.add(PRIMER_WT_SELF_ANY_TH, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(new JLabel("3' Self Complementarity"), c);

        // template Mispriming
        c = new GridBagConstraints();
        c.gridy = gridy++;
        PRIMER_WT_SELF_END_TH.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_WT_SELF_END_TH, 100);
        ret.add(PRIMER_WT_SELF_END_TH, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(new JLabel("Template Mispriming"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.EAST;
        PRIMER_WT_TEMPLATE_MISPRIMING_TH = new JSpinner();
        PRIMER_WT_TEMPLATE_MISPRIMING_TH.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_WT_TEMPLATE_MISPRIMING_TH, 100);
        ret.add(PRIMER_WT_TEMPLATE_MISPRIMING_TH, c);

        // hairpin
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(new JLabel("Hairpin"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        PRIMER_WT_HAIRPIN_TH = new JSpinner();
        PRIMER_WT_HAIRPIN_TH.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_WT_HAIRPIN_TH, 100);
        ret.add(PRIMER_WT_HAIRPIN_TH, c);

        //# N's
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        ret.add(new JLabel("# N's"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        PRIMER_WT_NUM_NS.setModel(new SpinnerNumberModel(0.0, 0.0, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_WT_NUM_NS, 100);
        ret.add(PRIMER_WT_NUM_NS, c);

        return ret;
    }
    private JSpinner PRIMER_WT_SELF_ANY_TH;
    private JSpinner PRIMER_WT_SELF_END_TH;
    private JSpinner PRIMER_WT_GC_PERCENT_GT;
    private JSpinner PRIMER_WT_GC_PERCENT_LT;
    private JSpinner PRIMER_WT_NUM_NS;
    private JSpinner PRIMER_WT_TEMPLATE_MISPRIMING_TH;
    private JSpinner PRIMER_WT_HAIRPIN_TH;
    private JSpinner PRIMER_WT_SIZE_GT;
    private JSpinner PRIMER_WT_SIZE_LT;
    private JSpinner PRIMER_WT_TM_GT;
    private JSpinner PRIMER_WT_TM_LT;
}
