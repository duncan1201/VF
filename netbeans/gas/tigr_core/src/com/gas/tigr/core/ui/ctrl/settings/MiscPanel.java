/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.settings;

import com.gas.domain.core.tigr.TIGRSettings;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author dq
 */
public class MiscPanel extends JPanel {

    TIGRSettings settings;
    JCheckBox trimmedSeq;
    JCheckBox includeSingletons;
    JCheckBox keepBadSeqs;
    JCheckBox considerLowScores;

    public MiscPanel() {
        initComp();
        hookupListeners();
    }

    private void initComp() {
        setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Other conditions", TitledBorder.LEFT, TitledBorder.TOP));
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        trimmedSeq = new JCheckBox("Sequence are trimmed");
        add(trimmedSeq, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        includeSingletons = new JCheckBox("Include singletons");
        add(includeSingletons, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        keepBadSeqs = new JCheckBox("Keep potential chimeras and splice variants");
        add(keepBadSeqs, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        considerLowScores = new JCheckBox("Consider seq with low pairwise scores");
        add(considerLowScores, c);
    }
    
    public void updateSettingsFromUI(TIGRSettings settings){
        settings.setLowScores(considerLowScores.isSelected());
        settings.setIncludeBadSeq(keepBadSeqs.isSelected());
        settings.setGenerateSingletons(includeSingletons.isSelected());
        settings.setTrimmedSeq(trimmedSeq.isSelected());
    }

    private void hookupListeners() {
        addPropertyChangeListener(new MiscPanelListeners.PtyListener());
    }

    public void setSettings(TIGRSettings settings) {
        TIGRSettings old = this.settings;
        this.settings = settings;
        firePropertyChange("settings", old, this.settings);
    }
}
