/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.general;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.CollapsibleTitlePanel;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.ui.translation.TranslationPanel;
import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import org.jdesktop.swingx.VerticalLayout;

/**
 *
 * @author dq
 */
public class GeneralPanel extends JPanel {

    private PropertiesPanel propertiesPanel;
    private FontPanel fontPanel;
    private Boolean forProtein;
    private TranslationPanel translationPanel;
    protected CollapsibleTitlePanel translationCollapsiblePanel;
    private TitledPanel titledPanel;

    public GeneralPanel() {
        setToolTipText("Home");
        setBackground(CNST.BG);
        LayoutManager layout = new BorderLayout();
        setLayout(layout);
        
        titledPanel = new TitledPanel("General");
        add(titledPanel, BorderLayout.CENTER);
        layout = new VerticalLayout();
        titledPanel.getContentPane().setLayout(layout);
                
        propertiesPanel = new PropertiesPanel();
        CollapsibleTitlePanel collapsibleTitlePanel = new CollapsibleTitlePanel("Common Properties");
        collapsibleTitlePanel.getContentPane().add(propertiesPanel, BorderLayout.CENTER);
        titledPanel.getContentPane().add(collapsibleTitlePanel);

        collapsibleTitlePanel = new CollapsibleTitlePanel("Font Sizes");
        fontPanel = new FontPanel();
        collapsibleTitlePanel.getContentPane().add(fontPanel, BorderLayout.CENTER);
        titledPanel.getContentPane().add(collapsibleTitlePanel);

        translationCollapsiblePanel = new CollapsibleTitlePanel("Translation");
        translationPanel = new TranslationPanel();
        translationCollapsiblePanel.getContentPane().add(translationPanel, BorderLayout.CENTER);
        titledPanel.getContentPane().add(translationCollapsiblePanel);        
        
        UIUtil.setBackground(titledPanel.getContentPane(), CNST.BG, JPanel.class);
        addPropertyChangeListener(new GeneralPanelListeners.PtyChangeListener());
    }
    
    void removeTranslationPanel(){
        titledPanel.getContentPane().remove(translationCollapsiblePanel);
    }
    
    public String getChildName() {
        return "General";
    }
    
    public void setDoubleForm(boolean doubleForm){
        propertiesPanel.setDoubleStranded(doubleForm);
    }
    
    public void setBaseNumber(boolean baseNumber){
        propertiesPanel.setBaseNumber(baseNumber);
    }
    
    public void setMinimap(boolean minimap){
        propertiesPanel.setMinimap(minimap);
    }
    
    public void setTranslationResults(Set<TranslationResult> translationResults){
        translationPanel.setTranslationResults(translationResults);
    }

    protected TranslationPanel getTranslationPanel() {
        return translationPanel;
    }
    
    public FontPanel getFontPanel(){
        return fontPanel;
    }

    public Boolean getForProtein() {
        return forProtein;
    }

    public void setForProtein(Boolean forProtein) {
        Boolean old = this.forProtein;
        this.forProtein = forProtein;
        firePropertyChange("forProtein", old, this.forProtein);
    }

    PropertiesPanel getPropertiesPanel() {
        return propertiesPanel;
    }

    public ImageIcon getImageIcon() {
        return ImageHelper.createImageIcon(ImageNames.HOME_16);
    }  
}
