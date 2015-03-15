/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.contigs;

import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.RowFilter;
import javax.swing.RowFilter.Entry;
import javax.swing.SwingUtilities;

/**
 *
 * @author dq
 */
public class VariantsPanel extends JPanel {

    protected VariantsTable variantsTable;
    protected JCheckBox ambiguityCheckbox;
    protected JCheckBox gapCheckbox;
    protected JCheckBox transitionCheckbox;
    protected JCheckBox transversionCheckbox;
    protected JCheckBox insertionCheckbox;
    private JCheckBox otherCheckbox;
    private JButton nextVariantBtn;
    private JButton prevVariantBtn;
    protected VariantMapMdl variantMapMdl;
    protected boolean includeGaps = true;
    protected boolean includeAmbiguitis = true;
    protected boolean includeTransitions = true;
    protected boolean includeTransversions = true;
    protected boolean includeInsertions = true;
    protected boolean includeOther = true;
    protected boolean adjustingCheckbox = true;    

    public VariantsPanel() {
        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;
        int gridy = 0;

        gapCheckbox = new JCheckBox("Gap");
        ambiguityCheckbox = new JCheckBox("Ambiguity");
        transitionCheckbox = new JCheckBox("Transition");
        transversionCheckbox = new JCheckBox("Transversion");
        insertionCheckbox = new JCheckBox("Insertion");

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        add(ambiguityCheckbox, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        add(transitionCheckbox, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        add(transversionCheckbox, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        add(gapCheckbox, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        add(insertionCheckbox, c);        

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy++;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0f;
        c.weighty = 1.0f;
        c.gridwidth = 2;
        getVariantsTable();
        int w = variantsTable.getPreferredSize().width;
        int h = variantsTable.getRowHeight() * 5;
        variantsTable.setPreferredScrollableViewportSize(new Dimension(w, h));
        JScrollPane scrollPane = new JScrollPane(variantsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));
        add(scrollPane, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        prevVariantBtn = new JButton("Previous");
        add(prevVariantBtn, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        nextVariantBtn = new JButton("Next");
        add(nextVariantBtn, c);

        hookupListeners();
    }

    public void setIncludeAmbiguitis(boolean includeAmbiguitis) {
        boolean old = this.includeAmbiguitis;
        this.includeAmbiguitis = includeAmbiguitis;
        firePropertyChange("includeAmbiguitis", old, this.includeAmbiguitis);
    }

    public void setIncludeGaps(boolean includeGaps) {
        boolean old = this.includeGaps;
        this.includeGaps = includeGaps;
        firePropertyChange("includeGaps", old, this.includeGaps);
    }
    
    public void setIncludeInsertion(boolean includeInsertions) {
        boolean old = this.includeInsertions;
        this.includeInsertions = includeInsertions;
        firePropertyChange("includeInsertions", old, this.includeInsertions);
    }    

    public void setIncludeTransitions(boolean includeTransitions) {
        boolean old = this.includeTransitions;
        this.includeTransitions = includeTransitions;
        firePropertyChange("includeTransitions", old, this.includeTransitions);
    }

    public void setIncludeTransversions(boolean includeTransversions) {
        boolean old = this.includeTransversions;
        this.includeTransversions = includeTransversions;
        firePropertyChange("includeTransversions", old, this.includeTransversions);
    }

    private void hookupListeners() {
        insertionCheckbox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                adjustingCheckbox = true;
                setIncludeInsertion(state == ItemEvent.SELECTED);
                adjustingCheckbox = false;
            }
        });
        gapCheckbox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                adjustingCheckbox = true;
                setIncludeGaps(state == ItemEvent.SELECTED);
                adjustingCheckbox = false;
            }
        });
        ambiguityCheckbox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                adjustingCheckbox = true;
                setIncludeAmbiguitis(state == ItemEvent.SELECTED);
                adjustingCheckbox = false;
            }
        });
        transitionCheckbox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                adjustingCheckbox = true;
                setIncludeTransitions(state == ItemEvent.SELECTED);
                adjustingCheckbox = false;
            }
        });
        transversionCheckbox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                adjustingCheckbox = true;
                setIncludeTransversions(state == ItemEvent.SELECTED);
                adjustingCheckbox = false;
            }
        });

        prevVariantBtn.addActionListener(new ActionListener() {

            KeyStroke keyStroke = KeyStroke.getKeyStroke("UP");
            Action action = (Action) getVariantsTable().getActionForKeyStroke(KeyStroke.getKeyStroke("UP"));

            @Override
            public void actionPerformed(ActionEvent e) {
                KeyEvent ke = UIUtil.toKeyEvent(getVariantsTable(), keyStroke);
                SwingUtilities.notifyAction(action, keyStroke, ke, getVariantsTable(), keyStroke.getModifiers());
            }
        });

        nextVariantBtn.addActionListener(new ActionListener() {

            Action action = (Action) getVariantsTable().getActionForKeyStroke(KeyStroke.getKeyStroke("DOWN"));
            KeyStroke keyStroke = KeyStroke.getKeyStroke("DOWN");

            @Override
            public void actionPerformed(ActionEvent e) {
                KeyEvent ke = UIUtil.toKeyEvent(getVariantsTable(), keyStroke);
                SwingUtilities.notifyAction(action, keyStroke, ke, getVariantsTable(), keyStroke.getModifiers());
            }
        });

        addPropertyChangeListener(new VariantsPanelListeners.PtyChangeListener(this));
    }

    protected RowFilter createRowFilter() {
        RowFilter<VariantsTableModel, Integer> ret = new RowFilter<VariantsTableModel, Integer>() {

            @Override
            public boolean include(Entry<? extends VariantsTableModel, ? extends Integer> entry) {
                boolean ret = false;
                VariantsTableModel model = entry.getModel();
                int id = entry.getIdentifier();
                VariantsTableModel.Row row = model.getRow(id);
                String base = row.getBase();
          
                String[] splits = base.split("/");
                if (splits.length == 2) {
                    if (includeGaps && splits[1].equalsIgnoreCase("-")) {
                        ret = true;
                    } else if (includeAmbiguitis && BioUtil.isAnyNucleotide(splits[0], splits[1])) {
                        ret = true;
                    } else if (includeTransitions && BioUtil.isTransition(splits[0], splits[1])) {
                        ret = true;
                    } else if (includeTransversions && BioUtil.isTransverion(splits[0], splits[1])) {
                        ret = true;
                    } else if (includeInsertions && BioUtil.isInsertion(splits[0], splits[1])){
                        ret = true;
                    }
                }
                return ret;
            }
        };
        return ret;
    }

    public VariantsTable getVariantsTable() {
        if (variantsTable == null) {
            variantsTable = new VariantsTable();
            variantsTable.setFillsViewportHeight(true);
        }
        return variantsTable;
    }

    public void setVariantMapMdl(VariantMapMdl variantMapMdl) {
        VariantMapMdl old = this.variantMapMdl;
        this.variantMapMdl = variantMapMdl;
        firePropertyChange("variantMapMdl", old, this.variantMapMdl);
    }
}
