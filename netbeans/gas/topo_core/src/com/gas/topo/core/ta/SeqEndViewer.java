/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.topo.core.ta;

import com.gas.common.ui.color.ClustalDNAColorProvider;
import com.gas.common.ui.color.ColorProviderFetcher;
import com.gas.common.ui.color.IColorProvider;
import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Overhang;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
public class SeqEndViewer extends JComponent {

    private String forward5 = "";
    private String forward3 = "";
    private String reverse5 = "";
    private String reverse3 = "";
    private Boolean circular;
    private AnnotatedSeq as;
    private Font font = FontUtil.getDefaultMSFont();
    static final int BASE_GAP = 2;
    private static final Color COLOR_LABEL = Color.DARK_GRAY;
    private static final Color COLOR_LABEL_5_3 = ColorCnst.DUKE_BLUE;
    private static final Color COLOR_CONNECTORS = Color.BLUE;    
    IColorProvider colorProvider = ColorProviderFetcher.getColorProvider(ClustalDNAColorProvider.NAME);

    public SeqEndViewer(boolean circular) {
        this.circular = circular;
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final int charWidth = fm.charWidth('A');
        Insets insets = null;
        if (circular) {
            // 5' and sequence are inside the border
            insets = new Insets(charWidth, charWidth * 3, charWidth, charWidth * 3);
        } else {
            // 5' and sequence are inside border
            insets = new Insets(0, charWidth * 7, 0, charWidth * 7);
        }
        setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
    }

    public boolean isCircular() {
        return circular;
    }

    public String getComp3() {
        return reverse3;
    }

    public void setComp3(String comp3) {
        this.reverse3 = comp3;
    }

    public String getComp5() {
        return reverse5;
    }

    public void setComp5(String comp5) {
        this.reverse5 = comp5;
    }

    public String getForward3() {
        return forward3;
    }

    public void setForward3(String forward3) {
        this.forward3 = forward3;
    }

    public String getForward5() {
        return forward5;
    }

    public void setForward5(String forward5) {
        this.forward5 = forward5;
    }

    @Override
    protected void paintComponent(Graphics g) {
        validateInput();
        Graphics2D g2d = (Graphics2D) g;      
        if (circular) {
            paintCircular(g2d);
        } else {
            paintLinear(g2d);
        }
    }

    private void validateInput() {
        if (forward5.length() != reverse3.length()) {
            throw new IllegalArgumentException("The length of forward5 is not the same as comp3");
        }
        if (forward3.length() != reverse5.length()) {
            throw new IllegalArgumentException("The length of forward3 is not the same as comp5");
        }
    }

    public AnnotatedSeq getAs() {
        return as;
    }

    public void setAs(AnnotatedSeq as) {
        this.as = as;
        final int length = as.getLength();
        final String data = as.getSiquence().getData();
        final String compData = BioUtil.complement(data);
        final Overhang startOverhang = as.getStartOverhang();
        if (startOverhang == null) {
            forward5 = data.substring(0, 6);
            reverse3 = compData.substring(0, 6);
        } else {
            int overhangLength = startOverhang.getLength();
            if (startOverhang.isFivePrime()) {
                forward5 = data.substring(0, 6);
                String compSeq = compData.substring(overhangLength, 6);
                compSeq = StrUtil.append(compSeq, ' ', overhangLength);
                reverse3 = compSeq;
            } else {
                String seq = data.substring(overhangLength, 6);
                seq = StrUtil.insertFront(seq, ' ', overhangLength);
                forward5 = seq;
                reverse3 = compData.substring(0, 6);
            }
        }

        final Overhang endOverhang = as.getEndOverhang();
        if (endOverhang == null) {
            forward3 = data.substring(data.length() - 6, data.length());
            reverse5 = compData.substring(data.length() - 6, compData.length());
        } else {
            int overhangLength = endOverhang.getLength();
            if (endOverhang.isFivePrime()) {
                String seq = data.substring(data.length() - 6, data.length() - overhangLength);
                seq = StrUtil.append(seq, ' ', overhangLength);
                forward3 = seq;
                reverse5 = compData.substring(compData.length() - 6, compData.length());
            } else {
                forward3 = data.substring(data.length() - 6, data.length());
                String compSeq = compData.substring(compData.length() - 6, compData.length() - overhangLength);
                compSeq = StrUtil.append(compSeq, ' ', overhangLength);
                reverse5 = compSeq;
            }
        }
    }

