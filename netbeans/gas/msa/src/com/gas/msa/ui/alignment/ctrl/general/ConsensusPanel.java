/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.ctrl.general;

import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.msa.service.api.IConsensusService;
import com.gas.domain.core.msa.MSA;
import com.gas.msa.ui.alignment.AlignPane;
import java.awt.*;
import java.lang.ref.WeakReference;
import javax.swing.*;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ConsensusPanel extends JPanel {

    private IConsensusService service = Lookup.getDefault().lookup(IConsensusService.class);
    protected WeakReference<JCheckBox> ignoreGapsRef;
    protected WeakReference<JRadioButton> pluralityRef;
    protected WeakReference<JRadioButton> majorityRef;
    protected WeakReference<JSpinner> thresholdRef;
    protected WeakReference<AlignPane> alignPaneRef;
    static boolean populatingUI;

    public ConsensusPanel() {
        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);

        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        JRadioButton plurality = new JRadioButton("Plurality");
        add(plurality, c);
        pluralityRef = new WeakReference<JRadioButton>(plurality);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        JRadioButton majority = new JRadioButton("Majority");
        Insets insets = majority.getInsets();
        add(majority, c);
        majorityRef = new WeakReference<JRadioButton>(majority);

        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        JSpinner threshold = new JSpinner();
        threshold.setModel(new SpinnerNumberModel(0.5f, 0.5f, 1, 0.01f));
        threshold.setEditor(new JSpinner.NumberEditor(threshold, "##%"));
        add(threshold, c);
        thresholdRef = new WeakReference<JSpinner>(threshold);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        JCheckBox ignoreGaps = new JCheckBox("Ignore Gaps");
        add(ignoreGaps, c);
        ignoreGapsRef = new WeakReference<JCheckBox>(ignoreGaps);


        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(plurality);
        bGroup.add(majority);
    }

    protected AlignPane getAlignPane() {
        if (alignPaneRef == null || alignPaneRef.get() == null) {
            AlignPane m = UIUtil.getParent(this, AlignPane.class);
            alignPaneRef = new WeakReference<AlignPane>(m);
        }
        return alignPaneRef.get();
    }

    protected void recalculateConsensus() {
        getAlignPane().getMsaScroll().getColumnHeaderUI().getColumnHeaderComp().recalculateRefreshConsensusUI();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new ConsensusPanelListeners.PtyListener());
        ignoreGapsRef.get().addItemListener(new ConsensusPanelListeners.GapListener(new WeakReference<ConsensusPanel>(this)));
        pluralityRef.get().addActionListener(new ConsensusPanelListeners.PluralityListener(new WeakReference<ConsensusPanel>(this)));
        majorityRef.get().addActionListener(new ConsensusPanelListeners.MajorityListener(new WeakReference<ConsensusPanel>(this)));
        thresholdRef.get().addChangeListener(new ConsensusPanelListeners.ThresholdListener(new WeakReference<ConsensusPanel>(this)));
    }

    void populateUI(MSA msa) {
        populatingUI = true;
        boolean ignoreGap = msa.getConsensusParam().isIgnoreGaps();
        boolean plurality = msa.getConsensusParam().isPlurality();
        float threshold = msa.getConsensusParam().getThreshold();
        ignoreGapsRef.get().setSelected(ignoreGap);
        if (plurality) {
            pluralityRef.get().setSelected(true);
            thresholdRef.get().setEnabled(false);
        } else {
            majorityRef.get().setSelected(true);
            thresholdRef.get().setEnabled(true);
        }
        thresholdRef.get().setValue(threshold);
        populatingUI = false;
    }
}
