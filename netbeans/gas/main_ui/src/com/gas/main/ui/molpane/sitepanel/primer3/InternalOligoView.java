/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * InternalOligoView.java
 *
 * Created on Jan 9, 2012, 5:06:13 PM
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.main.ui.molpane.sitepanel.primer3.task.TaskPanel;
import com.gas.common.ui.accordian2.IOutlookPanel;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.CollapsibleTitlePanel;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.RichSeparator;
import com.gas.common.ui.misc.SpinnerValidators;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.UserInput;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.jdesktop.swingx.VerticalLayout;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

/**
 *
 * @author dq
 */
class InternalOligoView extends JPanel implements IOutlookPanel {

    /**
     * Creates new form InternalOligoView
     */
    InternalOligoView() {
        initComponents();
        hookupListeners();
    }

    private void hookupListeners() {
        new SpinnerValidators.Linker(PRIMER_INTERNAL_MIN_SIZE, PRIMER_INTERNAL_OPT_SIZE);
        new SpinnerValidators.Linker(PRIMER_INTERNAL_OPT_SIZE, PRIMER_INTERNAL_MAX_SIZE);

        new SpinnerValidators.Linker(PRIMER_INTERNAL_MIN_TM, PRIMER_INTERNAL_OPT_TM);
        new SpinnerValidators.Linker(PRIMER_INTERNAL_OPT_TM, PRIMER_INTERNAL_MAX_TM);

        new SpinnerValidators.Linker(PRIMER_INTERNAL_MIN_GC, PRIMER_INTERNAL_OPT_GC_PERCENT);
        new SpinnerValidators.Linker(PRIMER_INTERNAL_OPT_GC_PERCENT, PRIMER_INTERNAL_MAX_GC);

    }

    List<String> validateInput() {
        List<String> ret = new ArrayList<String>();
        return ret;
    }

    void updateUserInputFromUI(UserInput userInput) {
        if (userInput == null) {
            return;
        }
        userInput.set("PRIMER_INTERNAL_MIN_TM", PRIMER_INTERNAL_MIN_TM.getValue().toString());
        userInput.set("PRIMER_INTERNAL_OPT_TM", PRIMER_INTERNAL_OPT_TM.getValue().toString());
        userInput.set("PRIMER_INTERNAL_MAX_TM", PRIMER_INTERNAL_MAX_TM.getValue().toString());
        userInput.set("PRIMER_INTERNAL_MIN_SIZE", PRIMER_INTERNAL_MIN_SIZE.getValue().toString());
        userInput.set("PRIMER_INTERNAL_OPT_SIZE", PRIMER_INTERNAL_OPT_SIZE.getValue().toString());
        userInput.set("PRIMER_INTERNAL_MAX_SIZE", PRIMER_INTERNAL_MAX_SIZE.getValue().toString());
        userInput.set("PRIMER_INTERNAL_MIN_GC", PRIMER_INTERNAL_MIN_GC.getValue().toString());
        userInput.set("PRIMER_INTERNAL_OPT_GC_PERCENT", PRIMER_INTERNAL_OPT_GC_PERCENT.getValue().toString());
        userInput.set("PRIMER_INTERNAL_MAX_GC", PRIMER_INTERNAL_MAX_GC.getValue().toString());
        userInput.set("PRIMER_INTERNAL_MAX_SELF_ANY_TH", PRIMER_INTERNAL_MAX_SELF_ANY_TH.getValue().toString());
        userInput.set("PRIMER_INTERNAL_MAX_SELF_END_TH", PRIMER_INTERNAL_MAX_SELF_END_TH.getValue().toString());
        userInput.set("PRIMER_INTERNAL_MAX_POLY_X", PRIMER_INTERNAL_MAX_POLY_X.getValue().toString());

    }

