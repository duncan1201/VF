/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * PrimerPickingView.java
 *
 * Created on Jan 7, 2012, 6:45:12 PM
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
import com.gas.common.ui.util.Unicodes;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.domain.core.primer3.UserInput;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import org.jdesktop.swingx.VerticalLayout;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

/**
 *
 * @author dq
 */
class PrimerPickingView extends JPanel implements IOutlookPanel {

    /**
     * Creates new form PrimerPickingView
     */
    PrimerPickingView() {
        initComponents();
        hookupListeners();
    }

    private void hookupListeners() {
        new SpinnerValidators.Linker(PRIMER_MIN_TM, PRIMER_OPT_TM);
        new SpinnerValidators.Linker(PRIMER_OPT_TM, PRIMER_MAX_TM);

        new SpinnerValidators.Linker(PRIMER_MIN_SIZE, PRIMER_OPT_SIZE);
        new SpinnerValidators.Linker(PRIMER_OPT_SIZE, PRIMER_MAX_SIZE);

        new SpinnerValidators.Linker(PRIMER_MIN_GC, PRIMER_OPT_GC_PERCENT);
        new SpinnerValidators.Linker(PRIMER_OPT_GC_PERCENT, PRIMER_MAX_GC);

        new SpinnerValidators.Linker(PRIMER_PRODUCT_MIN_TM, PRIMER_PRODUCT_OPT_TM);
        new SpinnerValidators.Linker(PRIMER_PRODUCT_OPT_TM, PRIMER_PRODUCT_MAX_TM);
    }

    List<String> validateInput() {
        List<String> ret = new ArrayList<String>();
        return ret;
    }

    void updateUserInputFromUI(UserInput userInput) {
        userInput.set("PRIMER_MIN_TM", PRIMER_MIN_TM.getValue().toString());
        userInput.set("PRIMER_OPT_TM", PRIMER_OPT_TM.getValue().toString());
        userInput.set("PRIMER_MAX_TM", PRIMER_MAX_TM.getValue().toString());

        userInput.set("PRIMER_MIN_SIZE", PRIMER_MIN_SIZE.getValue().toString());
        userInput.set("PRIMER_OPT_SIZE", PRIMER_OPT_SIZE.getValue().toString());
        userInput.set("PRIMER_MAX_SIZE", PRIMER_MAX_SIZE.getValue().toString());

        userInput.set("PRIMER_MIN_GC", PRIMER_MIN_GC.getValue().toString());
        userInput.set("PRIMER_OPT_GC_PERCENT", PRIMER_OPT_GC_PERCENT.getValue().toString());
        userInput.set("PRIMER_MAX_GC", PRIMER_MAX_GC.getValue().toString());

        userInput.set("PRIMER_PRODUCT_MIN_TM", PRIMER_PRODUCT_MIN_TM.getValue().toString());
        userInput.set("PRIMER_PRODUCT_OPT_TM", PRIMER_PRODUCT_OPT_TM.getValue().toString());
        userInput.set("PRIMER_PRODUCT_MAX_TM", PRIMER_PRODUCT_MAX_TM.getValue().toString());

        userInput.set("PRIMER_PAIR_MAX_DIFF_TM", PRIMER_PAIR_MAX_DIFF_TM.getValue().toString());
        userInput.set("PRIMER_MAX_END_STABILITY", PRIMER_MAX_END_STABILITY.getValue().toString());
        userInput.set("PRIMER_MAX_TEMPLATE_MISPRIMING_TH", PRIMER_MAX_TEMPLATE_MISPRIMING_TH.getValue().toString());
        userInput.set("PRIMER_PAIR_MAX_TEMPLATE_MISPRIMING_TH", PRIMER_PAIR_MAX_TEMPLATE_MISPRIMING_TH.getValue().toString());
        userInput.set("PRIMER_MAX_SELF_ANY_TH", PRIMER_MAX_SELF_ANY_TH.getValue().toString());
        userInput.set("PRIMER_MAX_SELF_END_TH", PRIMER_MAX_SELF_END_TH.getValue().toString());
        userInput.set("PRIMER_PAIR_MAX_COMPL_ANY_TH", PRIMER_PAIR_MAX_COMPL_ANY_TH.getValue().toString());
        userInput.set("PRIMER_PAIR_MAX_COMPL_END_TH", PRIMER_PAIR_MAX_COMPL_END_TH.getValue().toString());
        userInput.set("PRIMER_MAX_HAIRPIN_TH", PRIMER_MAX_HAIRPIN_TH.getValue().toString());
        userInput.set("PRIMER_MAX_POLY_X", PRIMER_MAX_POLY_X.getValue().toString());
        userInput.set("PRIMER_GC_CLAMP", PRIMER_GC_CLAMP.getValue().toString());
        userInput.set("PRIMER_MAX_LIBRARY_MISPRIMING", PRIMER_MAX_LIBRARY_MISPRIMING.getValue().toString());
        userInput.set("PRIMER_PAIR_MAX_LIBRARY_MISPRIMING", PRIMER_PAIR_MAX_LIBRARY_MISPRIMING.getValue().toString());

    }

