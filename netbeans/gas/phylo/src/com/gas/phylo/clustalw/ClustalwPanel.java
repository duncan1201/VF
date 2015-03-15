/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.phylo.clustalw;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.clustalw.ClustalTreeParam;
import com.gas.domain.core.msa.clustalw.GeneralParam;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.lang.ref.WeakReference;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class ClustalwPanel extends JPanel{
    
    WeakReference<JComboBox> clusteringComboRef;
    WeakReference<JCheckBox> noGapsBoxRef;
    WeakReference<JCheckBox> distCorrectBoxRef;
    
    public ClustalwPanel(){
        setLayout(new GridBagLayout());
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        GridBagConstraints c;
        int gridy = 0;
        
        c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Clustering Method"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        JComboBox clusteringCombo = new JComboBox();
        clusteringComboRef = new WeakReference<JComboBox>(clusteringCombo);
        StringList descs = GeneralParam.CLUSTERING.getAllDescs();        
        UIUtil.setPreferredWidthByPrototype(clusteringCombo, descs.longest() + "A");
        clusteringCombo.setModel(new DefaultComboBoxModel(descs.toArray(new String[descs.size()])));
        add(clusteringCombo, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Distance Correction"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        JCheckBox distCorrectBox = new JCheckBox();
        distCorrectBoxRef = new WeakReference<JCheckBox>(distCorrectBox);
        add(distCorrectBox, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;        
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Exclude Gaps"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy++;    
        c.anchor = GridBagConstraints.WEST;
        JCheckBox noGapsBox = new JCheckBox();
        noGapsBoxRef = new WeakReference<JCheckBox>(noGapsBox);
        add(noGapsBox, c);        
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = gridy;          
        c.anchor = GridBagConstraints.EAST;
        //add(new JLabel("% identity matrix"), c);      
        
        c = new GridBagConstraints();
        c.gridy = gridy++;          
        c.anchor = GridBagConstraints.WEST;
        JCheckBox pimBox = new JCheckBox();
        //add(pimBox, c);          
    }
    
    public void populateUI(MSA msa){
        ClustalTreeParam param = msa.getClustalTreeParam();
        clusteringComboRef.get().setSelectedItem(param.getClusteringEnum().getDesc());
        noGapsBoxRef.get().setSelected(param.isTossGaps());
        distCorrectBoxRef.get().setSelected(param.isKimura());
    }
}