    void populateUI(UserInput userInput) {
        if (userInput == null) {
            return;
        }
        String primerInternalMinTm = userInput.get("PRIMER_INTERNAL_MIN_TM");
        String primerInternalOptTm = userInput.get("PRIMER_INTERNAL_OPT_TM");
        String primerInternalMaxTm = userInput.get("PRIMER_INTERNAL_MAX_TM");
        String primerInternalMinSize = userInput.get("PRIMER_INTERNAL_MIN_SIZE");
        String primerInternalOptSize = userInput.get("PRIMER_INTERNAL_OPT_SIZE");
        String primerInternalMaxSize = userInput.get("PRIMER_INTERNAL_MAX_SIZE");
        String primerInternalMinGC = userInput.get("PRIMER_INTERNAL_MIN_GC");
        String primerInternalOptGcPercent = userInput.get("PRIMER_INTERNAL_OPT_GC_PERCENT");
        String primerInternalMaxGC = userInput.get("PRIMER_INTERNAL_MAX_GC");

        String primerInternalMaxSelfAnyTH = userInput.get("PRIMER_INTERNAL_MAX_SELF_ANY_TH");
        String primerInternalMaxSelfEndTH = userInput.get("PRIMER_INTERNAL_MAX_SELF_END_TH");
        String primerInternalMaxPolyX = userInput.get("PRIMER_INTERNAL_MAX_POLY_X");


        PRIMER_INTERNAL_MIN_TM.setValue(Float.parseFloat(primerInternalMinTm));
        PRIMER_INTERNAL_OPT_TM.setValue(Float.parseFloat(primerInternalOptTm));
        PRIMER_INTERNAL_MAX_TM.setValue(Float.parseFloat(primerInternalMaxTm));
        PRIMER_INTERNAL_MIN_SIZE.setValue(Integer.parseInt(primerInternalMinSize));
        PRIMER_INTERNAL_OPT_SIZE.setValue(Integer.parseInt(primerInternalOptSize));
        PRIMER_INTERNAL_MAX_SIZE.setValue(Integer.parseInt(primerInternalMaxSize));
        PRIMER_INTERNAL_MIN_GC.setValue(Float.parseFloat(primerInternalMinGC));
        if (!primerInternalOptGcPercent.isEmpty()) {
            PRIMER_INTERNAL_OPT_GC_PERCENT.setValue(Float.parseFloat(primerInternalOptGcPercent));
        }
        PRIMER_INTERNAL_MAX_GC.setValue(Float.parseFloat(primerInternalMaxGC));
        PRIMER_INTERNAL_MAX_SELF_ANY_TH.setValue(Float.parseFloat(primerInternalMaxSelfAnyTH));
        PRIMER_INTERNAL_MAX_SELF_END_TH.setValue(Float.parseFloat(primerInternalMaxSelfEndTH));
        PRIMER_INTERNAL_MAX_POLY_X.setValue(Integer.parseInt(primerInternalMaxPolyX));

        UIUtil.enabledRecursively(this, userInput.pickInternalOligo());
    }

