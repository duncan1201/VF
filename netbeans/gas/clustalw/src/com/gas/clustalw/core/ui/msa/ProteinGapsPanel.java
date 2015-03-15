/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.clustalw.core.ui.msa;

import com.gas.common.ui.misc.RichSeparator;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
class ProteinGapsPanel extends JPanel {

    JCheckBox residueSpecificCheck;    
    JCheckBox hydrophilicPenaltyCheck;
    JTextField hydrophilicResidues;
    JCheckBox ignoreEndGapsCheck;
    
    ProteinGapsPanel(boolean vertical) {
        initComponents(vertical);
        hookupListeners();
    }
    
    private void hookupListeners(){
        residueSpecificCheck.addItemListener(new ProteinGapsPanelListeners.CheckBoxListener());
        hydrophilicPenaltyCheck.addItemListener(new ProteinGapsPanelListeners.CheckBoxListener());
        ignoreEndGapsCheck.addItemListener(new ProteinGapsPanelListeners.CheckBoxListener());
        hydrophilicResidues.getDocument().addDocumentListener(new ProteinGapsPanelListeners.DocListener(this));
    }
    
    private void initComponents(boolean vertical){
        setLayout(new GridBagLayout());
        GridBagConstraints c;

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        RichSeparator rs = new RichSeparator(new JLabel("<html><b>Protein Gaps Penalty</b></html>"));
        add(rs, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        JPanel contents = createComponents(vertical);
        add(contents, c);        
    }
    
    void populateUI(ClustalwParam param){
        residueSpecificCheck.setSelected(param.getMultiple().isResidueSpecificPenalty());
        hydrophilicPenaltyCheck.setSelected(param.getMultiple().isHydrophilicPenalty());
        hydrophilicResidues.setText(param.getMultiple().getHydrophilicResidue());
        ignoreEndGapsCheck.setSelected(param.getMultiple().getIgnoreEndGaps());
    }

    private JPanel createComponents(boolean vertical) {
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;

        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Residue Specific"), c);

        c = new GridBagConstraints();
        c.gridy = vertical ? gridy++ : gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        residueSpecificCheck = new JCheckBox();
        residueSpecificCheck.setActionCommand("residueSpecific");
        residueSpecificCheck.setHorizontalTextPosition(SwingConstants.LEFT);
        ret.add(residueSpecificCheck, c);

        c = new GridBagConstraints();
        c.gridx = vertical ? 0 : GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Hydrophilic Gap"), c);

        c = new GridBagConstraints();
        c.gridy = vertical ? gridy++ : gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        hydrophilicPenaltyCheck = new JCheckBox();
        hydrophilicPenaltyCheck.setActionCommand("hydrophilic");
        hydrophilicPenaltyCheck.setHorizontalTextPosition(SwingConstants.LEFT);
        ret.add(hydrophilicPenaltyCheck, c);

        c = new GridBagConstraints();
        c.gridx = vertical? 0 : GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Hydrophilic Residues"), c);
        
        c = new GridBagConstraints();      
        c.gridy = vertical? gridy++ : gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        hydrophilicResidues = new JTextField();
        UIUtil.setPreferredWidthByPrototype(hydrophilicResidues, "GPSNDQEKQ12");
        ret.add(hydrophilicResidues, c);
        
        // ignore end gaps
        c = new GridBagConstraints();
        c.gridx = vertical ? 0 : GridBagConstraints.RELATIVE;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Ignore End Gaps"), c);

        c = new GridBagConstraints();
        c.gridy = vertical ? gridy++ : gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        ignoreEndGapsCheck = new JCheckBox();
        ignoreEndGapsCheck.setActionCommand("ignoreEndGaps");
        ignoreEndGapsCheck.setHorizontalTextPosition(SwingConstants.LEFT);
        ret.add(ignoreEndGapsCheck, c);        
        
        if(!vertical){
            c = new GridBagConstraints();
            c.gridx = GridBagConstraints.RELATIVE;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            Component comp = Box.createRigidArea(new Dimension(1,1));
            ret.add(comp, c);
        }
        
        return ret;
    }
}
