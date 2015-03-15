/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.ref;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Reference;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author dunqiang
 */
public class ReferencesPanel extends JPanel {

    private Wrapper wrapper;
    
    public ReferencesPanel(){
        super(new BorderLayout());
        setToolTipText("References");
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        TitledPanel titledPanel = new TitledPanel(getChildName());
        
        titledPanel.getContentPane().setLayout(new BorderLayout());
        
        titledPanel.getContentPane().add(scrollPane, BorderLayout.CENTER);
        
        wrapper = new Wrapper();
        
        scrollPane.setViewportView(wrapper);
        
        add(titledPanel, BorderLayout.CENTER);
    }
    
    public void setReference(Set<Reference> refs){
        wrapper.setReferences(refs);
    }    
    
    public String getChildName() {
        return "References";
    }

    
    public ImageIcon getImageIcon() {
        return ImageHelper.createImageIcon(ImageNames.PUBLICATION_16);
    }
    
}
