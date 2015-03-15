/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel;

import com.gas.common.ui.tabbedpane.JTabbedPaneFactory;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.ui.predict.api.IPredictPanel;
import com.gas.domain.ui.predict.api.IPredictPanelCreator;
import com.gas.main.ui.molpane.sitepanel.ref.ReferencesPanel;
import com.gas.enzyme.core.ui.RENAnalPanel;
import com.gas.main.ui.molpane.sitepanel.analysis.AnalysisPanel;
import com.gas.main.ui.molpane.sitepanel.annotation.AnnotationPanel;
import com.gas.main.ui.molpane.sitepanel.general.GeneralPanel;
import com.gas.main.ui.molpane.sitepanel.primer3.Primer3Panel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.lang.ref.WeakReference;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import org.openide.util.Lookup;

/**
 *
 * @author dunqiang
 */
public class SidePanel extends JPanel {
    private WeakReference<AnnotatedSeq> asRef;
    private WeakReference<JTabbedPane> tabbedPaneRef;
    //
    GeneralPanel generalPanel ;
        
    private WeakReference<AnnotationPanel> annotationPanelRef;    
    RENAnalPanel renAnalysisPanel;
    WeakReference<AnalysisPanel> analysisPanelRef;
    WeakReference<IPredictPanel> predictPanelRef;
    WeakReference<ReferencesPanel> referencesPanelRef;
    Primer3Panel primer3Panel;
    
    private Boolean forProtein;

    public SidePanel() {
        setLayout(new BorderLayout());

        generalPanel = new GeneralPanel();
        getTabbedPane().addTab("", generalPanel.getImageIcon(), generalPanel, generalPanel.getToolTipText());
        
        AnnotationPanel annotationPanel = new AnnotationPanel();
        annotationPanelRef = new WeakReference<AnnotationPanel>(annotationPanel);
        getTabbedPane().addTab("", annotationPanel.getImageIcon(), annotationPanel, annotationPanel.getToolTipText());

        AnalysisPanel analysisPanel = new AnalysisPanel();        
        analysisPanelRef = new WeakReference<AnalysisPanel>(analysisPanel);
        getTabbedPane().addTab("", analysisPanel.getImageIcon(), analysisPanel, analysisPanel.getToolTipText());;
        
        ReferencesPanel referencePanel = new ReferencesPanel();
        referencesPanelRef = new WeakReference<ReferencesPanel>(referencePanel);
        getTabbedPane().addTab("", referencePanel.getImageIcon(), referencePanel, referencePanel.getToolTipText());

        renAnalysisPanel = new RENAnalPanel();
        getTabbedPane().addTab("", renAnalysisPanel.getImageIcon(), renAnalysisPanel, renAnalysisPanel.getToolTipText());

        IPredictPanelCreator predictPanelCreator = Lookup.getDefault().lookup(IPredictPanelCreator.class);
        if(predictPanelCreator != null){
            IPredictPanel predictPanel = predictPanelCreator.create();
            predictPanelRef = new WeakReference<IPredictPanel>(predictPanel);
            getTabbedPane().addTab("", predictPanel.getImageIcon(), (JPanel)predictPanel, predictPanel.getToolTipText());
        }
        
        primer3Panel = new Primer3Panel();
        getTabbedPane().addTab("", primer3Panel.getImageIcon(), primer3Panel, primer3Panel.getToolTipText());
        
        add(getTabbedPane(), BorderLayout.CENTER);
              
        hookupListeners();
    }
    
    public void performRENAnalysis(){
        getRenAnalysisPanel().performAnalysis();
    }
    
    public boolean isShowingRENResultPage(){
        if(getRenAnalysisPanel() != null){
            return getRenAnalysisPanel().isShowingResultPage();
        }else{
            return false;
        }
    }
    
    public ReferencesPanel getReferencesPanel(){
        return referencesPanelRef.get();
    }
    
    public Primer3Panel getPrimer3Panel(){        
        return primer3Panel;
    }
    
    public void refresh(){
        annotationPanelRef.get().refresh();
        predictPanelRef.get().refresh();
        getPrimer3Panel().refresh();
        renAnalysisPanel.refresh();
    }
    
    public AnnotatedSeq getAs(){
        if(asRef != null){
            return asRef.get();
        }else{
            return null;
        }
    }
    
    private void hookupListeners(){
        addPropertyChangeListener(new SidePanelListeners.PtyChangeListener());
        final Dimension originalSize = getTabbedPane().getPreferredSize();
        getTabbedPane().addChangeListener(new SidePanelListeners.TabbedPaneChangeLtr(originalSize));      
    }

    public GeneralPanel getGeneralPanel() {
        return generalPanel;
    }
    
    public AnalysisPanel getAnalysisPanel(){
        return analysisPanelRef.get();
    }

    public Boolean getForProtein() {
        return forProtein;
    }

    public void setForProtein(Boolean forProtein) {
        Boolean old = this.forProtein;
        this.forProtein = forProtein;
        firePropertyChange("forProtein", old, this.forProtein);
    }

    public void setAs(AnnotatedSeq as) {              
        asRef = new WeakReference<AnnotatedSeq>(as);
        firePropertyChange("as", null, asRef.get());        
    }

    protected final JTabbedPane getTabbedPane() {
        if (tabbedPaneRef == null || tabbedPaneRef.get() == null) {
            JTabbedPane tabbedPane = JTabbedPaneFactory.create(SwingConstants.RIGHT, Color.LIGHT_GRAY);
            tabbedPaneRef = new WeakReference<JTabbedPane>(tabbedPane);
        }
        return tabbedPaneRef.get();
    }

    public AnnotationPanel getAnnotationPanel(){
        return annotationPanelRef.get();
    }

    public RENAnalPanel getRenAnalysisPanel() {
        return renAnalysisPanel;
    }
    
    public IPredictPanel getPredictPanel(){
        return predictPanelRef.get();
    }
    
    public void displayRENAnalysisPanel(){
        JTabbedPane tabbedPane = getTabbedPane();
        tabbedPane.setSelectedComponent(getRenAnalysisPanel());
    }
}