    void populateUI(UserInput userInput) {
        if (userInput == null) {
            return;
        }

        String primerMinTm = userInput.get("PRIMER_MIN_TM");
        String primerOptTm = userInput.get("PRIMER_OPT_TM");
        String primerMaxTm = userInput.get("PRIMER_MAX_TM");

        PRIMER_MIN_TM.setValue(Float.parseFloat(primerMinTm));
        PRIMER_OPT_TM.setValue(Float.parseFloat(primerOptTm));
        PRIMER_MAX_TM.setValue(Float.parseFloat(primerMaxTm));

        String primerMinSize = userInput.get("PRIMER_MIN_SIZE");
        String primerOptSize = userInput.get("PRIMER_OPT_SIZE");
        String primerMaxSize = userInput.get("PRIMER_MAX_SIZE");

        PRIMER_MIN_SIZE.setValue(Integer.parseInt(primerMinSize));
        PRIMER_OPT_SIZE.setValue(Integer.parseInt(primerOptSize));
        PRIMER_MAX_SIZE.setValue(Integer.parseInt(primerMaxSize));

        String primerMinGC = userInput.get("PRIMER_MIN_GC");
        String primerOptGC = userInput.get("PRIMER_OPT_GC_PERCENT");
        String primerMaxGC = userInput.get("PRIMER_MAX_GC");

        PRIMER_MIN_GC.setValue(Float.parseFloat(primerMinGC));
        if (!primerOptGC.isEmpty()) {
            PRIMER_OPT_GC_PERCENT.setValue(Float.parseFloat(primerOptGC));
        }
        PRIMER_MAX_GC.setValue(Float.parseFloat(primerMaxGC));

        String primerProductMinSize = userInput.get("PRIMER_PRODUCT_MIN_TM");
        String primerProductOptSize = userInput.get("PRIMER_PRODUCT_OPT_TM");
        String primerProductMaxSize = userInput.get("PRIMER_PRODUCT_MAX_TM");

        if (!primerProductMinSize.isEmpty()) {
            PRIMER_PRODUCT_MIN_TM.setValue(Float.parseFloat(primerProductMinSize));
        }
        if (!primerProductOptSize.isEmpty()) {
            PRIMER_PRODUCT_OPT_TM.setValue(Float.parseFloat(primerProductOptSize));
        }
        if (!primerProductMaxSize.isEmpty()) {
            PRIMER_PRODUCT_MAX_TM.setValue(Float.parseFloat(primerProductMaxSize));
        }

        String primerPairMaxDiffTm = userInput.get("PRIMER_PAIR_MAX_DIFF_TM");
        String primerMaxEndStability = userInput.get("PRIMER_MAX_END_STABILITY");
        String primerMaxSelfAnyTH = userInput.get("PRIMER_MAX_SELF_ANY_TH");
        String primerMaxTemplateMisprimingTH = userInput.get("PRIMER_MAX_TEMPLATE_MISPRIMING_TH");
        String primerPairMaxTemplateMisprimingTH = userInput.get("PRIMER_PAIR_MAX_TEMPLATE_MISPRIMING_TH");
        String primerMaxSelfEndTH = userInput.get("PRIMER_MAX_SELF_END_TH");
        String primerPairMaxComplAnyTH = userInput.get("PRIMER_PAIR_MAX_COMPL_ANY_TH");
        String primerPairMaxComplEndTH = userInput.get("PRIMER_PAIR_MAX_COMPL_END_TH");
        String primerMaxHairpinTH = userInput.get("PRIMER_MAX_HAIRPIN_TH");
        String primerMaxPolyX = userInput.get("PRIMER_MAX_POLY_X");
        String primerGcClamp = userInput.get("PRIMER_GC_CLAMP");
        String primerMaxLibraryMispriming = userInput.get("PRIMER_MAX_LIBRARY_MISPRIMING");
        String primerPairMaxLibraryMispriming = userInput.get("PRIMER_PAIR_MAX_LIBRARY_MISPRIMING");

        PRIMER_PAIR_MAX_DIFF_TM.setValue(Float.parseFloat(primerPairMaxDiffTm));
        PRIMER_MAX_END_STABILITY.setValue(Float.parseFloat(primerMaxEndStability));
        PRIMER_MAX_TEMPLATE_MISPRIMING_TH.setValue(Float.parseFloat(primerMaxTemplateMisprimingTH));
        PRIMER_PAIR_MAX_TEMPLATE_MISPRIMING_TH.setValue(Float.parseFloat(primerPairMaxTemplateMisprimingTH));
        PRIMER_MAX_SELF_ANY_TH.setValue(Float.parseFloat(primerMaxSelfAnyTH));
        PRIMER_MAX_SELF_END_TH.setValue(Float.parseFloat(primerMaxSelfEndTH));
        PRIMER_PAIR_MAX_COMPL_ANY_TH.setValue(Float.parseFloat(primerPairMaxComplAnyTH));
        PRIMER_PAIR_MAX_COMPL_END_TH.setValue(Float.parseFloat(primerPairMaxComplEndTH));
        PRIMER_MAX_HAIRPIN_TH.setValue(Float.parseFloat(primerMaxHairpinTH));
        PRIMER_MAX_POLY_X.setValue(Integer.parseInt(primerMaxPolyX));
        PRIMER_GC_CLAMP.setValue(Integer.parseInt(primerGcClamp));
        PRIMER_MAX_LIBRARY_MISPRIMING.setValue(Float.parseFloat(primerMaxLibraryMispriming));
        PRIMER_PAIR_MAX_LIBRARY_MISPRIMING.setValue(Float.parseFloat(primerPairMaxLibraryMispriming));

    }

