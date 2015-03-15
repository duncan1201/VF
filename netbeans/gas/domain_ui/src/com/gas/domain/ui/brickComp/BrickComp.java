/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.brickComp;

import com.gas.common.ui.core.VariantMapMdl;
import com.gas.common.ui.core.VariantMapMdl.Read;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.IVisibleLocProvider;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.light.BaseList;
import com.gas.common.ui.misc.Loc2D;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JComponent;

/**
 *
 * @author dunqiang
 */
public class BrickComp extends JComponent {

    static Logger log = Logger.getLogger(BrickComp.class.getName());
    public static final String SEQ = "Sequence";
    public static final String FRAME = "Frame %d";
    private Integer frame;
    private Integer desiredHeight;
    private Integer desiredWidth;
    private Loc selection;
    private float scale = 1.7f;
    protected VariantMapMdl.Read variants;
    private List<Character> data;
    private BaseList forwardTextList = new BaseList();
    private BaseList reverseTextList = new BaseList();
    // for internal use
    protected Integer scrollableUnitIncrement;
    protected boolean doubleLine;
    // for internal use only
    protected IColorProvider colorProvider;

    public BrickComp() {
        super();
        hookupListener();
    }

    public void setSelection2D(Loc2D loc2d) {
        if (loc2d != null) {
            setSelection(loc2d.toLoc());
        } else {
            setSelection(null);
        }
    }

    public void setSelection(Loc loc) {
        Loc old = this.selection;
        this.selection = loc;
        firePropertyChange("selection", old, this.selection);
    }

    protected BaseList getForwardTextList() {
        return forwardTextList;
    }

    protected BaseList getReverseTextList() {
        return reverseTextList;
    }

    public void setFrame(Integer frame) {
        this.frame = frame;
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        Rectangle old = getBounds();
        super.setBounds(x, y, width, height);
        Rectangle current = new Rectangle(x, y, width, height);
        firePropertyChange("bounds", old, current);
    }

    public void setColorProvider(IColorProvider colorProvider) {
        IColorProvider old = this.colorProvider;
        this.colorProvider = colorProvider;
        firePropertyChange("colorProvider", old, this.colorProvider);
    }

    private void hookupListener() {
        addPropertyChangeListener(new BrickCompListeners.PtyChangeListener());
    }

    public void setVariants(Read variants) {
        Read old = this.variants;
        this.variants = variants;
        firePropertyChange("variants", old, this.variants);
    }

    public List<Character> getData() {
        return data;
    }

    public void strikeThroughLine1(Loc loc, boolean strikeThrough) {
        forwardTextList.setStrikeout(loc.getStart(), loc.getEnd(), strikeThrough);
    }

    public void strikeThroughLine2(Loc loc, boolean strikeThrough) {
        reverseTextList.setStrikeout(loc.getStart(), loc.getEnd(), strikeThrough);
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        float old = this.scale;
        this.scale = scale;
        firePropertyChange("scale", old, this.scale);
    }

    public Integer getScrollableUnitIncrement() {
        if (scrollableUnitIncrement == null) {
            resetDesiredWidth();
            int dWidth = getDesiredWidth();
            scrollableUnitIncrement = MathUtil.round(1.0f * dWidth / data.size());
        }
        return scrollableUnitIncrement;
    }

    public float getUnitWidth() {
        Dimension size = getSize();

        Insets insets = getInsets();
        int totalDisplayWidth = size.width - insets.left - insets.right;
        float unitWidth = totalDisplayWidth * 1.0f / getData().size();
        return unitWidth;
    }

    public void setDoubleLine(boolean doubleLine) {
        boolean old = this.doubleLine;
        this.doubleLine = doubleLine;
        firePropertyChange("doubleLine", old, this.doubleLine);
    }

    public Boolean isDoubleLine() {
        return doubleLine;
    }

