/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.settings;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.tigr.TIGRSettings;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
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
public class OverlapPanel extends JPanel {

    TIGRSettings tigrSettings;
    JSpinner minimumLengthSpinner; // -l minimum_length
    JSpinner minimumPercentSpinner; // -p minimum_percent
    JSpinner maximumEndSpinner;// maximum_end -e
    JSpinner maxErr32Spinner;// -g max_err_32

    public OverlapPanel() {
        initComp();
        hookupListeners();
    }

    private void initComp() {
        setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Overlap conditions", TitledBorder.LEFT, TitledBorder.TOP));
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1;
        JPanel tmp = createShortPanel();
        add(tmp, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        tmp = createMaxErr32Panel();
        add(tmp, c);
    }

    private void hookupListeners() {
        addPropertyChangeListener(new OverlapPanelListeners.PtyListener());
    }

    private JPanel createMaxErr32Panel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        ret.add(new JLabel("Max mismatches within any 32mer"), c);

        c = new GridBagConstraints();        
        maxErr32Spinner = new JSpinner();
        maxErr32Spinner.setModel(new SpinnerNumberModel(1, 0, 32, 1));
        UIUtil.setPreferredWidthByPrototype(maxErr32Spinner, 1000);
        ret.add(maxErr32Spinner, c);
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component comp = Box.createRigidArea(new Dimension(1,1));
        ret.add(comp, c);

        return ret;
    }

    public void setTigrSettings(TIGRSettings tigrSettings) {
        TIGRSettings old = this.tigrSettings;
        this.tigrSettings = tigrSettings;
        firePropertyChange("tigrSettings", old, this.tigrSettings);
    }
    
    public void updateSettingsFromUI(TIGRSettings settings){
        Number minPercent = (Number)minimumPercentSpinner.getValue();
        settings.setMinPercent(minPercent.floatValue());
        settings.setMinimumLength((Integer)minimumLengthSpinner.getValue());
        settings.setMaxError32((Integer)maxErr32Spinner.getValue());
        settings.setMaximumEnd((Integer)maximumEndSpinner.getValue());
    }

    private JPanel createShortPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Min overlap length"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        minimumLengthSpinner = new JSpinner();
        minimumLengthSpinner.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        UIUtil.setPreferredWidthByPrototype(minimumLengthSpinner, 1000);
        ret.add(minimumLengthSpinner, c);

        // filler
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        Component comp = Box.createRigidArea(new Dimension(1, 1));
        ret.add(comp, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Min overlap % match"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        minimumPercentSpinner = new JSpinner();
        minimumPercentSpinner.setModel(new SpinnerNumberModel(1f, 1f, 100f, 1f));
        UIUtil.setPreferredWidthByPrototype(minimumPercentSpinner, 1000);
        ret.add(minimumPercentSpinner, c);

        // filler
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        comp = Box.createRigidArea(new Dimension(1, 1));
        ret.add(comp, c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Max overlap mismatch"), c);

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        maximumEndSpinner = new JSpinner();
        maximumEndSpinner.setModel(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        UIUtil.setPreferredWidthByPrototype(maximumEndSpinner, 1000);
        ret.add(maximumEndSpinner, c);

        // filler
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        comp = Box.createRigidArea(new Dimension(1, 1));
        ret.add(comp, c);

        return ret;
    }
}
