package com.gas.domain.ui.shape;

import com.gas.domain.ui.shape.layout.DefaultLayoutHelper;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.ColorUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.core.GeneralPathList;
import com.gas.common.ui.painter.LinearGradientPainter;
import com.gas.common.ui.painter.LinearGradientPainterFactory;
import com.gas.common.ui.tooltip.JTTComponent;
import com.gas.common.ui.util.FontUtil;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.FetureKeyCnst;
import com.gas.domain.core.as.Lucation;
import com.gas.domain.ui.shape.layout.ILayoutHelper;
import com.gas.domain.ui.shape.layout.LayoutHelperFinder;
import com.gas.domain.ui.shape.paint.DefaultPainter;
import com.gas.domain.ui.shape.paint.IPainter;
import com.gas.domain.ui.shape.paint.PainterFinder;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Iterator;
import java.util.logging.Logger;
import org.biojavax.bio.seq.CompoundRichLocation;

/**
 *
 * @author dunqiang
 */
public class Arrow extends JTTComponent implements IShape, IReclaimable {

    private static final Logger logger = Logger.getLogger(Arrow.class.getName());
    private float arrowCapWidthRatio = 0.3f;
    private Boolean forward = true;
    private Color seedColor = Color.RED;
    private float roundnessRatio = 0.1f;
    private float verticalPaddingRatio = 0.2f; /*
     * percentage of the font height
     */

    private float textTopPaddingRatio = 0f; /*
     * percentage of the font height
     */

    private float highlightRoundness = 0.05f;
    private Lucation luc;
    private GeneralPathList arrowPaths = new GeneralPathList();
    private Object data;
    private String displayText = "";
    private TEXT_LOC textLoc;
    private Font textFont;
    private boolean hoverEnabled = true;
    // for internal use only
    protected boolean mouseHover = false;
    protected boolean selected = false;
    protected ArrowListeners.ArrowMouseAdapter arrowMouseAdapter;

    public Arrow() {
        super();

        arrowMouseAdapter = new ArrowListeners.ArrowMouseAdapter();

        if (hoverEnabled) {
            addMouseListener(arrowMouseAdapter);
        }

        addPropertyChangeListener(new ArrowListeners.PtyChangeListener());
        setOpaque(false);

    }  

    @Override
    public void cleanup() {
        arrowMouseAdapter = null;
        data = null;
        arrowPaths.clear();
    }