    public int getBrickCount() {
        if(data == null){
            return 0;
        }else{
            return data.size();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension size = getSize();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size.width, size.height);
        IVisibleLocProvider visibleLocProvider = UIUtil.getParent(this, IVisibleLocProvider.class);
        if(visibleLocProvider == null){
            System.out.println();
        }
        boolean paintVisibleOnly = visibleLocProvider.isPaintVisibleOnly();
        Loc visibleLoc;
        if (paintVisibleOnly) {
            visibleLoc = visibleLocProvider.calculateVisibleLoc();
        } else {
            visibleLoc = visibleLocProvider.getTotalLoc();
        }
        if(visibleLoc == null){
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        if (!isDoubleLine()) {
            Integer startPos = null;
            Integer endPos = null;
            if (frame == null) {
                startPos = visibleLoc.getStart();
                endPos = visibleLoc.getEnd();
            } else if (frame > 0) {
                startPos = (int) Math.ceil((visibleLoc.getStart() + frame - 1) / 3.0);
                endPos = (int) Math.ceil((visibleLoc.getEnd() + frame - 1) / 3.0);
            } else if (frame < 0) {
                startPos = (int) Math.ceil((visibleLoc.getStart()) / 3.0 - 1);
                startPos = Math.max(1, startPos);
                endPos = (int) Math.ceil((visibleLoc.getEnd()) / 3.0 + 1);
                endPos = Math.min(endPos, forwardTextList.size());
            }           
            forwardTextList.paint(g2d, startPos, endPos);
        } else {
            forwardTextList.paint(g2d, visibleLoc.getStart(), visibleLoc.getEnd());

            reverseTextList.paint(g2d, visibleLoc.getStart(), visibleLoc.getEnd());
        }

    }

    public void setData(String data) {
        setData(StrUtil.toChars(data));
    }

    public void setData(List<Character> data) {
        List<Character> old = this.data;
        this.data = data;
        firePropertyChange("data", old, this.data);
    }

    public Integer getDesiredHeight() {
        if (desiredHeight == null) {
            Font font = getFont();
            FontMetrics fm = FontUtil.getFontMetrics(font);
            desiredHeight = fm.getHeight();
            if (isDoubleLine()) {
                desiredHeight *= 2;
            }
        }
        return desiredHeight;
    }

    public Integer getDesiredWidth() {
        if (desiredWidth == null) {
            Insets insets = getInsets();
            Font font = getFont();
            int charWidth = FontUtil.getMSFontCharWidth(font);
            desiredWidth = MathUtil.round(charWidth * data.size() * getScale()) + insets.left + insets.right;
        }
        return desiredWidth;
    }

    public void resetDesiredWidth() {
        desiredWidth = null;
        getDesiredWidth();
    }

    public void resetDesiredHeight() {
        desiredHeight = null;
        getDesiredHeight();
    }

    public Integer getCaretX(int xParent) {
        return getCaretX(xParent, 0);
    }

    /**
     * @return the caret coordinate relative to BrickComp
     */
    public Point getCaretLoc(int pos) {
        Point ret = null;
        if (!forwardTextList.isEmpty()) {
            ret = forwardTextList.getCaretLoc(pos);
        }
        return ret;
    }

    public Integer getCaretPos(int xParent) {
        Integer ret = null;
        if (!forwardTextList.isEmpty()) {
            ret = forwardTextList.getCaretPos(xParent);
        }
        return ret;
    }

    public Integer getCaretX(int xParent, int posOffset) {
        Integer ret = null;
        if (!forwardTextList.isEmpty()) {
            ret = forwardTextList.getCaretX(xParent, posOffset);
        }
        return ret;
    }

    protected boolean validateInput(char c) {
        boolean ret;
        ret = BioUtil.isDNA(c);
        if (!ret) {
            ret = BioUtil.isProtein(c);
        }
        if (!ret) {
            ret = c == '-' || c == '*';
        }
        return true;
    }

    /*
     * Seq Frame 1 Frame 2 Frame 3 Frame -1 Frame -2 Frame -3
     */
    public static class SeqFirstComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            int ret = 0;
            Integer f1 = null;
            Integer f2 = null;
            if (o1.startsWith("Frame")) {
                String frameNo = o1.split(" ")[1];
                f1 = Integer.parseInt(frameNo);
            }
            if (o2.startsWith("Frame")) {
                String frameNo = o2.split(" ")[1];
                f2 = Integer.parseInt(frameNo);
            }

            if (f1 != null && f2 != null) {
                if (f1 < 0 && f2 < 0) {
                    ret = f1.compareTo(f2) * -1;
                } else if (f1 < 0) {
                    ret = 1;
                } else if (f2 < 0) {
                    ret = -1;
                } else {
                    ret = f1.compareTo(f2);
                }

            } else if (f1 != null) { // o2 == SEQ
                ret = 1;
            } else if (f2 != null) { // o1 == SEQ
                ret = -1;
            }
            return ret;
        }
    }

    /*
     * Frame 2 Frame 2 Frame 1 Seq Frame -1 Frame -2 Frame -3
     */
    public static class SeqMiddleBrickPanelComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            int ret = 0;
            Integer f1 = null;
            Integer f2 = null;
            if (o1.startsWith("Frame")) {
                String frameNo = o1.split(" ")[1];
                f1 = Integer.parseInt(frameNo);
            }
            if (o2.startsWith("Frame")) {
                String frameNo = o2.split(" ")[1];
                f2 = Integer.parseInt(frameNo);
            }
            if (f1 != null && f2 != null) {
                ret = f1.compareTo(f2) * -1;
            } else if (f1 != null) { // o2 == SEQ
                if (f1 > 0) {
                    ret = -1;
                } else {
                    ret = 1;
                }
            } else if (f2 != null) { // o1 == SEQ
                if (f2 > 0) {
                    ret = 1;
                } else {
                    ret = -1;
                }
            }
            return ret;
        }
    }
}
