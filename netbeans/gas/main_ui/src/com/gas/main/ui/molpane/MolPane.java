/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.caret.ICaretParent;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.misc.api.IJEditorPaneService;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.LocUtil;
import com.gas.main.ui.molpane.sitepanel.SidePanel;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.as.util.AnnotatedSeqWriter;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.gc.api.GCResult;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.common.ui.util.Pref;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.IZoomable;
import com.gas.domain.ui.editor.TopBracket;
import com.gas.domain.ui.editor.TopBracketList;
import com.gas.main.ui.molpane.graphpane.GraphPane;
import com.gas.main.ui.ringpane.RingPane;
import com.gas.domain.ui.shape.IShapeListMap;
import com.gas.main.ui.actions.print.PrintAction;
import com.gas.main.ui.molpane.ancestor.AncestorPanel;
import com.gas.main.ui.ringpane.LabelList;
import com.gas.main.ui.ringpane.Ring;
import com.gas.main.ui.ringpane.SortedRingListMap;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import org.openide.util.Lookup;

/**
 *
 * @author dunqiang
 */
public class MolPane extends JPanel implements IReclaimable, IMolPane, IZoomable {

    private AnnotatedSeq as;
    private WeakReference<GraphPane> graphPaneRef;
    private WeakReference<RingPane> ringPaneRef;
    private JEditorPane editorPane;
    private WeakReference<SidePanel> sidePanelRef;
    private Boolean doubleStranded;
    private Boolean linearDisplay;
    private Boolean minimapShown;
    private Boolean baseNumberShown;
    private Float rulerFontSize;
    private Float baseFontSize;
    private Float annotationLabelSize;
    private Boolean editable;
    private RMap rmap;
    private Set<TranslationResult> translationResults;
    private GCResult gcResult;
    private WeakReference<MainPanel> mainPanelRef;
    //internal use
    //CardLayout cardLayout;
    public final static String RING_CARD = "RING_CARD";
    public final static String LINEAR_CARD = "GRAPH_CARD";

    public static MolPane createInstance() {
        MolPane ret = new MolPane();
        return ret;
    }

    private MolPane() {
        //super(JTabbedPane.BOTTOM);
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        MainPanel mainPanel = new MainPanel();
        mainPanelRef = new WeakReference<MainPanel>(mainPanel);


        IJEditorPaneService editorPaneService = Lookup.getDefault().lookup(IJEditorPaneService.class);
        editorPane = editorPaneService.create(false);
        editorPane.setFont(FontUtil.getDefaultMSFont());
        UIUtil.attachAction(editorPane, KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK), new PrintAction());
        JScrollPane scrollPane = new JScrollPane(editorPane);
        scrollPane.addComponentListener(new MolPaneListeners.TextViewListener());
        
        mainPanel.addComponent("Text View", scrollPane);

        mainPanel.addSidePanel(getSidePanel());

        getSidePanel().setVisible(false);

        add(mainPanel, BorderLayout.CENTER);