    private void paintCircular(Graphics2D g) {
        final Dimension size = getSize();
        final Insets insets = getInsets();
        final float yMiddle = size.height * 0.5f;
        
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final int fontHeight = fm.getHeight();
        final int ascent = fm.getAscent();
        final int descent = fm.getDescent();
        final int leading = fm.getLeading();
        final int charWidth = fm.charWidth('A');

        final float seqMiddle = fontHeight + insets.top;
        
        final float yLine1 = seqMiddle - leading - descent;
        final float yLine2 = seqMiddle + ascent;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size.width, size.height);
        
        // draw the connector first
        paintRings(g, seqMiddle, fontHeight);

        // draw 3' and 5'
        int x = insets.left;
        g.setColor(COLOR_LABEL_5_3);
        //g.drawString("3'", x, yLine1);
        //g.drawString("5'", x, yLine2);
        //x += 3 * charWidth;
        
        for (int i = 0; i < forward3.length(); i++) {            
            final String baseForward = forward3.substring(i, i + 1);
            Color color = colorProvider.getColor(baseForward);
            g.setColor(color);
            g.drawString(baseForward, x, yLine1);
            final String baseReverse = reverse5.substring(i, i + 1);
            color = colorProvider.getColor(baseReverse);
            g.setColor(color);
            g.drawString(baseReverse, x, yLine2);
            
            x += charWidth;
        }

        final int xBarEnd = size.width - forward5.length() * charWidth - insets.right ;//- " ".length() * charWidth ;//- "5'".length() * charWidth;

        for (int i = 0; i < forward5.length(); i++) {
            x = xBarEnd + i * charWidth;
            final String baseForward = forward5.substring(i, i + 1);
            Color color = colorProvider.getColor(baseForward);
            g.setColor(color);
            g.drawString(baseForward, x, yLine1);
            final String baseReverse = reverse3.substring(i, i + 1);
            color = colorProvider.getColor(baseReverse);
            g.setColor(color);
            g.drawString(baseReverse, x, yLine2);
        }

        // draw 5' and 3'
        g.setColor(COLOR_LABEL_5_3);
        x = size.width - fm.stringWidth("5'") - insets.right;
        //g.drawString("5'", x, yLine1);
        //g.drawString("3'", x, yLine2);

        // draw the length
        g.setColor(COLOR_LABEL);
        final String lengthStr = String.format("%s bp", as.getLength());
        x = Math.round((size.width - fm.stringWidth(lengthStr)) * 0.5f);
        final int y = Math.round(yMiddle + ascent);
        g.drawString(lengthStr, x, y);

