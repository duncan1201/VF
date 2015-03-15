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
class MultipleOptionsUI extends JPanel {

    private ClustalwParam msaParams;
    protected JComboBox weightMatrixCombo = new JComboBox();
    protected JSpinner gapOpenSpinner = new JSpinner();
    protected JSpinner gapExtSpinner = new JSpinner();
    protected JComboBox iterationCombo = new JComboBox();
    JPanel contentsPanel;
    // internal layout use only
    boolean vertical;
    int gridy = 0;
    // dna specific
    protected JSpinner transitionsWeightSpinner = new JSpinner();

    MultipleOptionsUI(boolean vertical) {
        this.vertical = vertical;
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        createComponents();
        hookupListeners();
    }

    ClustalwParam getMsaParams() {
        return msaParams;
    }

    public void setMsaParams(ClustalwParam msaParams) {
        ClustalwParam old = this.msaParams;
        this.msaParams = msaParams;
        firePropertyChange("msaParams", old, this.msaParams);
    }

    private void hookupListeners() {
        addPropertyChangeListener(new MultipleOptionsUIListeners.PtyListener());
        iterationCombo.addActionListener(new MultipleOptionsUIListeners.IterationListener());
        weightMatrixCombo.addActionListener(new MultipleOptionsUIListeners.MatrixListener());
        gapOpenSpinner.addChangeListener(new MultipleOptionsUIListeners.GapOpenListener());
        gapExtSpinner.addChangeListener(new MultipleOptionsUIListeners.GapExtListener());
    }

    protected void hookupListenersDynamically() {
        // dna
        if (transitionsWeightSpinner != null) {
            transitionsWeightSpinner.addChangeListener(new MultipleOptionsUIListeners.TransitionsWeightListener());
        }        
    }

    private void createComponents() {
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        RichSeparator rs = new RichSeparator(new JLabel("<html><b>Multiple Sequence Alignment</b></html>"));
        add(rs, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        contentsPanel = createContentsPanel();
        add(contentsPanel, c);
    }

    private JPanel createContentsPanel() {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;


        // weight matrix
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Weight Matrix"), c);

        c = new GridBagConstraints();
        c.gridy = vertical ? gridy++ : gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        UIUtil.setPreferredWidthByPrototype(weightMatrixCombo, "CLUSTALW ");
        ret.add(weightMatrixCombo, c);

        // gap open
        c = new GridBagConstraints();
        c.gridx = vertical ? 0 : GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Gap Open"), c);

        c = new GridBagConstraints();
        c.gridy = vertical ? gridy++ : gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        gapOpenSpinner.setModel(new SpinnerNumberModel(10, 1, 100, 0.1));
        ret.add(gapOpenSpinner, c);

        // gap ext
        c = new GridBagConstraints();
        c.gridx = vertical ? 0 : GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Gap Ext"), c);

        c = new GridBagConstraints();
        c.gridy = vertical ? gridy++ : gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        gapExtSpinner.setModel(new SpinnerNumberModel(0.3, 0.05, 100, 0.05));
        ret.add(gapExtSpinner, c);

        // iteration        
        c = new GridBagConstraints();
        c.gridx = vertical ? 0 : GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Iteration"), c);

        c = new GridBagConstraints();
        c.gridy = vertical ? gridy++ : gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        ret.add(iterationCombo, c);

        if (!vertical) {
            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            Component filler = Box.createRigidArea(new Dimension(1, 1));
            ret.add(filler, c);
        }

        return ret;
    }

    protected void createComponentsDynamically(boolean dna) {
        GridBagConstraints c = null;

        if (dna) {
            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = vertical ? gridy : 2;
            c.anchor = GridBagConstraints.WEST;
            contentsPanel.add(new JLabel("Trans. Weight"), c);

            c = new GridBagConstraints();
            c.gridy = vertical ? gridy++ : 2;
            c.fill = GridBagConstraints.HORIZONTAL;
            transitionsWeightSpinner.setModel(new SpinnerNumberModel(0.5, 0, 1, 0.1));
            contentsPanel.add(transitionsWeightSpinner, c);            
        }

    }
}
