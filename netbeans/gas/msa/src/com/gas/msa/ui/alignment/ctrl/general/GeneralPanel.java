/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.ctrl.general;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.CollapsibleTitlePanel;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.domain.core.msa.MSA;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.jdesktop.swingx.VerticalLayout;

/**
 *
 * @author dq
 */
public class GeneralPanel extends JPanel {

    ConsensusPanel consensusPanel;
    PropertiesPanel propertiesPanel;
    MSA msa;

    public GeneralPanel() {
        hookupListeners();
    }

    protected void initComponentsByType(boolean dna) {
        LayoutManager layout;
        layout = new BorderLayout();
        setLayout(layout);
        TitledPanel titledPanel = new TitledPanel("General");
        titledPanel.getContentPane().setLayout(new VerticalLayout());

        propertiesPanel = new PropertiesPanel();
        CollapsibleTitlePanel collapsibleTitlePanel = new CollapsibleTitlePanel("Common Properties");
        collapsibleTitlePanel.getContentPane().add(propertiesPanel, BorderLayout.CENTER);
        titledPanel.getContentPane().add(collapsibleTitlePanel);

        consensusPanel = new ConsensusPanel();
        collapsibleTitlePanel = new CollapsibleTitlePanel("Consensus");
        collapsibleTitlePanel.getContentPane().add(consensusPanel, BorderLayout.CENTER);
        titledPanel.getContentPane().add(collapsibleTitlePanel);

        if (dna) {
            //TranslationPanel translationPanel = new TranslationPanel();
            //collapsibleTitlePanel = new CollapsibleTitlePanel("Translation");
            //collapsibleTitlePanel.getContentPane().add(translationPanel, BorderLayout.CENTER);
            //titledPanel.getContentPane().add(collapsibleTitlePanel);
        }

        add(titledPanel, BorderLayout.CENTER);
    }

    private void hookupListeners() {
        addPropertyChangeListener(new GeneralPanelListeners.PtyListener());
    }

    public ConsensusPanel getConsensusPanel() {
        return consensusPanel;
    }

    protected PropertiesPanel getPropertiesPanel() {
        return propertiesPanel;
    }

    public ImageIcon getImageIcon() {
        return ImageHelper.createImageIcon(ImageNames.HOME_16);
    }

    public void setMsa(MSA msa) {
        MSA old = this.msa;
        this.msa = msa;
        firePropertyChange("msa", old, this.msa);
    }
}
