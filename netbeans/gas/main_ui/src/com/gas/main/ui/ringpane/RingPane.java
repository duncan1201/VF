/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.color.ColorProviderFetcher;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.TranslationResult;
import com.gas.common.ui.util.Pref;
import com.gas.domain.ui.pref.ColorPref;
import com.gas.main.ui.minimap.Minimap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import java.awt.print.Printable;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 *
 * @author dq
 */
public class RingPane extends JPanel implements IReclaimable {
    
    Minimap minimap;
    WeakReference<RingGraphPanel> ringGraphPanelRef;
    private WeakReference<AnnotatedSeq> asRef;
    private Boolean minimapShown;
    private Float annotationLabelSize;
    private WeakReference<JScrollBar> scrollBarRef;
    private WeakReference<JScrollPane> scrollPaneRef;
    private Integer centerPos;
    private Set<TranslationResult> translationResults = Collections.EMPTY_SET;
    private IColorProvider translationColorProvider;
    private RingPaneListeners.ColorPreferenceListener colorPrefListener;

    public RingPane() {
        LayoutManager layout = null;
        layout = new BorderLayout(0, 0);
        setLayout(layout);
        minimap = new Minimap();
        add(minimap, BorderLayout.NORTH);

        RingGraphPanel ringGraphPanel = new RingGraphPanel();
        ringGraphPanelRef = new WeakReference<RingGraphPanel>(ringGraphPanel);
        JScrollPane scrollPane = new JScrollPane(ringGraphPanel);
        scrollPaneRef = new WeakReference<JScrollPane>(scrollPane);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
        UIUtil.disableScrolling(scrollPane);

        JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL);
        scrollBar.setMinimum(0);
        scrollBar.setVisibleAmount(0);
        add(scrollBar, BorderLayout.SOUTH);
        scrollBarRef = new WeakReference<JScrollBar>(scrollBar);
        
        hookupListeners();
    }
    
    @Override
    public void cleanup(){
        minimap = null;
        ColorPref.getInstance().removePropertyChangeListener(colorPrefListener);
    }
    
    public LocList getSelections(){
        LocList ret = new LocList();
        Loc loc = ringGraphPanelRef.get().getSelectedLoc();
        if(loc != null){
            ret.add(loc);
        }
        return ret;
    }
    
    public void setSelections(LocList locList){
        if(!locList.isEmpty()){
            ringGraphPanelRef.get().setSelectedLoc(locList.get(0));
        }else{
            ringGraphPanelRef.get().setSelectedLoc(null);
        }
    }
    
    public void setTrackVisible(String name, boolean visible){
        ringGraphPanelRef.get().getSortedRingMap().clear();
        ringGraphPanelRef.get().getLabelList().clear();
        ringGraphPanelRef.get().getConnectors().clear();
        ringGraphPanelRef.get().revalidate();
        minimap.refresh();
    }
 
    public void refresh(Integer caretPos){
        minimap.refresh();
        getRingGraphPanel().refresh(caretPos);
    }
    
    protected void scroll(int amount){
        JScrollBar scrollBar = getScrollBar();
        int value = scrollBar.getValue();
        final int max = scrollBar.getMaximum();
        final int min = scrollBar.getMinimum();
        if(amount > 0){
            int nV = (value + amount) % max;
            getScrollBar().setValue(nV);
        }else if (amount < 0){
            int nV = value + amount;
            if(nV < min){
                nV = max;
            }
            getScrollBar().setValue(nV);
        }
    }

    public Integer getCenterPos() {
        return centerPos;
    }

    public void setCenterPos(Integer centerPos) {
        Integer old = this.centerPos;
        this.centerPos = centerPos;
        firePropertyChange("centerPos", old, this.centerPos);
    }
    
    public void setAnnotationLabelSize(Float annotationLabelSize) {
        Float old = this.annotationLabelSize;
        this.annotationLabelSize = annotationLabelSize;
        firePropertyChange("annotationLabelSize", old, this.annotationLabelSize);
    }
    
    public Float getAnnotationLabelSize(){
        return this.annotationLabelSize;
    }

    public IColorProvider getTranslationColorProvider() {
        if(translationColorProvider == null){
            String name = Pref.ColorProviderPrefs.getInstance().getColorProviderName(Pref.ColorProviderPrefs.KEY.TRANSLATION);
            translationColorProvider = ColorProviderFetcher.getColorProvider(name);
        }
        return translationColorProvider;
    }

    public void setTranslationColorProvider(IColorProvider translationColorProvider) {
        IColorProvider old = this.getTranslationColorProvider();
        this.translationColorProvider = translationColorProvider;
        firePropertyChange("translationColorProvider", old, this.translationColorProvider);
    }

    private void hookupListeners() {
        colorPrefListener = new RingPaneListeners.ColorPreferenceListener(this);
        ColorPref.getInstance().addPropertyChangeListener(colorPrefListener);
        minimap.addPropertyChangeListener(new RingPaneListeners.MinimapPtyListener(this));
        getScrollBar().addAdjustmentListener(new RingPaneListeners.ScrollBarListener(this));
        addPropertyChangeListener(new RingPaneListeners.PtyChangeListener());
        getScrollPane().getHorizontalScrollBar().getModel().addChangeListener(new RingPaneListeners.ScrollPaneHBarModelListener(this)); 
        getScrollPane().addComponentListener(new RingPaneListeners.ScrollPaneResizeListener());
    }

    public Boolean isBaseNumberShown() {
        if(getAs() != null){
            return getAs().getAsPref().isBaseNumberShown();
        }
        return null;
    }

    public void setBaseNumberShown(Boolean baseNumberShown) {
        getRingGraphPanel().revalidate();
    }
    
    public JScrollBar getScrollBar() {
        return scrollBarRef.get();
    }

    public Minimap getMinimap() {
        return minimap;
    }

    public Boolean getMinimapShown() {
        return minimapShown;
    }

    public void setMinimapShown(Boolean minimapShown) {
        Boolean old = this.minimapShown;
        this.minimapShown = minimapShown;
        firePropertyChange("minimapShown", old, this.minimapShown);
    }

    public JScrollPane getScrollPane() {
        return scrollPaneRef.get();
    }

    public void setAs(AnnotatedSeq as) {        
        this.asRef = new WeakReference<AnnotatedSeq>(as);
        firePropertyChange("as", null, getAs());
    }
    
    public AnnotatedSeq getAs(){
        if(asRef != null){
            return asRef.get();
        }else{
            return null;
        }
    }

    public RingGraphPanel getRingGraphPanel() {
        return ringGraphPanelRef.get();
    }
    
    public void setTranslationResults(Set<TranslationResult> translationResults){
        Set<TranslationResult> old = this.translationResults;
        this.translationResults = translationResults;
        firePropertyChange("translationResults", old, this.translationResults);
    }

    public Set<TranslationResult> getTranslationResults() {
        return translationResults;
    }
    
        
    public Printable createPrintable(){
        return null;
    }
    
    
}
