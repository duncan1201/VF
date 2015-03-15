/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.ui.msa;

import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.common.ui.misc.RichSeparator;
import com.gas.common.ui.util.UIUtil;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author dq
 */
public class PairwiseOptionsUI extends JPanel {

    private JRadioButton slowType = new JRadioButton("Slow");
    private JRadioButton fastType = new JRadioButton("Fast");
    private ButtonGroup buttonGroup;
    private CardLayout cardLayout;
    protected SlowUI slowUI;
    protected FastUI fastUI;
    private JPanel fastSlowPanel;
    private ClustalwParam msaParams;
    // slow
    protected JComboBox weightMatrixCombo = new JComboBox();
    protected JSpinner gapOpenSpinner = new JSpinner();
    protected JSpinner gapExtSpinner = new JSpinner();
    // fast
    protected JSpinner wordSizeSpinner = new JSpinner();
    protected JSpinner windowLengthSpinner = new JSpinner();
    protected JSpinner topDiagsSpinner = new JSpinner();
    protected JSpinner pairGapSpinner = new JSpinner();

    public PairwiseOptionsUI(boolean vertical) {
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        createComponents(vertical);
        hookupListeners();
        initComponents();
    }

    private JPanel createSeparatorComponent() {
        JPanel ret = new JPanel(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        ret.add(new JLabel("<html><b>Pairwise Alignment</b></html>"), c);

        buttonGroup = new ButtonGroup();
        slowType.setActionCommand("slow");
        fastType.setActionCommand("fast");
        buttonGroup.add(slowType);
        buttonGroup.add(fastType);

        c = new GridBagConstraints();
        c.gridy = 0;
        ret.add(slowType, c);

        c = new GridBagConstraints();
        c.gridy = 0;
        ret.add(fastType, c);

        return ret;
    }

    private void createComponents(boolean vertical) {
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        RichSeparator rs = new RichSeparator(createSeparatorComponent());
        add(rs, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.BOTH;
        fastUI = new FastUI(vertical);
        slowUI = new SlowUI(vertical);
        fastSlowPanel = new JPanel();
        cardLayout = new CardLayout();
        fastSlowPanel.setLayout(cardLayout);
        fastSlowPanel.add(fastUI, "fastUI");
        fastSlowPanel.add(slowUI, "slowUI");
        add(fastSlowPanel, c);
        cardLayout.last(fastSlowPanel);
    }

    private void initComponents() {
    }

    private void hookupListeners() {
        slowType.addActionListener(new PairwiseOptionsUIListeners.TypeListener());
        fastType.addActionListener(new PairwiseOptionsUIListeners.TypeListener());
        addPropertyChangeListener(new PairwiseOptionsUIListeners.PtyListener());

        // slow
        weightMatrixCombo.addActionListener(new PairwiseOptionsUIListeners.MatrixListener());
        gapOpenSpinner.addChangeListener(new PairwiseOptionsUIListeners.GapOpenListener());
        gapExtSpinner.addChangeListener(new PairwiseOptionsUIListeners.GapExtListener());

        // fast
        wordSizeSpinner.addChangeListener(new PairwiseOptionsUIListeners.WordSizeListener());
        windowLengthSpinner.addChangeListener(new PairwiseOptionsUIListeners.WindowLengthListener());
        topDiagsSpinner.addChangeListener(new PairwiseOptionsUIListeners.TopDiagsListener());
        pairGapSpinner.addChangeListener(new PairwiseOptionsUIListeners.PairGapListener());
    }

    public void setQuickTree(Boolean quickTree) {
        if (quickTree) {
            fastType.setSelected(true);
            cardLayout.show(fastSlowPanel, "fastUI");
        } else {
            slowType.setSelected(true);
            cardLayout.show(fastSlowPanel, "slowUI");
        }
    }

    public ClustalwParam getMsaParams() {
        return msaParams;
    }

    public void setMsaParams(ClustalwParam clustalwParam) {
        ClustalwParam old = this.msaParams;
        this.msaParams = clustalwParam;
        firePropertyChange("msaParams", old, this.msaParams);
    }

    protected class FastUI extends JPanel {

        public FastUI(boolean vertical) {
            LayoutManager layout = new GridBagLayout();
            setLayout(layout);

            createComponents(vertical);
        }

        private void createComponents(boolean vertical) {
            GridBagConstraints c;
            int gridy = 0;

            // word size
            c = new GridBagConstraints();
            c.gridy = gridy;
            c.anchor = GridBagConstraints.WEST;
            add(new JLabel("Word Size"), c);

            c = new GridBagConstraints();
            c.gridy = gridy;
            c.anchor = GridBagConstraints.WEST;
            wordSizeSpinner.setModel(new SpinnerNumberModel(1, 1, 99, 1));
            add(wordSizeSpinner, c);

            // window size
            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.gridy = gridy;
            c.anchor = GridBagConstraints.WEST;
            add(new JLabel("Window Size"), c);

            c = new GridBagConstraints();
            c.gridy = vertical ? gridy++ : gridy;
            c.anchor = GridBagConstraints.WEST;
            windowLengthSpinner.setModel(new SpinnerNumberModel(5, 0, 99, 1));
            add(windowLengthSpinner, c);

            // pair gap
            c = new GridBagConstraints();
            c.gridx = vertical ? 0 : GridBagConstraints.RELATIVE;
            c.gridy = gridy;
            c.anchor = GridBagConstraints.WEST;
            add(new JLabel("Pair Gap"), c);

            c = new GridBagConstraints();
            c.gridy = gridy;
            c.anchor = GridBagConstraints.WEST;
            pairGapSpinner.setModel(new SpinnerNumberModel(3, 1, 99, 1));
            add(pairGapSpinner, c);

            // top diags
            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.gridy = gridy;
            c.anchor = GridBagConstraints.WEST;
            add(new JLabel("Top diags"), c);

            c = new GridBagConstraints();
            c.gridy = vertical ? gridy++ : gridy;
            c.anchor = GridBagConstraints.WEST;
            topDiagsSpinner.setModel(new SpinnerNumberModel(5, 1, 99, 1));
            add(topDiagsSpinner, c);

            if (!vertical) {
                c = new GridBagConstraints();
                c.gridx = GridBagConstraints.RELATIVE;
                c.gridy = 0;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 1.0;
                Component filler = Box.createRigidArea(new Dimension(1, 1));
                add(filler, c);
            }
        }
    }

    class SlowUI extends JPanel {

        public SlowUI(boolean vertical) {
            LayoutManager layout = null;
            layout = new GridBagLayout();
            setLayout(layout);

            createComponents(vertical);
        }

        private void createComponents(boolean vertical) {
            GridBagConstraints c = null;
            int gridy = 0;

            c = new GridBagConstraints();
            c.gridy = gridy;
            c.anchor = GridBagConstraints.WEST;
            add(new JLabel("Weight Matrix"), c);

            c = new GridBagConstraints();
            c.gridy = vertical ? gridy++ : gridy;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.WEST;
            UIUtil.setPreferredWidthByPrototype(weightMatrixCombo, "CLUSTALW ");
            add(weightMatrixCombo, c);

            c = new GridBagConstraints();
            c.gridx = vertical ? 0 : GridBagConstraints.RELATIVE;
            c.gridy = gridy;
            c.anchor = GridBagConstraints.WEST;
            add(new JLabel("Gap Open"), c);

            c = new GridBagConstraints();
            c.gridy = vertical ? gridy++ : gridy;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.WEST;
            gapOpenSpinner.setModel(new SpinnerNumberModel(10, 0, 100, 0.1));
            add(gapOpenSpinner, c);

            c = new GridBagConstraints();
            c.gridx = vertical ? 0 : GridBagConstraints.RELATIVE;
            c.gridy = gridy;
            c.anchor = GridBagConstraints.WEST;
            add(new JLabel("Gap Ext"), c);

            c = new GridBagConstraints();
            c.gridy = vertical ? gridy++ : gridy;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.anchor = GridBagConstraints.WEST;
            gapExtSpinner.setModel(new SpinnerNumberModel(0.1, 0, 10, 0.1));
            add(gapExtSpinner, c);

            if (!vertical) {
                c = new GridBagConstraints();
                c.gridx = GridBagConstraints.RELATIVE;
                c.gridy = 0;
                c.fill = GridBagConstraints.HORIZONTAL;
                c.weightx = 1;
                Component filler = Box.createRigidArea(new Dimension(1, 1));
                add(filler, c);
            }
        }
    }
}