        hookupListeners();

    }
    
    ICaretParent getCaretParent(){
        ICaretParent ret = null;
        if(as != null){
            if(as.isCircular()){
                ret = getRingPane().getRingGraphPanel();
            }else{
                ret = getGraphPane().getGraphPanel();
            }
        }
        return ret;
    }
    
    @Override
    public void performRENAnalysis(){
        getSidePanel().performRENAnalysis();
    }
    
    @Override
    public boolean isShowingRENResultPage(){
        return getSidePanel().isShowingRENResultPage();
    }
    
    public void displayRENAnalysisPanel(){
        getSidePanel().displayRENAnalysisPanel();
    }

    public List<Object> getShapeData(String trackName, String displayText) {
        return getShapeData(trackName, displayText, null, null);
    }

    @Override
    public List<Object> getShapeData(String trackName, Integer start, Integer end) {
        return getShapeData(trackName, null, start, end);
    }

    public List<Object> getShapeData(String fetureKey, String displayText, Integer start, Integer end) {
        List<Object> ret = new ArrayList<Object>();
        if (as != null && !as.isCircular()) {
            IShapeListMap arrowListMap = getGraphPane().getGraphPanel().getFeatureArrowMap();
            if (fetureKey != null) {
                arrowListMap = arrowListMap.getByTypes(fetureKey);
            }
            if (displayText != null) {
                arrowListMap = arrowListMap.getByDisplayTexts(displayText);
            }
            if (start != null && end != null) {
                arrowListMap = arrowListMap.getByLocation(start, end);
            }

            ret = arrowListMap.getAllData();
        } else if (as != null && as.isCircular()) {
            SortedRingListMap ringListMap = getRingPane().getRingGraphPanel().getSortedRingMap();
            if (fetureKey != null) {
                ringListMap = ringListMap.getByKey(fetureKey);
            }
            if (displayText != null) {
                LabelList labelList = getRingPane().getRingGraphPanel().getLabelList().getByText(displayText);
                List<Ring> rings = labelList.getData(Ring.class);
                ringListMap = ringListMap.getByRings(rings);
            }
            if (start != null && end != null) {
                ringListMap = ringListMap.getByLocation(start, end);
            }
            ret = ringListMap.getAllData();
        }

        return ret;
    }

    public void deleteFeature(Feture feture) {
        as.getFetureSet().remove(feture);
        refresh();
    }

    public void setGCResult(GCResult gcResult) {
        GCResult old = this.gcResult;
        this.gcResult = gcResult;
        firePropertyChange("gcResult", old, this.gcResult);
    }

    public Float getRulerFontSize() {
        return rulerFontSize;
    }

    @Override
    public void clearSelectedSites() {
        getGraphPane().getGraphPanel().getResPanel().clearSelection();
        getGraphPane().getGraphPanel().getTrianglesMap().clearSelection();
    }

    @Override
    public void setSelectedSite(TopBracket topBracket) {
        RMap.Entry entry = (RMap.Entry) topBracket.getData();
        setSelectedSite(new RMap.EntryList(entry));
    }

    public RMap.EntryList getSelectedSites() {
        RMap.EntryList ret = new RMap.EntryList();
        if (as != null && as.isCircular()) {
            ret = getRingPane().getRingGraphPanel().getCurvedBrackets().getSelectedEntries();
        } else if (as != null && !as.isCircular()) {
            TopBracketList tp = getGraphPane().getGraphPanel().getSelectedTopBrackets();
            if (tp != null) {
                ret = tp.getEntries();
            }
        }
        return ret;
    }

    @Override
    public void setSelectedSite(RMap.EntryList entries) {
        if (as != null && !as.isCircular()) {
            if (entries == null || entries.isEmpty()) {
                getGraphPane().getGraphPanel().getResPanel().clearSelection();
            } else {
                getGraphPane().getGraphPanel().getResPanel().selectExclusively(entries);
            }
            if (entries == null || entries.isEmpty()) {
                getGraphPane().getGraphPanel().getTrianglesMap().clearSelection();
            } else {
                getGraphPane().getGraphPanel().getTrianglesMap().select(entries);
            }
        } else if (as != null && as.isCircular()) {
            getRingPane().getRingGraphPanel().selectCurvedBrackets(entries);
        }
    }

    /**
     * Select & center the feature
     */
    @Override
    public void setSelectedFeture(Feture feture) {
        if (as != null && !as.isCircular()) {
            getGraphPane().setSelectedFeture(feture);
        } else if (as != null && as.isCircular()) {
            getRingPane().getRingGraphPanel().setSelectedFeture(feture);
            if(feture != null){
                Loc loc = feture.getLucation().toLoc();
                int center = LocUtil.getCenter(loc.getStart(), loc.getEnd(), as.getLength());
                getRingPane().setCenterPos(center);
            }
        }
    }

    @Override
    public void center(Loc loc) {
        if (loc.getTotalPos() == null) {
            loc.setTotalPos(as.getLength());
        }
        if (as != null && !as.isCircular()) {
            getGraphPane().setCenterLoc(loc);
            getGraphPane().center(loc);
        } else if (as != null && as.isCircular()) {
            getRingPane().setCenterPos(loc.center());
        }
    }

    public void setRulerFontSize(Float rulerFontSize) {
        Float old = this.rulerFontSize;
        this.rulerFontSize = rulerFontSize;
        firePropertyChange("rulerFontSize", old, this.rulerFontSize);
    }

    public Float getAnnotationLabelSize() {
        return annotationLabelSize;
    }

    public void setEditable(Boolean e) {
        Boolean old = this.editable;
        this.editable = e;
        firePropertyChange("editable", old, this.editable);
    }

    public void setAnnotationLabelSize(Float annotationLabelSize) {
        Float old = this.annotationLabelSize;
        this.annotationLabelSize = annotationLabelSize;
        firePropertyChange("annotationLabelSize", old, this.annotationLabelSize);
    }

    public Float getBaseFontSize() {
        return baseFontSize;
    }

    public void setBaseFontSize(Float baseFontSize) {
        Float old = this.baseFontSize;
        this.baseFontSize = baseFontSize;
        firePropertyChange("baseFontSize", old, this.baseFontSize);
    }

    @Override
    public Map<Loc, String> getSelectedSeqs() {
        Map<Loc, String> ret = new HashMap();
        if (as != null) {
            String seq = as.getSiquence().getData();
            LocList locList = new LocList();

            if (!as.isCircular()) {
                locList = getGraphPane().getSelections();
            } else if (as.isCircular()) {
                locList = getRingPane().getSelections();
            }
            Iterator<Loc> itr = locList.iterator();
            while (itr.hasNext()) {
                Loc loc = itr.next();   
                String subSeq = as.getSiquence().getData(loc.getStart(), loc.getEnd());
                ret.put(loc, subSeq);
            }
        }
        return ret;
    }

    public Boolean getDoubleStranded() {
        return doubleStranded;
    }

    public void setDoubleStranded(Boolean doubleStranded) {
        Boolean old = this.doubleStranded;
        this.doubleStranded = doubleStranded;
        firePropertyChange("doubleStranded", old, this.doubleStranded);
    }

    public void setBaseNumberShown(Boolean shown) {
        Boolean old = this.baseNumberShown;
        this.baseNumberShown = shown;
        firePropertyChange("baseNumberShown", old, this.baseNumberShown);
    }

    public Boolean getBaseNumberShown() {
        return this.baseNumberShown;
    }

    @Override
    public String getSequence() {
        String seq = as.getSiquence().getData();
        return seq;
    }

    @Override
    public void setSelection(Loc loc) {
        LocList locList = new LocList();
        locList.add(loc);
        setSelections(locList);
    }

    public void setSelections(LocList locList) {
        if (as != null && as.isCircular()) {
            getRingPane().setSelections(locList);
        } else if (as != null && !as.isCircular()) {
            getGraphPane().setSelections(locList);
        }
    }

    @Override
    public LocList getSelections() {
        LocList ret = null;
        if (as != null && as.isCircular()) {
            ret = getRingPane().getSelections();
        } else if (as != null && !as.isCircular()) {
            ret = getGraphPane().getSelections();
        }
        return ret;
    }

    @Override
    public void setTranslationColorProvider(IColorProvider colorProvider) {
        AnnotatedSeq as = getAs();
        if (as != null && as.isCircular()) {
            getRingPane().setTranslationColorProvider(colorProvider);
        } else if (as != null && !as.isCircular()) {
            getGraphPane().setTranslationColorProvider(colorProvider);
        }
    }

    @Override
    public void setPrimarySeqColorProvider(IColorProvider colorProvider) {
        Pref.ColorProviderPrefs.KEY key = AsHelper.isAminoAcid(as) ? Pref.ColorProviderPrefs.KEY.PROTEIN : Pref.ColorProviderPrefs.KEY.DNA;
        Pref.ColorProviderPrefs.getInstance().setColorProviderName(key, colorProvider.getName());

        if (as != null && !as.isCircular()) {
            getGraphPane().setPrimarySeqColorProvider(colorProvider);
        } else if (as != null && as.isCircular()) {
            getRingPane().getRingGraphPanel().getBrickRing().setColorProvider(colorProvider);
        }
    }

    @Override
    public boolean isCircular() {
        return as.isCircular();
    }

    @Override
    public void setTranslationResults(Set<TranslationResult> translationResults) {
        Set<TranslationResult> old = this.translationResults;
        this.translationResults = translationResults;
        firePropertyChange("translationResults", old, this.translationResults);
    }

    @Override
    public void cleanup() {
        as = null;
        editorPane = null;
        rmap = null;
    }

    public RMap getRmap() {
        return rmap;
    }

    @Override
    public void setRmap(RMap rmap) {
        RMap old = this.rmap;
        this.rmap = rmap;
        firePropertyChange("rmap", old, this.rmap);
    }

    public Boolean isLinearDisplay() {
        return linearDisplay;
    }

    public void setLinearDisplay(Boolean linearDisplay) {
        Boolean old = this.linearDisplay;
        this.linearDisplay = linearDisplay;
        firePropertyChange("linearDisplay", old, this.linearDisplay);
    }

    public void setMinimapShown(Boolean shown) {
        Boolean old = this.minimapShown;
        this.minimapShown = shown;
        firePropertyChange("minimapShown", old, this.minimapShown);
    }

    public Boolean getMinimapShown() {
        return this.minimapShown;
    }

    public RingPane getRingPane() {
        return ringPaneRef.get();
    }

    public GraphPane getGraphPane() {
        if (graphPaneRef != null) {
            return graphPaneRef.get();
        } else {
            return null;
        }
    }

    private void hookupListeners() {
        addPropertyChangeListener(new MolPaneListeners.PtyChangeListener(this));
        //TrackVisiblePref.getInstance().addPropertyChangeListener(new MolPaneListeners.TrackVisibleListener(this));
        Pref.CommonPtyPrefs.getInstance().addPropertyChangeListener(new MolPaneListeners.PrefListener(this));
    }

    @Override
    public void refresh() {

        updateEditorPane();

        if (as != null && !as.isCircular()) {
            getGraphPane().refresh();
        }

        if (as != null && as.isCircular()) {
            getRingPane().refresh(null);
        }

        if (as != null) {
            getSidePanel().refresh();
        }
    }

    public SidePanel getSidePanel() {
        if (sidePanelRef == null || sidePanelRef.get() == null) {
            SidePanel sidePanel = new SidePanel();
            sidePanelRef = new WeakReference<SidePanel>(sidePanel);
        }
        return sidePanelRef.get();
    }

    @Override
    public AnnotatedSeq getAs() {
        return as;
    }

    public void setAs(AnnotatedSeq as) {
        this.as = as;

        as.getAsPref().addPropertyChangeListener(new MolPaneListeners.AsPrefListener(this));

        if (as.getOperation() != null) {
            AncestorPanel ancestorPanel = new AncestorPanel();
            mainPanelRef.get().addComponent("Ancestors", ancestorPanel);
            ancestorPanel.populateUI(as);
        }

        if (as.isCircular()) {
            RingPane ringPane = new RingPane();
            ringPaneRef = new WeakReference<RingPane>(ringPane);
            mainPanelRef.get().insertComponent("Graphical View", ringPane, 0);
            ringPane.setAs(as);
        } else {
            GraphPane graphPane = new GraphPane();
            graphPaneRef = new WeakReference<GraphPane>(graphPane);
            mainPanelRef.get().insertComponent("Graphical View", graphPane, 0);
            graphPaneRef.get().setAs(as);
        }
        mainPanelRef.get().getZoomPanel().setZoom(Math.round(as.getAsPref().getZoom() * 100));
        getSidePanel().setVisible(true);
        getSidePanel().setAs(as);
        updateEditorPane();

        setDoubleStranded(as.getAsPref().isDoubleStranded());  


        if (as.getGcResult() != null) {
            setGCResult(as.getGcResult());
        }
    }

    @Override
    public void setZoom(int zoom) {
        if (as != null) {
            as.getAsPref().setZoom(zoom / 100.0f);
        }
    }

    protected void updateEditorPane() {
        as.setSequence(as.getSiquence().getData().toUpperCase(Locale.ENGLISH));
        String str = AnnotatedSeqWriter.toString(as);
        editorPane.setText(str);
    }

    /**
     * @return the currently viewable component:
     * {@code RingPane, GraphPane, JEditorPane, AncestorsComp}
     */
    public JComponent getCurrentComponent() {
        Component comp = mainPanelRef.get().getShowingComponent();
        if (comp instanceof JScrollPane) {
            JScrollPane scrollPane = (JScrollPane) comp;
            comp = scrollPane.getViewport().getView();
        } else if (comp instanceof AncestorPanel) {
            AncestorPanel ap = (AncestorPanel) comp;
            comp = ap.getAncestorsComp();
        }
        return (JComponent) comp;
    }
    
    @Override
    public boolean canZoomOut(int zoom) {
        return true;
    }    
}