    public void setHoverEnabled(boolean hoverEnabled) {
        boolean old = this.hoverEnabled;
        this.hoverEnabled = hoverEnabled;
        firePropertyChange("hoverEnabled", old, this.hoverEnabled);
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public Loc getLoc() {
        int min = luc.getStart();
        int max = luc.getEnd();
        Loc ret = new Loc(min, max);
        if (luc.getStrand() != null) {
            ret.setStrand(luc.getStrand());
        }
        return ret;
    }

    public void setData(Object data) {
        Object old = this.data;
        this.data = data;
        firePropertyChange("data", old, data);
    }

    @Override
    public Lucation getLuc() {
        return luc;
    }

    public void setLuc(Lucation luc) {
        Lucation old = this.luc;
        this.luc = luc;
        firePropertyChange("luc", old, this.luc);
    }

    @Override
    public Font getTextFont() {
        return textFont;
    }

    public void setTextFont(Font textFont) {
        Font old = this.textFont;
        this.textFont = textFont;
        firePropertyChange("textFont", old, this.textFont);
    }

    public int getTextTopPadding() {
        if (textFont == null) {
            throw new IllegalStateException("Must set the \'textFont\' first");
        }
        FontMetrics fontMetrics = FontUtil.getFontMetrics(textFont);
        int ret = MathUtil.round(fontMetrics.getHeight() * textTopPaddingRatio, Integer.class);
        return ret;
    }

    public int getTextBottomPadding() {
        if (textFont == null) {
            throw new IllegalStateException("Must set the \'textFont\' first");
        }
        FontMetrics fontMetrics = FontUtil.getFontMetrics(textFont);
        int ret = fontMetrics.getLeading();
        return ret;
    }

    public int getTextLeftPadding() {

        if (textFont == null) {
            throw new IllegalStateException("Must set the \'textFont\' first");
        }
        FontMetrics fontMetrics = FontUtil.getFontMetrics(textFont);
        int ret;
        if (forward != null && !forward) {
            ret = MathUtil.round(fontMetrics.stringWidth("a") / 2, Integer.class) + getArrowCapWidth();
        } else {
            ret = MathUtil.round(fontMetrics.stringWidth("a") / 2, Integer.class);
        }
        return ret;
    }

    public int getTextRightPadding() {
        if (textFont == null) {
            throw new IllegalStateException("Must set the \'textFont\' first");
        }
        FontMetrics fontMetrics = FontUtil.getFontMetrics(textFont);
        int ret;
        if (forward != null && !forward) {
            ret = MathUtil.round(fontMetrics.stringWidth("a") / 2, Integer.class) + getArrowCapWidth();
        } else {
            ret = MathUtil.round(fontMetrics.stringWidth("a") / 2, Integer.class);
        }
        return ret;
    }

    public void setDisplayText(String displayText) {
        this.displayText = displayText;
    }

    @Override
    public String getDisplayText() {
        return displayText;
    }

    public float getHighlightRoundness() {
        return highlightRoundness;
    }

    public void setHighlightRoundness(float highlightRoundness) {
        this.highlightRoundness = highlightRoundness;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (textLoc == null) {
            throw new IllegalStateException("txtLoc must be set");
        }
        if (luc == null) {
            return;
        }
        
        
        Feture feture = (Feture)getData();
        IPainter painter = feture == null? PainterFinder.getDefaultPainter(): PainterFinder.findPainter(feture.getKey());       
        
        painter.paint(this, (Graphics2D)g);
    
    }

    @Override
    public GeneralPathList getArrowPaths() {
        return arrowPaths;
    }
    
    @Override
    public void setArrowPaths(GeneralPathList arrowPaths) {
        this.arrowPaths = arrowPaths;
    }

    @Override
    public boolean isMouseHover() {
        return mouseHover;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        boolean old = this.selected;
        this.selected = selected;
        firePropertyChange("selected", old, this.selected);
    }

    @Override
    public int getVerticalPadding() {
        if (textFont == null) {
            throw new IllegalArgumentException("Must set the textFont");
        }
        int height = FontUtil.getFontMetrics(textFont).getHeight();
        int ret = MathUtil.round(height * verticalPaddingRatio);
        return ret;
    }

    protected GeneralPathList createArrowPath(BasicStroke stroke, Rectangle rect, Lucation luc, String fetureKey) {
        GeneralPathList ret = new GeneralPathList();
        if (luc.isEmpty()) {
            return ret;
        }
        if (luc.isContiguous()) {
            ret = PathCreator.createSingleArrowPath(this, luc.isContiguousFuzzyStart(), luc.isContiguousFuzzyEnd(), fetureKey, rect, stroke);            
        } else if (CompoundRichLocation.getJoinTerm().toString().equalsIgnoreCase(luc.getTerm())) {
            ret = PathCreator.createJoinArrowPath(this, rect, stroke, getLuc());
        } else if (CompoundRichLocation.getOrderTerm().toString().equalsIgnoreCase(luc.getTerm())) {
            //throw new IllegalArgumentException("Fix me" + luc.getTerm());
            ret = PathCreator.createJoinArrowPath(this, rect, stroke, getLuc());
        }
        return ret;
    }

    public void setVerticalPaddingRatio(float verticalPaddingRatio) {
        if (verticalPaddingRatio < 0 || verticalPaddingRatio > 0.3) {
            throw new IllegalArgumentException("VerticalMarginRatio must be between 0 and 0.3");
        }
        this.verticalPaddingRatio = verticalPaddingRatio;
    }

    @Override
    public int getRoundness() {
        Dimension size = getSize();
        int max = MathUtil.round(size.height / 2) - 1;
        //int max = 0;
        int ret = MathUtil.round(size.height * roundnessRatio);
        return Math.min(max, ret);
    }

    public void setRoundnessRatio(float roundnessRatio) {
        if (roundnessRatio > 0.5f || roundnessRatio < 0) {
            throw new IllegalArgumentException("RoundnessRatio must be between 0 and 0.5");
        }
        this.roundnessRatio = roundnessRatio;
    }

    @Override
    public void setSeedColor(Color seedColor) {
        Color old = this.seedColor;
        this.seedColor = seedColor;
        firePropertyChange("seedColor", old, seedColor);
    }

    public Color getSeedColor() {
        return seedColor;
    }

    @Override
    public int getArrowCapWidth() {
        int fontHeight = FontUtil.getFontMetrics(textFont).getHeight();
        int max = fontHeight;
        int ret = MathUtil.round(fontHeight * arrowCapWidthRatio);
        return Math.min(ret, max);
    }

    @Override
    public int getBarHeight() {
        int height = FontUtil.getFontMetrics(textFont).getHeight();
        int ret = MathUtil.round(height);

        return ret;
    }

    @Override
    public void setForward(Boolean forward) {
        this.forward = forward;
    }

    @Override
    public Boolean getForward() {
        return forward;
    }

    protected LinearGradientPaint getGradientPaint(Dimension size, boolean textInside) {
        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float(0, size.height / 2);
        float[] dist = {0.0f, 1.0f};

        Color endColor = seedColor;

        Color startColor = ColorUtil.changeBrightness(seedColor, 0.7f);

        Color[] colors = {startColor, endColor};
        LinearGradientPaint p =
                new LinearGradientPaint(start, end, dist, colors, CycleMethod.REFLECT);

        return p;
    }

    @Override
    public TEXT_LOC getTextLoc() {
        return textLoc;
    }

    @Override
    public void setTextLoc(TEXT_LOC txtLoc) {
        TEXT_LOC old = this.textLoc;
        this.textLoc = txtLoc;
        firePropertyChange("textLoc", old, txtLoc);
    }
}