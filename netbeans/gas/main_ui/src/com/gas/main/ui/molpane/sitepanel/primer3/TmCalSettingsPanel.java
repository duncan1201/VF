/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJPanel.java
 *
 * Created on Jan 9, 2012, 3:24:48 PM
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.UserInput;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author dq
 */
public abstract class TmCalSettingsPanel extends JPanel {

    /**
     * Creates new form NewJPanel
     */
    public TmCalSettingsPanel() {
        initComponents();
    }

    public abstract void updateUserInputFromUI(UserInput userInput);

    public abstract void populateUI(UserInput userInput);

    /**
     * PRIMER_DNA_CONC or PRIMER_INTERNAL_DNA_CONC
     */
    public String getDnaConc() {
        String ret = null;
        ret = dnaConc.getValue().toString();
        return ret;
    }

    /**
     * PRIMER_SALT_MONOVALENT or PRIMER_INTERNAL_SALT_MONOVALENT
     */
    public String getSaltMonovalent() {
        String ret = null;
        ret = saltMonovalent.getValue().toString();
        return ret;
    }

    /**
     * PRIMER_SALT_DIVALENT or PRIMER_INTERNAL_SALT_DIVALENT
     */
    public String getSaltDivalent() {
        String ret = saltDivalent.getValue().toString();
        return ret;
    }

    /**
     * PRIMER_DNTP_CONC or PRIMER_INTERNAL_DNTP_CONC
     */
    public String getDntpConc() {
        String ret = dntpConc.getValue().toString();
        return ret;
    }
    
    private StringList getThermodynamicParameters() {
        StringList ret = new StringList(ThermoParam.getAllNames());
        return ret;
    }

    private StringList getSaltCorrectionFormula() {
        StringList ret = new StringList(SaltCorrectionFormula.getAllNames());
        return ret;
    }

    private void initComponents() {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));

        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;        
        JPanel panel = createConcentrationsPanel();
        add(panel, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.insets = UIUtil.getDefaultVerticalInsets();
        JPanel panel2 = createFormulasPanel();
        add(panel2, c);

    }

    private JPanel createFormulasPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;

        StringList thermoParams = getThermodynamicParameters();
        StringList saltCorrFormulas = getSaltCorrectionFormula();

        String longest = thermoParams.longest().length() > saltCorrFormulas.longest().length() ? thermoParams.longest() : saltCorrFormulas.longest();
        Dimension sizeCombo = UIUtil.getSize(longest + "A", JComboBox.class);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Salt Correction Formula:"), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;

        saltCorrections = new JComboBox(saltCorrFormulas.toArray(new String[saltCorrFormulas.size()]));
        UIUtil.setPreferredWidth(saltCorrections, sizeCombo.width);
        ret.add(saltCorrections, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Table of thermodynamics parameters:"), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.anchor = GridBagConstraints.WEST;
        thermoDynamics = new JComboBox(thermoParams.toArray(new String[thermoParams.size()]));
        UIUtil.setPreferredWidth(thermoDynamics, sizeCombo.width);
        ret.add(thermoDynamics, c);

        return ret;
    }

    private JPanel createConcentrationsPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        final Dimension sizeSpinner = UIUtil.getSize(1000, JSpinner.class);

        int gridy = 0;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Monovalent cation concentration(mM):"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        saltMonovalent = new JSpinner(new SpinnerNumberModel(0f, 0f, null, 0.1f));
        UIUtil.setPreferredWidth(saltMonovalent, sizeSpinner.width);
        ret.add(saltMonovalent, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Divalent cation concentration(mM):"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        saltDivalent = new JSpinner(new SpinnerNumberModel(0f, 0f, null, 0.1f));
        UIUtil.setPreferredWidth(saltDivalent, sizeSpinner.width);
        ret.add(saltDivalent, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Annealing oligo concentration(nM):"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        dnaConc = new JSpinner(new SpinnerNumberModel(0f, 0f, null, 0.1f));
        UIUtil.setPreferredWidth(dnaConc, sizeSpinner.width);
        ret.add(dnaConc, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("dNTP concentration(mM):"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = gridy++;
        dntpConc = new JSpinner(new SpinnerNumberModel(0, 0, null, 1));
        UIUtil.setPreferredWidth(dntpConc, sizeSpinner.width);
        ret.add(dntpConc, c);

        return ret;
    }

    protected JSpinner saltDivalent;
    protected JSpinner dnaConc;
    protected JSpinner dntpConc;
    protected JSpinner saltMonovalent;
    protected JComboBox saltCorrections;
    protected JComboBox thermoDynamics;
}
