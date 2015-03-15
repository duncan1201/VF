/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.tree;

import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.ui.pref.MSAPref;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.lang.ref.WeakReference;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class NodePanel extends JPanel {

    private String selectedNameAttribute;
    private String[] nameAttributeNames;
    private JCheckBox visibleCheck;
    private JCheckBox visibleNodeCheck;
    private WeakReference<JComboBox> attNamesComboRef;
    private WeakReference<JComboBox> shapeComboRef;

    public NodePanel() {
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
        visibleCheck = new JCheckBox();
        add(visibleCheck, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.EAST;
        //add(new JLabel("Shape:"), c);
        
        final String nodeShapeStr = MSAPref.getInstance().getNodeShape();
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        JComboBox shapeCombo = new JComboBox(new String[]{"Square", "Circle"});
        shapeCombo.setSelectedItem(nodeShapeStr);
        //add(shapeCombo, c);
        shapeComboRef = new WeakReference<JComboBox>(shapeCombo);
        
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 1;
        
        boolean nodeShapeVisible = !nodeShapeStr.equalsIgnoreCase("none");
        visibleNodeCheck = new JCheckBox("", nodeShapeVisible);
        //add(visibleNodeCheck, c);
        
    }

    void initValues(MSA msa) {       
        getVisibleCheck().setSelected(msa.getMsaSetting().isNodeLabelDisplay());
    }

    protected JCheckBox getVisibleCheck() {
        return visibleCheck;
    }
    
    protected JCheckBox getVisibleNodeCheck(){
        return visibleNodeCheck;
    }

    protected JComboBox getShapeCombo() {
        return shapeComboRef.get();
    }

    protected JComboBox getAttNamesCombo() {
        return attNamesComboRef.get();
    }

    public String[] getNameAttributeNames() {
        return nameAttributeNames;
    }

    public void setNameAttributeNames(String[] nameAttributeNames) {
        String[] old = this.nameAttributeNames;
        this.nameAttributeNames = nameAttributeNames;
        firePropertyChange("nameAttributeNames", old, this.nameAttributeNames);
    }

    public String getSelectedNameAttribute() {
        return selectedNameAttribute;
    }

    public void setSelectedNameAttribute(String selectedNameAttribute) {
        String old = this.selectedNameAttribute;
        this.selectedNameAttribute = selectedNameAttribute;
        firePropertyChange("selectedNameAttribute", old, this.selectedNameAttribute);
    }

    private void hookupListeners() {
        addPropertyChangeListener(new NodePanelListeners.PtyListener());
        getVisibleCheck().addItemListener(new NodePanelListeners.VisibleListener());
        getVisibleNodeCheck().addItemListener(new NodePanelListeners.VisibleNodeListener());
        getAttNamesCombo().addActionListener(new NodePanelListeners.AttNameComboListener());
        getShapeCombo().addActionListener(new NodePanelListeners.ShapeComboListener());
    }
}
