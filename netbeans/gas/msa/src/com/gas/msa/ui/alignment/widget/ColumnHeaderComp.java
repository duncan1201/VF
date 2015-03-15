/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.widget;

import com.gas.al2co.service.api.IAl2CoService;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.light.*;
import com.gas.common.ui.matrix.api.IMatrixService;
import com.gas.common.ui.matrix.api.Matrix;
import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.msa.MSA;
import com.gas.domain.core.msa.SeqLogoParam;
import com.gas.domain.ui.pref.MSAPref;
import com.gas.database.core.msa.service.api.CounterList;
import com.gas.database.core.msa.service.api.IConsensusService;
import com.gas.database.core.msa.service.api.IConservationService;
import com.gas.database.core.msa.service.api.ICounterListService;
import com.gas.msa.seqlogo.service.api.ISeqLogoService;
import com.gas.msa.ui.MSAEditor;
import com.gas.msa.ui.alignment.AlignPane;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ColumnHeaderComp extends JComponent {

    private IAl2CoService al2coService = Lookup.getDefault().lookup(IAl2CoService.class);
    private ISeqLogoService logoService = Lookup.getDefault().lookup(ISeqLogoService.class);
    private ICounterListService counterService = Lookup.getDefault().lookup(ICounterListService.class);
    private IConsensusService consensusService = Lookup.getDefault().lookup(IConsensusService.class);
    private WeakReference<MSA> msaRef;
    private LinearRuler linearRuler;
    private BaseList consensusList = new BaseList();
    private Histogram coveragePlot;
    private Histogram qualityPlot;
    private StyledTextSetMap seqLogoUI = new StyledTextSetMap();
    private RectOverlei overlei = new RectOverlei();
    protected Font consensusFont = FontUtil.getDefaultMSFont();
    private boolean scaleUpSeqLogo = true;
    private Integer consensusHeight;
    private Integer rulerHeight;
    public static final int GAP = 2;
    private IColorProvider colorProvider;
    private Loc spotLight;
    private Loc2D selection;
    private CNST.PAINT paintState;
    private boolean paintVisibleOnly = true;

    public ColumnHeaderComp() {
        setOpaque(false);
        addPropertyChangeListener(new ColumnHeaderCompListeners.PtyListener());
        Insets insets = AlignPane.getPadding();
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }

    @Override
    public void paintComponent(Graphics g) {
        setPaintState(CNST.PAINT.STARTED);
        Graphics2D g2d = (Graphics2D) g;

        final Dimension size = getSize();
        final Insets insets = getInsets();
        g.setColor(getBackground());
        g.fillRect(0, 0, size.width, size.height);
        final Rectangle visibleRect = getVisibleRect();
        double startPos;
        if (paintVisibleOnly) {
            startPos = Math.floor(consensusList.size() * (visibleRect.getX() - insets.left) / (size.getWidth() - insets.left - insets.right));
            startPos = Math.max(1, startPos);
        } else {
            startPos = 1;
        }
        double endPos;
        if (paintVisibleOnly) {
            endPos = Math.ceil(consensusList.size() * (visibleRect.getX() - insets.left + visibleRect.getWidth()) / (size.getWidth() - insets.left - insets.right));
            endPos = Math.min(consensusList.size(), endPos);
        } else {
            endPos = getMsa().getLength();
        }
        _paint(g2d, (int) startPos, (int) endPos);

        setPaintState(CNST.PAINT.ENDING);
    }

    public void setPaintVisibleOnly(boolean paintVisibleOnly) {
        boolean old = paintVisibleOnly;
        this.paintVisibleOnly = paintVisibleOnly;
        firePropertyChange("paintVisibleOnly", old, this.paintVisibleOnly);
    }

    public void setPaintState(CNST.PAINT paintState) {
        CNST.PAINT old = this.paintState;
        this.paintState = paintState;
        firePropertyChange("paintState", old, this.paintState);
    }

    protected void setSpotLight(Loc spotLight) {
        Loc old = this.spotLight;
        this.spotLight = spotLight;
        firePropertyChange("spotLight", old, this.spotLight);
    }

    protected Loc getSpotLight() {
        return this.spotLight;
    }

    protected Loc2D getSelection() {
        return selection;
    }

    protected void setSelection(Loc2D selection) {
        Loc2D old = this.selection;
        this.selection = selection;
        firePropertyChange("selection", old, this.selection);
    }

    public void refresh() {
        initPreferredHeight();
        repaint();
    }

    public void setColorProvider(IColorProvider colorProvider) {
        IColorProvider old = this.colorProvider;
        this.colorProvider = colorProvider;
        firePropertyChange("colorProvider", old, this.colorProvider);
    }

    protected BaseList getConsensusList() {
        return consensusList;
    }

    protected StyledTextSetMap getSeqLogoUI() {
        return seqLogoUI;
    }

    protected Histogram getQualityPlot() {
        return qualityPlot;
    }

    protected LinearRuler getLinearRuler() {
        return linearRuler;
    }

    private void _paint(Graphics2D g, int startPos, int endPos) {
        g.setFont(consensusFont);
        final Dimension size = getSize();
        final Insets insets = getInsets();
        final Rectangle visibleRect = getVisibleRect();

        int _y = 0;

        Map<IPaintable, Integer> paintables = getPaintables();
        Iterator<IPaintable> itr = paintables.keySet().iterator();

        while (itr.hasNext()) {
            IPaintable paintable = itr.next();

            Integer height = paintables.get(paintable);
            Rectangle2D.Double drawRect = new Rectangle2D.Double(insets.left, _y, size.width - insets.left - insets.right, height);
            boolean intersects = visibleRect.intersects(drawRect);
            if (intersects) {
                paintable.setRect(drawRect);
                paintable.paint(g, startPos, endPos);
            }
            _y += height;
            _y += GAP;
        }
        overlei.setTotalPos(consensusList.size());
        overlei.paint(g, new Rectangle2D.Double(insets.left, 0, size.width - insets.left - insets.right, size.height));
    }

    protected RectOverlei getOverlei() {
        return overlei;
    }

    protected MSA getMsa() {
        MSAEditor editor = UIUtil.getParent(this, MSAEditor.class);
        return editor.getMsa();
    }

    private Map<IPaintable, Integer> getPaintables() {
        Map<IPaintable, Integer> ret = new LinkedHashMap<IPaintable, Integer>();
        ret.put(linearRuler, rulerHeight);
        ret.put(consensusList, consensusHeight);
        if (getMsa().getMsaSetting().isCoverageDisplay()) {
            ret.put(coveragePlot, getMsa().getMsaSetting().getCoverageHeight());
        }
        if (getMsa().getMsaSetting().isSeqLogoDisplay()) {
            ret.put(seqLogoUI, getMsa().getMsaSetting().getSeqLogoHeight());
        }
        if (getMsa().getMsaSetting().isQualityDisplay()) {
            ret.put(qualityPlot, getMsa().getMsaSetting().getQualityHeight());
        }
        return ret;
    }

    public void setMsa(MSA msa) {
        msaRef = new WeakReference<MSA>(msa);
        createUIObjects();
    }

    protected void createUIObjects() {
        if (getMsa() == null) {
            throw new IllegalArgumentException();
        }

        linearRuler = createLinearRuler();

        consensusList = createConsensusTextList(getMsa().getConsensus());

        createUIPlotObjects();

        initPreferredHeight();
    }

    protected Histogram getCoveragePlot() {
        return coveragePlot;
    }

    /**
     * create the coverage plot, sequence logo plot , identity plot and linear
     * ruler
     */
    public void createUIPlotObjects() {
        CounterList counterList = counterService.createCounterList(getMsa().getEntriesMapCopy());
        coveragePlot = createCoverage(counterList);

        seqLogoUI = createStyledTextListMap(counterList);
        if (colorProvider != null) {
            seqLogoUI.setColorProvider(colorProvider);
        }

        if (getMsa().getQualityScores() != null && getMsa().getQualityScores().length > 0) {
            IConservationService consService = Lookup.getDefault().lookup(IConservationService.class);
            String matrixName = getMsa().getSubMatrix();
            IMatrixService matrixService = Lookup.getDefault().lookup(IMatrixService.class);
            Matrix matrix = matrixService.getAllMatrices().getMatrix(matrixName);
            int[] qualityScores = consService.calculate(counterList, matrix);
            getMsa().setQualityScores(qualityScores);
            qualityPlot = createQualityPlot(getMsa().getQualityScores());
        }

        linearRuler.setEnd(getMsa().getLength());
    }

    private Histogram createQualityPlot(int[] qualities) {
        Histogram ret = new Histogram();
        ret.setFrom(ColorCnst.AO);
        ret.setTo(Color.RED);
        ret.setDataList(qualities);
        return ret;
    }

    public void recalculateRefreshConsensusUI() {        
        String consensus = consensusService.calculate(getMsa().getEntriesMapCopy(), getMsa().getConsensusParam(), getMsa().isDnaByGuess());
        getMsa().setConsensus(consensus);
        consensusList = createConsensusTextList(getMsa().getConsensus());
        repaint();
    }

    protected void initPreferredHeight() {
        consensusHeight = calculateConsensusHeight();
        int pHeight = 0;
        pHeight += linearRuler.calculateDesiredHeight();
        pHeight += GAP;
        pHeight += consensusHeight;
        if (getMsa().getMsaSetting().isCoverageDisplay()) {
            pHeight += GAP;
            pHeight += getMsa().getMsaSetting().getCoverageHeight();
        }
        if (getMsa().getQualityScores() != null && getMsa().getQualityScores().length > 0 && getMsa().getMsaSetting().isQualityDisplay()) {
            pHeight += GAP;
            pHeight += getMsa().getMsaSetting().getQualityHeight();
        }
        if (getMsa().getMsaSetting().isSeqLogoDisplay()) {
            pHeight += GAP;
            pHeight += getMsa().getMsaSetting().getSeqLogoHeight();
        }
        pHeight += GAP;
        UIUtil.setPreferredHeight(this, pHeight);

    }

    protected int calculateConsensusHeight() {
        final FontMetrics fm = FontUtil.getFontMetrics(consensusFont);
        int ret = fm.getHeight();
        return ret;
    }

    private LinearRuler createLinearRuler() {
        LinearRuler ret = new LinearRuler();
        ret.setStart(1);
        ret.setEnd(getMsa().getConsensus().length());
        ret.setFont(FontUtil.getDefaultSansSerifFont().deriveFont(FontUtil.getDefaultFontSize() - 1));

        rulerHeight = ret.calculateDesiredHeight();

        return ret;
    }

    protected Integer getRulerHeight() {
        return rulerHeight;
    }

    public void setScaleUpSeqLogo(boolean scaleUpSeqLogo) {
        boolean old = this.scaleUpSeqLogo;
        this.scaleUpSeqLogo = scaleUpSeqLogo;
        firePropertyChange("scaleUpSeqLogo", old, this.scaleUpSeqLogo);
    }

    private StyledTextSetMap createStyledTextListMap(CounterList counterList) {
        if (getMsa().getSeqLogoParam() == null) {
            getMsa().setSeqLogoParam(new SeqLogoParam());
        }
        boolean smallSampleCorr = getMsa().getSeqLogoParam().isSmallSampleCorrection();
        boolean dnaByGuess = getMsa().isDnaByGuess();
        StyledTextSetMap ret = logoService.createStyledTextListMap(counterList, dnaByGuess, smallSampleCorr);
        ret.setDna(dnaByGuess);
        ret.setScaleUp(scaleUpSeqLogo);
        return ret;
    }

    private Histogram createIdentity(CounterList counterList) {
        Histogram ret = new Histogram();
        ret.setFrom(ColorCnst.AO);
        ret.setTo(Color.RED);
        ret.setDataList(counterList.getLetterModeFrequency());
        return ret;
    }

    private Histogram createCoverage(CounterList counterList) {
        Histogram ret = new Histogram();
        ret.setFrom(ColorCnst.AIR_FORCE_BLUE);
        ret.setTo(ColorCnst.AIR_FORCE_BLUE);
        ret.setDataList(counterList.getLetterFrequencies());
        return ret;
    }

    private BaseList createConsensusTextList(String consensus) {
        BaseList _textList = new BaseList();
        for (int i = 0; i < consensus.length(); i++) {
            char s = consensus.charAt(i);
            Base t = new Base();
            t.setText(s);
            _textList.add(t);
        }
        if (colorProvider != null) {
            _textList.setColorProvider(colorProvider);
        }
        return _textList;
    }
}