    private JPanel createOtherConditionsPanel() {
        JPanel otherConditionsPanel = new JPanel();
        //otherConditionsPanel.setBorder(new TitledBorder(new LineBorder(Color.LIGHT_GRAY), "Other Conditions", TitledBorder.LEADING, TitledBorder.TOP));                                         

        PRIMER_INTERNAL_MAX_SELF_ANY_TH = new JSpinner();
        PRIMER_INTERNAL_MAX_SELF_ANY_TH.setModel(new SpinnerNumberModel(0f, 0f, null, 0.1f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_MAX_SELF_ANY_TH, 100);

        PRIMER_INTERNAL_MAX_POLY_X = new JSpinner();
        PRIMER_INTERNAL_MAX_POLY_X.setModel(new SpinnerNumberModel(60, 0, null, 1));

        PRIMER_INTERNAL_MAX_SELF_END_TH = new JSpinner();
        PRIMER_INTERNAL_MAX_SELF_END_TH.setModel(new SpinnerNumberModel(0f, 0f, null, 0.1f));

        tmCalculationBtn = new JButton();
        tmCalculationBtn.setText("Tm Calculation");
        tmCalculationBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tmCalculationBtnActionPerformed(evt);
            }
        });

        pickingWeightsBtn = new JButton();
        pickingWeightsBtn.setText("Picking Weights");
        pickingWeightsBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pickingWeightsBtnActionPerformed(evt);
            }
        });

        otherConditionsPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = null;

        // max self complementary
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        otherConditionsPanel.add(new JLabel("Max Self Complementarity:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        otherConditionsPanel.add(PRIMER_INTERNAL_MAX_SELF_ANY_TH, c);

        // max 3' self complementary
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        otherConditionsPanel.add(new JLabel("Max 3' Self Complementarity:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        otherConditionsPanel.add(PRIMER_INTERNAL_MAX_SELF_END_TH, c);

        // max poly-x
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.WEST;
        otherConditionsPanel.add(new JLabel("Max Poly-X:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        otherConditionsPanel.add(PRIMER_INTERNAL_MAX_POLY_X, c);

        // button panel        
        JPanel btnPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        otherConditionsPanel.add(btnPanel, c);

        c = new GridBagConstraints();
        btnPanel.add(tmCalculationBtn, c);

        c = new GridBagConstraints();
        btnPanel.add(pickingWeightsBtn, c);

        return otherConditionsPanel;
    }

    private JPanel createHybOligoGCPanel() {
        JPanel ret = new JPanel(new BorderLayout());
        
        JPanel hybOligoGCPanel = new JPanel();

        PRIMER_INTERNAL_MIN_GC = new JSpinner();
        PRIMER_INTERNAL_MIN_GC.setModel(new SpinnerNumberModel(Float.valueOf(60), 0f, null, 0.1f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_MIN_GC, 1000);

        PRIMER_INTERNAL_OPT_GC_PERCENT = new JSpinner();
        PRIMER_INTERNAL_OPT_GC_PERCENT.setModel(new SpinnerNumberModel(Float.valueOf(60), 0f, null, 0.1f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_OPT_GC_PERCENT, 1000);

        PRIMER_INTERNAL_MAX_GC = new JSpinner();
        PRIMER_INTERNAL_MAX_GC.setModel(new SpinnerNumberModel(Float.valueOf(60), 0f, null, 0.1f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_MAX_GC, 1000);

        hybOligoGCPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        hybOligoGCPanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MIN_16)), c);

        c = new GridBagConstraints();
        hybOligoGCPanel.add(PRIMER_INTERNAL_MIN_GC, c);

        c = new GridBagConstraints();
        hybOligoGCPanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        hybOligoGCPanel.add(PRIMER_INTERNAL_OPT_GC_PERCENT, c);

        c = new GridBagConstraints();
        hybOligoGCPanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        hybOligoGCPanel.add(PRIMER_INTERNAL_MAX_GC, c);
        
        c = new GridBagConstraints();
        hybOligoGCPanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MAX_16)), c);        

        RichSeparator separator = new RichSeparator("DNA Probe GC%");
        ret.add(separator, BorderLayout.NORTH);
        ret.add(hybOligoGCPanel, BorderLayout.CENTER);
        
        return ret;
    }

    private JPanel createHybOligoTmPanel() {
        JPanel ret = new JPanel(new BorderLayout());
        
        JPanel hybOligoTmPanel = new JPanel();

        PRIMER_INTERNAL_MIN_TM = new JSpinner();
        PRIMER_INTERNAL_MIN_TM.setModel(new SpinnerNumberModel(/*cannot use primitive*/Float.valueOf(0), 0f, null, 0.1f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_MIN_TM, 1000);

        PRIMER_INTERNAL_OPT_TM = new JSpinner();
        PRIMER_INTERNAL_OPT_TM.setModel(new SpinnerNumberModel(/*cannot use primitive*/Float.valueOf(0), 0f, null, 0.1f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_OPT_TM, 1000);

        PRIMER_INTERNAL_MAX_TM = new JSpinner();
        PRIMER_INTERNAL_MAX_TM.setModel(new SpinnerNumberModel(/*cannot use primitive*/Float.valueOf(0), 0f, null, 0.1f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_MAX_TM, 1000);

        hybOligoTmPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        hybOligoTmPanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MIN_16)), c);

        c = new GridBagConstraints();
        hybOligoTmPanel.add(PRIMER_INTERNAL_MIN_TM, c);

        c = new GridBagConstraints();
        hybOligoTmPanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        hybOligoTmPanel.add(PRIMER_INTERNAL_OPT_TM, c);

        c = new GridBagConstraints();
        hybOligoTmPanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        hybOligoTmPanel.add(PRIMER_INTERNAL_MAX_TM, c);
        
        c = new GridBagConstraints();
        hybOligoTmPanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MAX_16)), c);        

        RichSeparator separator = new RichSeparator("DNA Probe Tm");
        ret.add(separator, BorderLayout.NORTH);
        ret.add(hybOligoTmPanel, BorderLayout.CENTER);
        
        return ret;
    }

    private JPanel createDNAProbeSizePanel() {
        JPanel ret = new JPanel(new BorderLayout());
        
        JPanel hybOligoSizePanel = new JPanel();

        hybOligoSizePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        hybOligoSizePanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MIN_16)), c);

        c = new GridBagConstraints();
        PRIMER_INTERNAL_MIN_SIZE = new JSpinner();
        PRIMER_INTERNAL_MIN_SIZE.setModel(new SpinnerNumberModel(60, 0, null, 1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_MIN_SIZE, 1000);        
        hybOligoSizePanel.add(PRIMER_INTERNAL_MIN_SIZE, c);

        c = new GridBagConstraints();
        hybOligoSizePanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        PRIMER_INTERNAL_OPT_SIZE = new JSpinner();
        PRIMER_INTERNAL_OPT_SIZE.setModel(new SpinnerNumberModel(60, 0, null, 1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_OPT_SIZE, 1000);        
        hybOligoSizePanel.add(PRIMER_INTERNAL_OPT_SIZE, c);

        c = new GridBagConstraints();
        hybOligoSizePanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        PRIMER_INTERNAL_MAX_SIZE = new JSpinner();
        PRIMER_INTERNAL_MAX_SIZE.setModel(new SpinnerNumberModel(60, 0, null, 1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_INTERNAL_MAX_SIZE, 1000);        
        hybOligoSizePanel.add(PRIMER_INTERNAL_MAX_SIZE, c);
        
        c = new GridBagConstraints();
        hybOligoSizePanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MAX_16)), c);        

        RichSeparator separator = new RichSeparator("DNA Probe Size");
        ret.add(separator, BorderLayout.NORTH);
        ret.add(hybOligoSizePanel, BorderLayout.CENTER);
        
        return ret;
    }

    private void initComponents() {

        final Insets insets = UIUtil.getDefaultInsets();
        
        JPanel hybOligoTmPanel = createHybOligoTmPanel();
        hybOligoTmPanel.setBorder(BorderFactory.createEmptyBorder(0, insets.left, 0, 0));
        
        JPanel hybOligoSizePanel = createDNAProbeSizePanel();
        hybOligoSizePanel.setBorder(BorderFactory.createEmptyBorder(0, insets.left, 0, 0));

        JPanel hybOligoGCPanel = createHybOligoGCPanel();
        hybOligoGCPanel.setBorder(BorderFactory.createEmptyBorder(0, insets.left, 0, 0));

        JPanel otherConditionsPanel = createOtherConditionsPanel();

        this.setLayout(new VerticalLayout());
        this.add(hybOligoTmPanel);
        this.add(hybOligoSizePanel);
        this.add(hybOligoGCPanel);

        CollapsibleTitlePanel other = new CollapsibleTitlePanel("Other Conditions");
        other.getContentPane().add(otherConditionsPanel, BorderLayout.CENTER);
        UIUtil.setBackground(otherConditionsPanel, CNST.BG, JPanel.class);
        other.setCollapsed(true);
        this.add(other);

    }// </editor-fold>

    private void tmCalculationBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Primer3Panel primer3Panel = UIUtil.getParent(this, Primer3Panel.class);
        UserInput userInput = primer3Panel.getP3output().getUserInput();
        if (userInput == null) {
            return;
        }
        TmCalSettingsPanel tmCalculationSettingsPanel = new InternalOligoTmCalSettingsPanel();
        tmCalculationSettingsPanel.populateUI(userInput);
        DialogDescriptor dd = new DialogDescriptor(tmCalculationSettingsPanel, "Tm Calculation Settings");
        Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);
        if (JOptionPane.OK_OPTION == answer) {
            tmCalculationSettingsPanel.updateUserInputFromUI(userInput);
        }
    }

    private void pickingWeightsBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Primer3Panel primer3Panel = UIUtil.getParent(this, Primer3Panel.class);

        PenaltyWeightsPanel ip = new PenaltyWeightsPanel(false, false, true);
        ip.setUserInput(primer3Panel.getP3output().getUserInput());
        DialogDescriptor dd = new DialogDescriptor(ip, "Penalty Weights");

        Object answer = DialogDisplayer.getDefault().notify(dd);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            ip.updateUserInputFromUI();
        }
    }
    private JSpinner PRIMER_INTERNAL_MAX_POLY_X;
    private JSpinner PRIMER_INTERNAL_MIN_SIZE;
    private JSpinner PRIMER_INTERNAL_OPT_SIZE;
    private JSpinner PRIMER_INTERNAL_MAX_SIZE;
    private JSpinner PRIMER_INTERNAL_MIN_TM;
    private JSpinner PRIMER_INTERNAL_OPT_TM;
    private JSpinner PRIMER_INTERNAL_MAX_TM;
    private JSpinner PRIMER_INTERNAL_MIN_GC;
    private JSpinner PRIMER_INTERNAL_OPT_GC_PERCENT;
    private JSpinner PRIMER_INTERNAL_MAX_GC;
    private JSpinner PRIMER_INTERNAL_MAX_SELF_ANY_TH;
    private JSpinner PRIMER_INTERNAL_MAX_SELF_END_TH;
    private JButton pickingWeightsBtn;
    private JButton tmCalculationBtn;

    @Override
    public void expanded() {
        Primer3View primer3view = UIUtil.getParent(this, Primer3View.class);
        if (primer3view == null) {
            return;
        }
        TaskPanel taskPanel = primer3view.getTaskPanel();
        UIUtil.enabledRecursively(this, taskPanel.internalPickPanel.isPicking());
    }
}