        // draw the name
        final String name = StrUtil.getStr4Display(as.getName(), fm, size.width);
        x = Math.round((size.width - fm.stringWidth(name)) * 0.5f);
        g.drawString(name, x, yMiddle - descent - leading);
    }
    
    private void paintRings(Graphics2D g, float seqMiddle, int fontHeight){
        final int RING_GAP = Math.round(fontHeight * 0.5f);
        Dimension size = getSize();
        Insets insets = getInsets();
        g.setColor(COLOR_CONNECTORS);        
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        RoundRectangle2D outer = new RoundRectangle2D.Double();
        outer.setRoundRect(1, seqMiddle - fontHeight * 0.5f, size.width - 2, size.height - seqMiddle - insets.bottom, fontHeight, fontHeight);
        g.draw(outer);
        RoundRectangle2D inner = new RoundRectangle2D.Double();
        inner.setRoundRect(outer.getX() + RING_GAP, seqMiddle + fontHeight * 0.5f, outer.getWidth() - RING_GAP - RING_GAP, size.height - seqMiddle - insets.bottom - fontHeight - RING_GAP, fontHeight, fontHeight);
        g.draw(inner);
        g.setColor(Color.WHITE);
        g.fillRect(insets.left, MathUtil.round(outer.getY() - 1), size.width - insets.left - insets.right, fontHeight + 3);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);        
    }

    private void paintLinear(Graphics2D g) {
        final Dimension size = getSize();
        final Insets insets = getInsets();
        final float yMiddle = size.height * 0.5f;

        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final int fontHeight = fm.getHeight();
        final int ascent = fm.getAscent();
        final int descent = fm.getDescent();
        final int leading = fm.getLeading();
        final int charWidth = fm.charWidth('A');

        final float y = yMiddle - leading - descent;
        final float y2 = yMiddle + ascent;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, size.width, size.height);

        // draw starting 5' and 3'
        g.setColor(COLOR_LABEL_5_3);
        int x = charWidth * 1;
        g.drawString("5'", x, y);
        g.drawString("3'", x, y2);

        // draw the left sequences
        for (int i = 0; i < forward5.length(); i++) {
            x = insets.left + i * charWidth;
            final String baseForward = forward5.substring(i, i + 1);
            Color color = colorProvider.getColor(baseForward);
            g.setColor(color);
            g.drawString(baseForward, x, y);
            final String baseReverse = reverse3.substring(i, i + 1);
            color = colorProvider.getColor(baseReverse);
            g.setColor(color);
            g.drawString(baseReverse, x, y2);
        }
        final int xBarStart = forward5.length() * charWidth + insets.left;

        final int xBarEnd = size.width - forward3.length() * charWidth - insets.right;

        // draw the right sequence
        for (int i = 0; i < forward3.length(); i++) {
            x = xBarEnd + i * charWidth;
            final String baseForward = forward3.substring(i, i + 1);
            Color color = colorProvider.getColor(baseForward);
            g.setColor(color);
            g.drawString(baseForward, x, y);
            final String baseReverse = reverse5.substring(i, i + 1);
            color = colorProvider.getColor(baseReverse);
            g.setColor(color);
            g.drawString(baseReverse, x, y2);
        }

        // draw the ending 3' and 5'
        g.setColor(COLOR_LABEL_5_3);
        x = size.width - charWidth * 1 - fm.stringWidth("5'");
        g.drawString("3'", x, y);
        g.drawString("5'", x, y2);

        // draw the center connectors
        g.setColor(COLOR_CONNECTORS);
        int yLine1 = Math.round(yMiddle - fontHeight * 0.2f);
        int yLine2 = Math.round(yMiddle + fontHeight * 0.2f);
        Stroke oldStroke = g.getStroke();
        //g.setStroke(new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{charWidth, charWidth}, 1));
        g.drawLine(xBarStart, yLine1, xBarEnd, yLine1);
        g.drawLine(xBarStart, yLine2, xBarEnd, yLine2);
        //g.setStroke(oldStroke);

        // draw the length label
        g.setColor(COLOR_LABEL);
        final String lengthStr = String.format("%d bp", as.getLength());
        x = Math.round((size.width - fm.stringWidth(lengthStr)) * 0.5f);
        g.drawString(String.format("%d bp", as.getLength()), x, yLine2 + ascent);

        // draw the name label
        final String name = StrUtil.getStr4Display(as.getName(), fm, size.width);
        x = Math.round((size.width - fm.stringWidth(name)) * 0.5f);
        g.drawString(name, x, yLine1 - descent - leading);
    }

    public Dimension getDesiredSize() {
        Dimension ret = new Dimension();
        final FontMetrics fm = FontUtil.getFontMetrics(this);
        final int charWidth = fm.charWidth('A');
        if (circular) {
            ret.height = Math.round(fm.getHeight() * 7.8f);
        } else {
            ret.height = Math.round(fm.getHeight() * 4.2f);
        }
        int desiredWidth;
        final int ENDs_DISTANCE_LENGTH = 29;
        final int DEFAULT_ENDs_LENGTH = 6;
        if (forward5.length() == 0) {
            desiredWidth = charWidth * (DEFAULT_ENDs_LENGTH * 2) + charWidth * ENDs_DISTANCE_LENGTH;
        } else {
            desiredWidth = charWidth * (forward5.length() * 2) + charWidth * ENDs_DISTANCE_LENGTH;
        }
        ret.width = desiredWidth;
        return ret;
    }
}
