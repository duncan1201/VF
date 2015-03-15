/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.tree;

import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.pref.MSAPref;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.ref.WeakReference;
import javax.swing.*;

/**
 *
 * @author dq
 */
class BranchPanel extends JPanel {

    private WeakReference<JComboBox> attNamesComboRef;
    private WeakReference<JCheckBox> visibleCheckRef;
    private JSpinner sigDigitSpinner;
    private WeakReference<JSpinner> lineWidthSpinnerRef;
    private String[] attNames;
    private String selectedAttName;

    BranchPanel() {
        initComponents();

        hookupListeners();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Label:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        JComboBox attNamesCombo = new JComboBox();
        add(attNamesCombo, c);
        attNamesComboRef = new WeakReference<JComboBox>(attNamesCombo);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        JCheckBox visibleCheck = new JCheckBox();
        add(visibleCheck, c);
        visibleCheckRef = new WeakReference<JCheckBox>(visibleCheck);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Sig Digits:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        sigDigitSpinner = new JSpinner();
        sigDigitSpinner.setModel(new SpinnerNumberModel(1, 1, 7, 1));
        add(sigDigitSpinner, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Line width:"), c);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 2;
        c.anchor = GridBagConstraints.WEST;
        JSpinner lineWidthSpinner = new JSpinner();
        lineWidthSpinner.setModel(new SpinnerNumberModel(1, 1, 4, 1));
        add(lineWidthSpinner, c);
        lineWidthSpinnerRef = new WeakReference<JSpinner>(lineWidthSpinner);
    }

    void initValues(MSA msa) {
        getVisibleCheck().setSelected(msa.getMsaSetting().isTreeEdgeLabelDisplay());
        getSigDigitSpinner().setValue(msa.getMsaSetting().getSigDigits());
        getLineWidthSpinner().setValue(msa.getMsaSetting().getLineWidth());
    }

    private void hookupListeners() {
        addPropertyChangeListener(new BranchPanelListeners.PtyListener());
        getVisibleCheck().addItemListener(new BranchPanelListeners.VisibleListener());
        getAttNamesCombo().addActionListener(new BranchPanelListeners.AttNameComboListener());
        getSigDigitSpinner().addChangeListener(new BranchPanelListeners.SigDigitListener());
        getLineWidthSpinner().addChangeListener(new BranchPanelListeners.LineWidthListener());
    }

    protected JSpinner getLineWidthSpinner() {
        return lineWidthSpinnerRef.get();
    }

    protected JSpinner getSigDigitSpinner() {
        return sigDigitSpinner;
    }

    protected JCheckBox getVisibleCheck() {
        return visibleCheckRef.get();
    }

    public String[] getAttNames() {
        return attNames;
    }

    public void setAttNames(String[] attNames) {
        String[] old = this.attNames;
        this.attNames = attNames;
        firePropertyChange("attNames", old, this.attNames);
    }

    public String getSelectedAttName() {
        return selectedAttName;
    }

    public void setSelectedAttName(String selectedAttName) {
        String old = this.selectedAttName;
        this.selectedAttName = selectedAttName;
        firePropertyChange("selectedAttName", old, this.selectedAttName);
    }

    public JComboBox getAttNamesCombo() {
        return attNamesComboRef.get();
    }
}
