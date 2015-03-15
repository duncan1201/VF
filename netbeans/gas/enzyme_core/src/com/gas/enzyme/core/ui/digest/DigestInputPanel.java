/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui.digest;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.util.Collection;
import javax.swing.*;

/**
 *
 * @author dq
 */
public class DigestInputPanel extends JPanel {

    protected JRadioButton useSelectedSiteBtn;
    protected JRadioButton useAllSitesBtn;
    protected JRadioButton useSeparatelyBtn;
    protected JRadioButton useAllAtOnceBtn;
    protected boolean useAllSites;
    protected boolean useSelectedSite;
    protected boolean useAllAtOnce;
    protected boolean useSeparatly;
    protected String selectedEnzyme;
    protected JComboBox namesCombo;
    private String[] names;

    public DigestInputPanel() {
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        JPanel panel = new JPanel();
        add(panel, c);
        initCutPositionsPanel(panel);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        panel = new JPanel();
        add(panel, c);
        initEnzymeUsagePanel(panel);

        hookupListeners();

        setUseAllSites(true);
        setUseAllAtOnce(true);
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        String[] old = this.names;
        this.names = names;
        firePropertyChange("names", old, this.names);
    }

    public void setNames(Collection<String> names) {
        String[] nameArr = names.toArray(new String[names.size()]);
        setNames(nameArr);
    }

    public void setSelectedEnzyme(String selectedEnzyme) {
        String old = this.selectedEnzyme;
        this.selectedEnzyme = selectedEnzyme;
        firePropertyChange("selectedEnzyme", old, this.selectedEnzyme);
    }

    public String getSelectedEnzyme() {
        return selectedEnzyme;
    }

    private void hookupListeners() {
        addPropertyChangeListener(new DigestInputPanelListeners.PtyChangeListener(this));
        useSelectedSiteBtn.addActionListener(new DigestInputPanelListeners.BtnListener(this));
        useAllSitesBtn.addActionListener(new DigestInputPanelListeners.BtnListener(this));
        useSeparatelyBtn.addActionListener(new DigestInputPanelListeners.BtnListener(this));
        useAllAtOnceBtn.addActionListener(new DigestInputPanelListeners.BtnListener(this));
        namesCombo.addActionListener(new DigestInputPanelListeners.ComboListener(this));
    }

    public boolean isUseAllAtOnce() {
        return useAllAtOnce;
    }

    public void setUseAllAtOnce(boolean useAllAtOnce) {
        boolean old = this.useAllAtOnce;
        this.useAllAtOnce = useAllAtOnce;
        firePropertyChange("useAllAtOnce", old, this.useAllAtOnce);
    }

    public boolean isUseSeperatly() {
        return useSeparatly;
    }

    public void setUseSeparatly(boolean useSeparatly) {
        boolean old = this.useSeparatly;
        this.useSeparatly = useSeparatly;
        firePropertyChange("useSeparatly", old, this.useSeparatly);
    }

    public boolean isUseAllSites() {
        return useAllSites;
    }

    public void setUseAllSites(boolean useAllSites) {
        boolean old = this.useAllSites;
        this.useAllSites = useAllSites;
        firePropertyChange("useAllSites", old, this.useAllSites);
    }

    public boolean isUseSelectedSite() {
        return useSelectedSite;
    }

    public void setUseSelectedSite(boolean useSelectedSite) {
        boolean old = this.useSelectedSite;
        this.useSelectedSite = useSelectedSite;
        firePropertyChange("useSelectedSite", old, this.useSelectedSite);
    }

    private void initEnzymeUsagePanel(JPanel parent) {
        ButtonGroup btnGroup = new ButtonGroup();

        parent.setBorder(BorderFactory.createTitledBorder("Restriction enzyme usage"));
        parent.setLayout(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        useSeparatelyBtn = new JRadioButton("Use each enzyme separately");
        parent.add(useSeparatelyBtn, c);
        btnGroup.add(useSeparatelyBtn);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        useAllAtOnceBtn = new JRadioButton("Use all enzymes at once");
        parent.add(useAllAtOnceBtn, c);
        btnGroup.add(useAllAtOnceBtn);
    }

    private void initCutPositionsPanel(JPanel parent) {
        ButtonGroup btnGroup = new ButtonGroup();

        parent.setBorder(BorderFactory.createTitledBorder("Cut positions"));
        parent.setLayout(new GridBagLayout());
        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        useAllSitesBtn = new JRadioButton("All annotated restriction enzymes");
        parent.add(useAllSitesBtn, c);
        btnGroup.add(useAllSitesBtn);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        useSelectedSiteBtn = new JRadioButton("Selected annotated restriction enzyme:");
        parent.add(useSelectedSiteBtn, c);
        btnGroup.add(useSelectedSiteBtn);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 5, 0, 0);
        c.anchor = GridBagConstraints.CENTER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.4;
        namesCombo = new JComboBox();
        parent.add(namesCombo, c);
        
        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0.6;
        Component filler = Box.createRigidArea(new Dimension(1,1));
        parent.add(filler, c);        
    }
}
