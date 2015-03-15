/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.contigs;

import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.CollapsibleTitlePanel;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.tasm.Condig;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author dq
 */
public class ContigsPanel extends JPanel {

    ContigsTable contigsTable;
    VariantsPanel variantsPanel;
    CollapsibleTitlePanel contigsPane;
    CollapsibleTitlePanel variantsPane;
    VariantMapMdl variantMapMdl;
    private List<Condig> condigs;

    public ContigsPanel() {
        initComponents();        
        addPropertyChangeListener(new ContigsPanelListeners.PtyListener());
    }
    
    private void initComponents(){
        setLayout(new BorderLayout());
        TitledPanel titledPanel = new TitledPanel("General");
        JPanel tmp = createContentPanel();
        titledPanel.getContentPane().setLayout(new BorderLayout());
        titledPanel.getContentPane().add(tmp, BorderLayout.CENTER);
        add(titledPanel, BorderLayout.CENTER);
    }
    
    private JPanel createContentPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;               

        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        contigsTable = getContigsTable();
        final int w = contigsTable.getPreferredSize().width;
        final int h = contigsTable.getRowHeight() * 7;
        contigsTable.setPreferredScrollableViewportSize(new Dimension(w, h));        
        JScrollPane scrollPane = new JScrollPane(contigsTable);
        
        scrollPane.setBorder(BorderFactory.createEmptyBorder(3, 4, 3, 4));
        contigsTable.setFillsViewportHeight(true);
                
        contigsPane = new CollapsibleTitlePanel("Contigs");
        contigsPane.getContentPane().setLayout(new BorderLayout());
        contigsPane.getContentPane().add(scrollPane, BorderLayout.CENTER);
        UIUtil.setPreferredHeight(contigsPane, h);
        ret.add(contigsPane, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0.9;
        c.weighty = 1;
        variantsPanel = new VariantsPanel();
        variantsPane = new CollapsibleTitlePanel("Variants");        
        variantsPane.getContentPane().setLayout(new BorderLayout());
        variantsPane.getContentPane().add(variantsPanel, BorderLayout.CENTER);
        ret.add(variantsPane, c);  
        
        return ret;
    }

    public VariantsPanel getVariantsPanel() {
        return variantsPanel;
    }
    
    public final ContigsTable getContigsTable() {
        if (contigsTable == null) {
            contigsTable = new ContigsTable(new ContigsTableModel());            
        }
        return contigsTable;
    }

    public void setCondigs(List<Condig> condigs) {
        List<Condig> old = this.condigs;
        this.condigs = condigs;
        firePropertyChange("condigs", old, this.condigs);
    }


    public void setVariantMapMdl(VariantMapMdl mdl) {
        VariantMapMdl old = this.variantMapMdl;
        this.variantMapMdl = mdl;
        firePropertyChange("variantMapMdl", old, this.variantMapMdl);
    }
    
    public ImageIcon getImageIcon(){
        return ImageHelper.createImageIcon(ImageNames.HOME_16);
    }
}
