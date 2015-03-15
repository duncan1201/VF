/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.UserInput;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
class PenaltyWeightsPanel extends JPanel {

    private PrimerWeightsPanel primerWeightsPanel;
    private PrimerPairWeightsPanel primerPairWeightsPanel;
    private InternalOligoWeightsPanel internalOligoWeightsPanel;
    private UserInput userInput;
    private boolean internalOligo;
    private boolean leftPrimer;
    private boolean rightPrimer;

    PenaltyWeightsPanel(boolean leftPrimer, boolean rightPrimer, boolean internalOligo) {
        this.rightPrimer = rightPrimer;
        this.leftPrimer = leftPrimer;
        this.internalOligo = internalOligo;
        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        final Insets insets = UIUtil.getDefaultInsets();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c;

        int gridx = 0;

        if (leftPrimer || rightPrimer) {
            c = new GridBagConstraints();
            c.gridx = gridx++;
            c.gridy = 0;
            c.fill = GridBagConstraints.VERTICAL;
            c.weighty = 1.0;
            primerWeightsPanel = new PrimerWeightsPanel();

            add(primerWeightsPanel, c);
        }

        if (leftPrimer || rightPrimer) {
            c = new GridBagConstraints();
            c.gridx = gridx++;
            c.gridy = 0;
            c.fill = GridBagConstraints.VERTICAL;
            c.weighty = 1.0;
            primerPairWeightsPanel = new PrimerPairWeightsPanel();
            add(primerPairWeightsPanel, c);
        }

        if (internalOligo) {
            c = new GridBagConstraints();
            c.gridx = gridx++;
            c.gridy = 0;
            c.fill = GridBagConstraints.VERTICAL;
            c.weighty = 1.0;
            internalOligoWeightsPanel = new InternalOligoWeightsPanel();
            internalOligoWeightsPanel.setChildrenEnabled(internalOligo);
            add(internalOligoWeightsPanel, c);
        }
    }

    private void hookupListeners() {

        addPropertyChangeListener(new PenaltyWeightsPanelListeners.PtyListener());
    }

    public void setUserInput(UserInput userInput) {
        UserInput old = this.userInput;
        this.userInput = userInput;
        firePropertyChange("userInput", old, this.userInput);
    }
    
    public UserInput updateUserInputFromUI(){
        if(leftPrimer || rightPrimer){
            getPrimerWeightsPanel().updateUserInputFromUI();
            getPrimerPairWeightsPanel().updateUserInputFromUI();
        }
        if(internalOligo){
            getInternalOligoWeightsPanel().updateUserInputFromUI();
        }
        return userInput;
    }

    public UserInput getUserInput() {
        return userInput;
    }
    
    protected void populateUI() {
        if (this.userInput == null) {
            return;
        }
        if (internalOligo) {
            getInternalOligoWeightsPanel().populateUI();
        }
        if (leftPrimer || rightPrimer) {
            getPrimerPairWeightsPanel().populateUI();
            getPrimerWeightsPanel().populateUI();
        }
    }

    public InternalOligoWeightsPanel getInternalOligoWeightsPanel() {
        return internalOligoWeightsPanel;
    }

    public PrimerPairWeightsPanel getPrimerPairWeightsPanel() {
        return primerPairWeightsPanel;
    }

    public PrimerWeightsPanel getPrimerWeightsPanel() {
        return primerWeightsPanel;
    }
}