    /*
     * PRIMER_MAX_TM
     */
    String getPrimerMaxTm() {
        Integer retInt = (Integer) PRIMER_MAX_TM.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_MIN_TM
     */
    String getPrimerMinTm() {
        Integer retInt = (Integer) PRIMER_MIN_TM.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_OPT_TM
     */
    String getPrimerOptTm() {
        Integer retInt = (Integer) PRIMER_OPT_TM.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_MAX_SIZE
     */
    String getPrimerMaxSize() {
        Integer retInt = (Integer) PRIMER_MAX_SIZE.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_MIN_SIZE
     */
    String getPrimerMinSize() {
        Integer retInt = (Integer) PRIMER_MIN_SIZE.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_OPT_SIZE
     */
    String getPrimerOptSize() {
        Integer retInt = (Integer) PRIMER_OPT_SIZE.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_OPT_GC_PERCENT
     */
    String getPrimerOptGCPercent() {
        Integer retInt = (Integer) PRIMER_OPT_GC_PERCENT.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_MIN_GC
     */
    String getPrimerMinGC() {
        Integer retInt = (Integer) PRIMER_MIN_GC.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_MAX_GC
     */
    String getPrimerMaxGC() {
        Integer retInt = (Integer) PRIMER_MAX_GC.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_PRODUCT_MAX_TM
     */
    String getPrimerProductMaxTm() {
        Integer retInt = (Integer) PRIMER_PRODUCT_MAX_TM.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_PRODUCT_MIN_TM
     */
    String getPrimerProductMinTm() {
        Integer retInt = (Integer) PRIMER_PRODUCT_MIN_TM.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_PRODUCT_OPT_TM
     */
    String getPrimerProductOptTm() {
        Integer retInt = (Integer) PRIMER_PRODUCT_OPT_TM.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_MAX_DIFF_TM
     */
    String getPrimerMaxDiffTm() {
        Integer retInt = (Integer) PRIMER_PAIR_MAX_DIFF_TM.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_MAX_END_STABILITY
     */
    String getPrimerMaxEndStability() {
        Integer retInt = (Integer) PRIMER_MAX_END_STABILITY.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_SELF_ANY
     */
    String getPrimerSelfAny() {
        Integer retInt = (Integer) PRIMER_MAX_SELF_ANY_TH.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_SELF_END
     */
    String getPrimerSelfEnd() {
        Integer retInt = (Integer) PRIMER_MAX_SELF_END_TH.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_MAX_POLY_X
     */
    String getPrimerMaxPolyX() {
        Integer retInt = (Integer) PRIMER_MAX_POLY_X.getValue();
        return retInt.toString();
    }

    /*
     * PRIMER_GC_CLAMP
     */
    String getPrimerGcClamp() {
        Integer retInt = (Integer) PRIMER_GC_CLAMP.getValue();
        return retInt.toString();
    }

    private void initComponents() {

        PRIMER_MIN_TM = new JSpinner();
        PRIMER_OPT_TM = new JSpinner();
        PRIMER_MAX_TM = new JSpinner();
        PRIMER_MIN_SIZE = new JSpinner();
        PRIMER_OPT_SIZE = new JSpinner();
        PRIMER_MAX_SIZE = new JSpinner();
        PRIMER_MIN_GC = new JSpinner();
        PRIMER_OPT_GC_PERCENT = new JSpinner();
        PRIMER_MAX_GC = new JSpinner();
        PRIMER_PRODUCT_MIN_TM = new JSpinner();
        PRIMER_PRODUCT_OPT_TM = new JSpinner();
        PRIMER_PRODUCT_MAX_TM = new JSpinner();

        PRIMER_GC_CLAMP = new JSpinner();
        PRIMER_MAX_LIBRARY_MISPRIMING = new JSpinner();
        PRIMER_PAIR_MAX_LIBRARY_MISPRIMING = new JSpinner();
        tmCalculationBtn = new JButton();
        pickingWeightsBtn = new JButton();

        final Insets insets = UIUtil.getDefaultHorizontalInsets();

        JPanel primerTmPanel = createPrimerTmPanel();
        primerTmPanel.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, 0));

        JPanel primerSizePanel = createPrimerSizePanel();
        primerSizePanel.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, 0));

        JPanel primerGcPanel = createPrimerGcPanel();
        primerGcPanel.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, 0));

        JPanel productTmPanel = createProductTmPanel();
        productTmPanel.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, 0));

        JPanel structurePanel = create2ndStructureAlignments();

        JPanel otherConditionsPanel = createOtherConditionsPanel();

        this.setLayout(new VerticalLayout());
        add(primerTmPanel);
        add(primerSizePanel);
        add(primerGcPanel);
        add(productTmPanel);

        CollapsibleTitlePanel structure = new CollapsibleTitlePanel("Secondary Structure Alignments");
        structure.getContentPane().add(structurePanel, BorderLayout.CENTER);
        UIUtil.setBackground(structurePanel, CNST.BG, JPanel.class);
        structure.setCollapsed(true);
        add(structure);

        CollapsibleTitlePanel other = new CollapsibleTitlePanel("Other Conditions");
        other.getContentPane().add(otherConditionsPanel, BorderLayout.CENTER);
        UIUtil.setBackground(otherConditionsPanel, CNST.BG, JPanel.class);
        other.setCollapsed(true);
        add(other);
    }

    private JPanel createPrimerTmPanel() {
        JPanel ret = new JPanel(new BorderLayout());

        JPanel primerTmPanel = new JPanel(new GridBagLayout());

        GridBagConstraints c = null;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;        
        primerTmPanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MIN_16)), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        PRIMER_MIN_TM.setModel(new SpinnerNumberModel(/*cannot use primitive here*/Float.valueOf(1.0f), null, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_MIN_TM, 1000);
        primerTmPanel.add(PRIMER_MIN_TM, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        primerTmPanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        PRIMER_OPT_TM.setModel(new SpinnerNumberModel(/*cannot use primitive here*/Float.valueOf(1.0f), null, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_OPT_TM, 1000);
        primerTmPanel.add(PRIMER_OPT_TM, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        primerTmPanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        PRIMER_MAX_TM.setModel(new SpinnerNumberModel(/*cannot use primitive here*/Float.valueOf(1.0f), null, null, 0.1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_MAX_TM, 1000);
        primerTmPanel.add(PRIMER_MAX_TM, c);
        
        c = new GridBagConstraints();
        c.gridy = gridy;        
        primerTmPanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MAX_16)), c);

        RichSeparator separator = new RichSeparator(String.format("Primer TM(%s)", Unicodes.CELSIUS));
        ret.add(separator, BorderLayout.NORTH);
        ret.add(primerTmPanel, BorderLayout.CENTER);

        return ret;
    }

    private JPanel createPrimerSizePanel() {
        JPanel ret = new JPanel(new BorderLayout());

        JPanel primerSizePanel = new JPanel(new GridBagLayout());

        PRIMER_MIN_SIZE.setModel(new SpinnerNumberModel(1, null, null, 1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_MIN_SIZE, 1000);

        PRIMER_OPT_SIZE.setModel(new SpinnerNumberModel(1, null, null, 1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_OPT_SIZE, 1000);

        PRIMER_MAX_SIZE.setModel(new SpinnerNumberModel(1, null, IPrimer3Service.PRIMER_LENGTH_MAX, 1));
        UIUtil.setPreferredWidthByPrototype(PRIMER_MAX_SIZE, 1000);

        GridBagConstraints c = null;

        c = new GridBagConstraints();
        primerSizePanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MIN_16)), c);

        c = new GridBagConstraints();
        primerSizePanel.add(PRIMER_MIN_SIZE, c);

        c = new GridBagConstraints();
        primerSizePanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        primerSizePanel.add(PRIMER_OPT_SIZE, c);

        c = new GridBagConstraints();
        primerSizePanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        primerSizePanel.add(PRIMER_MAX_SIZE, c);
        
        c = new GridBagConstraints();
        primerSizePanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MAX_16)), c);        

        RichSeparator separator = new RichSeparator("Primer Size");
        ret.add(separator, BorderLayout.NORTH);
        ret.add(primerSizePanel, BorderLayout.CENTER);

        return ret;
    }

    private JPanel createPrimerGcPanel() {
        JPanel ret = new JPanel(new BorderLayout());

        JPanel primerGcPanel = new JPanel();

        PRIMER_MIN_GC.setModel(new SpinnerNumberModel(Float.valueOf(1.0f), null, null, 0.1f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_MIN_GC, 1000);

        PRIMER_OPT_GC_PERCENT.setModel(new SpinnerNumberModel(Float.valueOf(1.0f), null, null, 0.1f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_OPT_GC_PERCENT, 1000);

        PRIMER_MAX_GC.setModel(new SpinnerNumberModel(Float.valueOf(1.0f), null, null, 0.1f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_MAX_GC, 1000);

        primerGcPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        primerGcPanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MIN_16)), c);  

        c = new GridBagConstraints();
        primerGcPanel.add(PRIMER_MIN_GC, c);

        c = new GridBagConstraints();
        primerGcPanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        primerGcPanel.add(PRIMER_OPT_GC_PERCENT, c);

        c = new GridBagConstraints();
        primerGcPanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        primerGcPanel.add(PRIMER_MAX_GC, c);
        
        c = new GridBagConstraints();
        primerGcPanel.add(new JLabel(ImageHelper.createImageIcon(ImageNames.MAX_16)), c);          

        RichSeparator separator = new RichSeparator("Primer GC%");
        ret.add(separator, BorderLayout.NORTH);
        ret.add(primerGcPanel, BorderLayout.CENTER);

        return ret;
    }

    private JPanel createProductTmPanel() {
        JPanel ret = new JPanel(new BorderLayout());

        JPanel productTmPanel = new JPanel();

        PRIMER_PRODUCT_MIN_TM.setModel(new SpinnerNumberModel(Float.valueOf(1.0f), null, null, 1.0f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PRODUCT_MIN_TM, 1000);

        PRIMER_PRODUCT_OPT_TM.setModel(new SpinnerNumberModel(Float.valueOf(1.0f), null, null, 1.0f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PRODUCT_OPT_TM, 1000);

        PRIMER_PRODUCT_MAX_TM.setModel(new SpinnerNumberModel(Float.valueOf(1.0f), null, null, 1.0f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PRODUCT_MAX_TM, 1000);

        productTmPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        JLabel labelMin = new JLabel(ImageHelper.createImageIcon(ImageNames.MIN_16));
        labelMin.setToolTipText("Min");
        productTmPanel.add(labelMin, c);

        c = new GridBagConstraints();
        productTmPanel.add(PRIMER_PRODUCT_MIN_TM, c);

        c = new GridBagConstraints();
        productTmPanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        productTmPanel.add(PRIMER_PRODUCT_OPT_TM, c);

        c = new GridBagConstraints();
        productTmPanel.add(new JLabel("-"), c);

        c = new GridBagConstraints();
        productTmPanel.add(PRIMER_PRODUCT_MAX_TM, c);

        c = new GridBagConstraints();
        JLabel labelMax = new JLabel(ImageHelper.createImageIcon(ImageNames.MAX_16));
        labelMax.setToolTipText("Max");
        productTmPanel.add(labelMax, c);          
        
        RichSeparator separator = new RichSeparator(String.format("Product TM(%s)", Unicodes.CELSIUS));

        ret.add(separator, BorderLayout.NORTH);
        ret.add(productTmPanel, BorderLayout.CENTER);

        return ret;
    }

    private JPanel create2ndStructureAlignments() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.gridwidth = 2;
        JLabel label = new JLabel("Using Thermodynamic Model");
        label.setForeground(Color.GRAY);
        label.setFont(label.getFont().deriveFont(Font.ITALIC));
        ret.add(label, c);

        // primer max template mispriming
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Max Template Mispriming:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        PRIMER_MAX_TEMPLATE_MISPRIMING_TH = new JSpinner();
        PRIMER_MAX_TEMPLATE_MISPRIMING_TH.setModel(new SpinnerNumberModel(0f, 0f, null, 0.05f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_MAX_TEMPLATE_MISPRIMING_TH, 100);
        ret.add(PRIMER_MAX_TEMPLATE_MISPRIMING_TH, c);

        // primer pair max template mispriming
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Pair Max Template Mispriming:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        PRIMER_PAIR_MAX_TEMPLATE_MISPRIMING_TH = new JSpinner();
        PRIMER_PAIR_MAX_TEMPLATE_MISPRIMING_TH.setModel(new SpinnerNumberModel(0f, 0f, null, 0.05f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PAIR_MAX_TEMPLATE_MISPRIMING_TH, 100);
        ret.add(PRIMER_PAIR_MAX_TEMPLATE_MISPRIMING_TH, c);

        // max self complementarity
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Max Self Complementarity:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        PRIMER_MAX_SELF_ANY_TH = new JSpinner();
        PRIMER_MAX_SELF_ANY_TH.setModel(new SpinnerNumberModel(0f, 0f, null, 0.05f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_MAX_SELF_ANY_TH, 100);
        ret.add(PRIMER_MAX_SELF_ANY_TH, c);

        // max 3' self complementarity
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Max 3' Self Compementarity:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        PRIMER_MAX_SELF_END_TH = new JSpinner();
        PRIMER_MAX_SELF_END_TH.setModel(new SpinnerNumberModel(0f, 0f, null, 0.05f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_MAX_SELF_END_TH, 100);
        ret.add(PRIMER_MAX_SELF_END_TH, c);

        // max pair complementarity
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Max Pair Complementarity:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        PRIMER_PAIR_MAX_COMPL_ANY_TH = new JSpinner();
        PRIMER_PAIR_MAX_COMPL_ANY_TH.setModel(new SpinnerNumberModel(0f, 0f, null, 0.05f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PAIR_MAX_COMPL_ANY_TH, 100);
        ret.add(PRIMER_PAIR_MAX_COMPL_ANY_TH, c);

        // max 3' pair complementarity
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Max 3' Pair Compementarity:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        PRIMER_PAIR_MAX_COMPL_END_TH = new JSpinner();
        PRIMER_PAIR_MAX_COMPL_END_TH.setModel(new SpinnerNumberModel(0f, 0f, null, 0.05f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PAIR_MAX_COMPL_END_TH, 100);
        ret.add(PRIMER_PAIR_MAX_COMPL_END_TH, c);

        // max primer hairpin
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Max Primer Hairpin:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        PRIMER_MAX_HAIRPIN_TH = new JSpinner();
        PRIMER_MAX_HAIRPIN_TH.setModel(new SpinnerNumberModel(0f, 0f, null, 0.05f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_MAX_HAIRPIN_TH, 100);
        ret.add(PRIMER_MAX_HAIRPIN_TH, c);

        return ret;
    }

    private JPanel createOtherConditionsPanel() {
        JPanel otherConditionsPanel = new JPanel();

        PRIMER_GC_CLAMP.setModel(new SpinnerNumberModel(0, 0, null, 1));

        PRIMER_PAIR_MAX_LIBRARY_MISPRIMING.setModel(new SpinnerNumberModel(0f, 0f, null, 0.05f));

        tmCalculationBtn.setText("Tm Calculation");
        tmCalculationBtn.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tmCalculationBtnActionPerformed(evt);
            }
        });

        pickingWeightsBtn.setText("Picking Weights");
        pickingWeightsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pickingWeightsBtnActionPerformed(evt);
            }
        });


        otherConditionsPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = null;

        int gridy = 0;

        // max tm difference
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        otherConditionsPanel.add(new JLabel("Max Tm Difference"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        PRIMER_PAIR_MAX_DIFF_TM = new JSpinner();
        PRIMER_PAIR_MAX_DIFF_TM.setModel(new SpinnerNumberModel(0.1f, 0.1f, null, 0.05f));
        UIUtil.setPreferredWidthByPrototype(PRIMER_PAIR_MAX_DIFF_TM, 100);
        otherConditionsPanel.add(PRIMER_PAIR_MAX_DIFF_TM, c);

        // max 3' stability
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        otherConditionsPanel.add(new JLabel("Max 3' Stability"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        PRIMER_MAX_END_STABILITY = new JSpinner();
        PRIMER_MAX_END_STABILITY.setModel(new SpinnerNumberModel(0f, 0f, null, 0.05f));
        otherConditionsPanel.add(PRIMER_MAX_END_STABILITY, c);

        // max poly-x 
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        otherConditionsPanel.add(new JLabel("Max Poly-X:"), c);

        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        PRIMER_MAX_POLY_X = new JSpinner();
        PRIMER_MAX_POLY_X.setModel(new SpinnerNumberModel(0, 0, null, 1));
        otherConditionsPanel.add(PRIMER_MAX_POLY_X, c);

        // gc clamp
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        otherConditionsPanel.add(new JLabel("GC Clamp:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        otherConditionsPanel.add(PRIMER_GC_CLAMP, c);

        //max mispriming
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        otherConditionsPanel.add(new JLabel("Max Library Mispriming:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        PRIMER_MAX_LIBRARY_MISPRIMING.setModel(new SpinnerNumberModel(0, 0, null, 0.05f));
        otherConditionsPanel.add(PRIMER_MAX_LIBRARY_MISPRIMING, c);

        // max pair mispriming
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        otherConditionsPanel.add(new JLabel("Pair Max Mispriming:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        otherConditionsPanel.add(PRIMER_PAIR_MAX_LIBRARY_MISPRIMING, c);

        // button panel
        JPanel btnPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        otherConditionsPanel.add(btnPanel, c);

        c = new GridBagConstraints();
        btnPanel.add(tmCalculationBtn, c);

        c = new GridBagConstraints();
        btnPanel.add(pickingWeightsBtn, c);

        return otherConditionsPanel;
    }

    private void tmCalculationBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Primer3Panel primer3Panel = UIUtil.getParent(this, Primer3Panel.class);
        UserInput userInput = primer3Panel.getP3output().getUserInput();
        if (userInput == null) {
            return;
        }
        TmCalSettingsPanel tmCalculationSettingsPanel = new PrimerTmCalSettingsPanel();
        tmCalculationSettingsPanel.populateUI(userInput);
        DialogDescriptor dd = new DialogDescriptor(tmCalculationSettingsPanel, "Tm Calculation Settings");
        Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);
        if (JOptionPane.OK_OPTION == answer) {
            tmCalculationSettingsPanel.updateUserInputFromUI(userInput);
        }
    }

    private void pickingWeightsBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Primer3Panel primer3Panel = UIUtil.getParent(this, Primer3Panel.class);
        PenaltyWeightsPanel penaltyWeightsPanel = new PenaltyWeightsPanel(true, true, false);
        penaltyWeightsPanel.setUserInput(primer3Panel.getP3output().getUserInput());
        DialogDescriptor dd = new DialogDescriptor(penaltyWeightsPanel, "Penalty Weights");
        Object answer = DialogDisplayer.getDefault().notify(dd);
        if (answer.equals(DialogDescriptor.OK_OPTION)) {
            penaltyWeightsPanel.updateUserInputFromUI();
        }
    }
    private JSpinner PRIMER_GC_CLAMP;
    private JSpinner PRIMER_PAIR_MAX_DIFF_TM;
    private JSpinner PRIMER_MAX_END_STABILITY;
    private JSpinner PRIMER_MAX_HAIRPIN_TH;
    private JSpinner PRIMER_MAX_GC;
    private JSpinner PRIMER_MAX_LIBRARY_MISPRIMING;
    private JSpinner PRIMER_MAX_POLY_X;
    private JSpinner PRIMER_MAX_SIZE;
    private JSpinner PRIMER_MAX_TM;
    private JSpinner PRIMER_MIN_GC;
    private JSpinner PRIMER_MIN_SIZE;
    private JSpinner PRIMER_MIN_TM;
    private JSpinner PRIMER_OPT_GC_PERCENT;
    private JSpinner PRIMER_OPT_SIZE;
    private JSpinner PRIMER_OPT_TM;
    private JSpinner PRIMER_PAIR_MAX_LIBRARY_MISPRIMING;
    private JSpinner PRIMER_PRODUCT_MAX_TM;
    private JSpinner PRIMER_PRODUCT_MIN_TM;
    private JSpinner PRIMER_PRODUCT_OPT_TM;
    private JSpinner PRIMER_MAX_TEMPLATE_MISPRIMING_TH;
    private JSpinner PRIMER_PAIR_MAX_TEMPLATE_MISPRIMING_TH;
    private JSpinner PRIMER_MAX_SELF_ANY_TH;
    private JSpinner PRIMER_MAX_SELF_END_TH;
    private JSpinner PRIMER_PAIR_MAX_COMPL_ANY_TH;
    private JSpinner PRIMER_PAIR_MAX_COMPL_END_TH;
    private JButton pickingWeightsBtn;
    private JButton tmCalculationBtn;

    @Override
    public void expanded() {
        Primer3View primer3View = UIUtil.getParent(this, Primer3View.class);
        if (primer3View == null) {
            return;
        }
        TaskPanel taskPanel = primer3View.getTaskPanel();
        UIUtil.enabledRecursively(this, taskPanel.leftPickPanel.isPicking()
                || taskPanel.rightPickPanel.isPicking());
    }
}
