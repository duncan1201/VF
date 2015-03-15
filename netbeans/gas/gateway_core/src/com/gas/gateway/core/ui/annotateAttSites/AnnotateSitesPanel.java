/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.annotateAttSites;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.awt.LayoutManager;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 *
 * @author dq
 */
public class AnnotateSitesPanel extends JPanel {

    private JRadioButton pcrProducts;
    private JRadioButton donorVectors;
    private JRadioButton entryVectors;
    private JRadioButton expVectors;

    public enum SEARCH_TYPE {
        
        attBSites("attB sites"), 
        attPSites("attP sites"), 
        attLSites("attL sites"), 
        attRSites("attR sites");
        
        private String str;
        SEARCH_TYPE(String str){
            this.str = str;
        }
        
        @Override
        public String toString(){
            return str;
        }
    };
    private SEARCH_TYPE searchType;

    public AnnotateSitesPanel() {
        super();
        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);

        GridBagConstraints c = null;

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        add(new JLabel("What type of sites do you want to annotate?"), c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0,5,0,5);
        JPanel panel = createAttSitesPanel();
        add(panel, c);

        hookupListeners();

        pcrProducts.setSelected(true);
        searchType = SEARCH_TYPE.attBSites;
    }

    private JPanel createAttSitesPanel() {
        JPanel ret = new JPanel();
        GridBagConstraints c = new GridBagConstraints();
        pcrProducts = new JRadioButton("attB sites");
        ret.add(pcrProducts, c);

        donorVectors = new JRadioButton("attP sites");
        ret.add(donorVectors, c);

        entryVectors = new JRadioButton("attL sites");
        ret.add(entryVectors, c);

        expVectors = new JRadioButton("attR sites");
        ret.add(expVectors, c);

        ButtonGroup group = new ButtonGroup();
        group.add(pcrProducts);
        group.add(donorVectors);
        group.add(entryVectors);
        group.add(expVectors);
        return ret;
    }

    private void hookupListeners() {
        pcrProducts.addActionListener(new AnnotateSitesPanelListener.PcrProductsListener());
        donorVectors.addActionListener(new AnnotateSitesPanelListener.DonorVectorListener());
        entryVectors.addActionListener(new AnnotateSitesPanelListener.EntryVectorListener());
        expVectors.addActionListener(new AnnotateSitesPanelListener.ExpVectorListener());
    }

    public SEARCH_TYPE getSearchType() {
        return searchType;
    }

    public void setSearchType(SEARCH_TYPE searchType) {
        this.searchType = searchType;
    }
}
