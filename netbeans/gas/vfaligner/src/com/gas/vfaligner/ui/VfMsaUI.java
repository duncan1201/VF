/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.vfaligner.ui;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.vfmsa.AlignType;
import com.gas.domain.core.msa.vfmsa.IVfMsaUI;
import com.gas.domain.core.msa.vfmsa.SubMatrix;
import com.gas.domain.core.msa.vfmsa.VfMsaParam;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 *
 * @author dq
 */
public class VfMsaUI extends JPanel implements IVfMsaUI{
        
    private JComboBox alignTypeCombo;
    private JCheckBox identicalOnlyCheck;
    private JComboBox matrixCombo;
    private JSpinner gapOpenSpinner;
    private JSpinner gapExtSpinner;
    private Boolean aminoAcids;
    private VfMsaParam vfMsaParam;
    
    public VfMsaUI(){        
        UIUtil.setDefaultBorder(this);
        vfMsaParam = new VfMsaParam();
        createComponents();
        hookupListeners();
    }
    
    void initComponents(boolean aminoAcids){
        String[] names = SubMatrix.getDisplayNames(aminoAcids);
        DefaultComboBoxModel model = new DefaultComboBoxModel(names);
        StringList nameList = new StringList(names);
        String longest = nameList.longest();
        UIUtil.setPreferredWidthByPrototype(matrixCombo, longest + "W");
        matrixCombo.setModel(model);
    }
    
    private void createComponents(){
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        int gridy = 0;
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Pairwise algorithm"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        StringList alignTypes = AlignType.getDisplayNames();
        alignTypeCombo = new JComboBox(alignTypes.toArray(new String[alignTypes.size()]));
        UIUtil.setPreferredWidthByPrototype(alignTypeCombo, alignTypes.longest() + "A");
        add(alignTypeCombo, c);        
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Substitution Matrix"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        matrixCombo = new JComboBox();
        add(matrixCombo, c);
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Gap open penalty"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        gapOpenSpinner = new JSpinner();
        UIUtil.setPreferredWidthByPrototype(gapOpenSpinner, 100);
        gapOpenSpinner.setModel(new SpinnerNumberModel(vfMsaParam.getOpenPenalty(), 0, Integer.MAX_VALUE, 1));
        add(gapOpenSpinner, c);
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.EAST;
        add(new JLabel("Gap ext. penalty"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        gapExtSpinner = new JSpinner();
        gapExtSpinner.setModel(new SpinnerNumberModel(vfMsaParam.getExtPenalty(),0,Integer.MAX_VALUE,1));
        UIUtil.setPreferredWidthByPrototype(gapExtSpinner, 100);
        add(gapExtSpinner, c);    
        
        c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        identicalOnlyCheck = new JCheckBox("Count identical matches only");
        add(identicalOnlyCheck, c);
        
        c = new GridBagConstraints();
        c.gridwidth = 2;
        c.gridy = gridy;
        c.fill = GridBagConstraints.VERTICAL;        
        c.weighty = 1;
        Component comp = Box.createRigidArea(new Dimension(1, 1));
        add(comp, c);
    }
    
    private void hookupListeners(){
        addPropertyChangeListener(new VfMsaUIListeners.PtyListner());
    }

    public JComboBox getAlignTypeCombo() {
        return alignTypeCombo;
    }

    public JCheckBox getIdenticalOnlyCheck() {
        return identicalOnlyCheck;
    }

    public JComboBox getMatrixCombo() {
        return matrixCombo;
    }

    public JSpinner getGapOpenSpinner() {
        return gapOpenSpinner;
    }

    public JSpinner getGapExtSpinner() {
        return gapExtSpinner;
    }
    

    @Override
    public void setAminoAcids(Boolean aminoAcids) {
        Boolean old = this.aminoAcids;
        this.aminoAcids = aminoAcids;
        firePropertyChange("aminoAcids", old, this.aminoAcids);
    }
    
    @Override
    public void setVfMsaParam(VfMsaParam param){
        VfMsaParam old = this.vfMsaParam ;
        this.vfMsaParam = param;
        firePropertyChange("vfMsaParam", old, this.vfMsaParam);
    }
        
    @Override
    public VfMsaParam getVfMsaParam(){
        Number gapExt = (Number)gapExtSpinner.getValue();
        vfMsaParam.setExtPenalty(gapExt.intValue());
        
        String matrixName = (String)matrixCombo.getSelectedItem();
        SubMatrix matrix = SubMatrix.getByDisplayName(matrixName);        
        vfMsaParam.setMatrix(matrix);
        
        Number gapOpen = (Number)gapOpenSpinner.getValue();
        vfMsaParam.setOpenPenalty(gapOpen.intValue());
        
        String alignTypeDisplayName = (String)alignTypeCombo.getSelectedItem();
        AlignType alignType = AlignType.getByDisplayName(alignTypeDisplayName);
        boolean identicalOnly = identicalOnlyCheck.isSelected();
        vfMsaParam.setIdenticalOnly(identicalOnly);        
        vfMsaParam.setAlignTypeEnum(alignType);
               
        return vfMsaParam;
    }
}
