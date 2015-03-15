/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.common.ui.util.BioUtil;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.StrUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.gateway.core.service.api.AttSite;
import com.gas.gateway.core.service.api.IAttSiteService;
import com.gas.gateway.core.service.api.PrimerAdapter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JComponent;

/**
 *
 * @author dq
 */
class PreviewComp extends JComponent {

    private WeakReference<AddAttbSitesPanel> ref;
    private StrShapeList leftShapes = new StrShapeList();
    private StrShapeList pcrProductList = new StrShapeList();
    private StrShapeList rightShapes = new StrShapeList();

    PreviewComp() {
        setFont(FontUtil.getDefaultMSFont());
        final FontMetrics fm = FontUtil.getDefaultFontMetrics();
        int heightFont = fm.getHeight();
        setBorder(BorderFactory.createEmptyBorder(0, heightFont, 0, heightFont));
    }

    @Override
    public Dimension getPreferredSize() {
        Insets insets = UIUtil.getDefaultInsets();
        Dimension ret = new Dimension();
        final Font font = getFont().deriveFont(Font.BOLD);
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        String temp = "5'";
        temp += "G"; // gap
        temp += "GGGG" + "TA" + AttSite.attB1.getSeq();
        temp += "1234567890"; // SD sequence
        temp += "123456"; // kozak sequence
        //temp += ":::PCR Product:::";
        temp += "STO"; // stop codon
        temp += "CCCC" + "T" + AttSite.attB2.getSeq();
        temp += "G"; // gap
        temp += "3'";
        final int stringWidth = Math.round(fm.stringWidth(temp) * 1.33f);// for the hyphens "-"
        final int widthPCRProduct = fm.stringWidth(":::PCR Product:::");
        ret.width = stringWidth + widthPCRProduct + insets.left + insets.right;
        ret.height = fm.getHeight() * 5;
        return ret;
    }

    @Override
    protected void paintComponent(Graphics g) {
        AddAttbSitesPanel panel = getAddAttbSitesPanel();
        if (panel == null) {
            return;
        }
        leftShapes.clear();
        pcrProductList.clear();
        rightShapes.clear();        
        final Font font = getFont();
        final Font fontBold = font.deriveFont(Font.BOLD);
        final Font fontItalic = font.deriveFont(Font.ITALIC);

        final PrimerAdapter leftAdapter = panel.getLeftPrimerAdapter();
        final PrimerAdapter rightAdapter = panel.getRightPrimerAdapter();

        // draw the 5'
        StrShape strShape = new StrShape();
        strShape.str = "5'";
        strShape.font = font;
        strShape.strandSymbol = true;
        leftShapes.add(strShape);

        // draw the prefix 
        // forward strand
        strShape = new StrShape();
        strShape.str = leftAdapter.getPrefix().toUpperCase(Locale.ENGLISH);
        strShape.font = font;
        strShape.prefix = true;
        leftShapes.add(strShape);

        // draw the left AttB seq
        // forward strand
        strShape = new StrShape();
        strShape.str = leftAdapter.getAttSite().getSeq().toUpperCase(Locale.ENGLISH);
        strShape.font = fontBold;
        leftShapes.add(strShape);

        // postfix
        // forward
        boolean fuseForwardIncluded = ref.get().isFuseForwardPrimer();
        if(fuseForwardIncluded){
            strShape = new StrShape();
            strShape.str = leftAdapter.getPostfix();
            strShape.font = font;
            leftShapes.add(strShape);
        }

        // is SD included
        boolean sdIncluded = ref.get().isSDIncluded();
        if (sdIncluded) {
            // forward
            strShape = new StrShape();
            strShape.str = IAttSiteService.SD;
            strShape.font = font;
            strShape.sd = true;
            leftShapes.add(strShape);
             
            strShape = new StrShape();
            strShape.str = IAttSiteService.SD_postfix;
            strShape.font = font;
            leftShapes.add(strShape);            
        }

        // draw the kozak 
        boolean kozakIncluded = ref.get().isKozakIncluded();
        if (kozakIncluded) {
            // forward
            strShape = new StrShape();
            strShape.str = IAttSiteService.KOZAK;
            strShape.font = font;
            strShape.kozak = true;
            leftShapes.add(strShape);
            //g.drawString(str, x, y);
            // reverse
        }

        // draw the start codon
        boolean startCodonIncluded = ref.get().isStartCodonIncluded();
        if (startCodonIncluded) {
            // forward
            strShape = new StrShape();
            strShape.str = IAttSiteService.START_CODON;;
            strShape.font = font;
            strShape.codon = true;
            leftShapes.add(strShape);
        }
        
        // draw the pcr product
        strShape = new StrShape();
        strShape.str = ":::PCR Product:::";
        strShape.font = fontItalic;
        strShape.pcrProduct = true;
        pcrProductList.add(strShape);

        // draw the stop codon
        if (ref.get().isStopCodonIncluded()) {
            // forward
            strShape = new StrShape();
            strShape.str = IAttSiteService.STOP_CODON;;
            strShape.font = font;
            strShape.codon = true;
            rightShapes.add(strShape);
        }

        // the prefix of the right AttB site
        // forward
        boolean fuseReversePrimer = ref.get().isFuseReversePrimer();
        if(fuseReversePrimer){
            strShape = new StrShape();
            strShape.str = rightAdapter.getPrefix();
            strShape.font = font;
            rightShapes.add(strShape);
        }
        // draw the right AttB site
        // forward
        strShape = new StrShape();
        strShape.str = rightAdapter.getAttSite().getSeq().toUpperCase(Locale.ENGLISH);
        strShape.font = fontBold;
        rightShapes.add(strShape);

        // draw the postfix
        // forward
        strShape = new StrShape();
        strShape.str = rightAdapter.getPostfix().toUpperCase(Locale.ENGLISH);
        strShape.font = font;
        strShape.prefix = true;
        rightShapes.add(strShape);

        // 3'
        strShape = new StrShape();
        strShape.str = "3'";
        strShape.font = font;
        strShape.strandSymbol = true;
        rightShapes.add(strShape);

        modifyLeftShapes(leftShapes);
        modifyRightShapes(rightShapes);

        final FontMetrics fm = FontUtil.getFontMetrics(this);
        final int ascent = fm.getAscent();
        final int height = fm.getHeight();
        int rectHeight = height + height;
        final int y = Math.round(rectHeight + ascent + (height - ascent) * 0.5f);
        final int y2 = Math.round(rectHeight + height + ascent + (height - ascent) * 0.5f);
        int widthShapes = leftShapes.getWidth() + pcrProductList.getWidth() + rightShapes.getWidth();
        int xStart = Math.round(getSize().width * 0.5f - widthShapes * 0.5f);
        
        Graphics2D g2d = (Graphics2D)g;
        //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        int x2 = paint(g2d, leftShapes, xStart, y, y2);
        Color colorOld = g.getColor();
        g.setColor(Color.DARK_GRAY);
        x2 = paint(g2d, pcrProductList, x2, y, y2);
        g.setColor(colorOld);
        paint(g2d, rightShapes, x2, y, y2);
    }        
    
    private void modifyRightShapes(List<StrShape> strShapes) {
        int baseCount = 0;
        ListIterator<StrShape> itr = strShapes.listIterator();
        while(itr.hasNext()){
            itr.next();
        }
        while (itr.hasPrevious()) {
            StrShape shape = itr.previous();            
            if (shape.strandSymbol) {
                // do nothing
            } else {
                StringBuilder builder = new StringBuilder();
                for (int j = shape.str.length() - 1; j > -1; j--) {
                    builder.append(shape.str.substring(j, j + 1));
                    baseCount++;
                    if ((baseCount) % 3 == 0) {
                        builder.append("-");
                    }
                }
                shape.str = StrUtil.reverse(builder.toString());
            }
            if(!itr.hasPrevious()){
                if(shape.str.startsWith("-")){
                    shape.str = shape.str.substring(1);
                }
            }
        }
    }

    private void modifyLeftShapes(List<StrShape> strShapes) {

        int baseCount = 0;
        Iterator<StrShape> itr = strShapes.iterator();
        while (itr.hasNext()) {
            StrShape shape = itr.next();
            if (shape.strandSymbol) {
                // do nothing
            } else if (shape.prefix) {
                shape.str += "-";
            } else {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < shape.str.length(); i++) {
                    builder.append(shape.str.substring(i, i + 1));
                    baseCount++;
                    if (baseCount % 3 == 0) {
                        builder.append("-");
                    }
                }
                
                shape.str = builder.toString();
            }
            if(!itr.hasNext()){
                if(shape.str.endsWith("-")){
                    shape.str = shape.str.substring(0, shape.str.length() - 1);
                }
            }
        }
    }

    private int paint(Graphics2D g, StrShapeList strShapes, int x, int y, int y2) {
        FontMetrics fm = FontUtil.getFontMetrics(this);
        final int height = fm.getHeight();

        // draw the 5'
        int rectHeight = height + height;
        int y1Underline = rectHeight + height;
        int y2Underline = y1Underline + height;
        for (StrShape shape : strShapes) {
            String str = shape.str;
            Font font = shape.font;
            fm = FontUtil.getFontMetrics(font);
            g.setFont(font);
            if(!shape.pcrProduct){
                g.drawString(str, x, y);
            }else{
                g.drawString(str, x, (y + y2) * 0.5f);
            }

            if (shape.kozak) {
                Rectangle rect = new Rectangle();
                rect.x = x;
                rect.y = 0;
                rect.width = fm.stringWidth(str);
                rect.height = rectHeight;
                drawAnnotation(g, "Kozak", rect);
            } else if (shape.sd) {
                Rectangle rect = new Rectangle();
                rect.x = x;
                rect.y = 0;
                rect.width = fm.stringWidth(str);
                rect.height = rectHeight;
                drawAnnotation(g, "SD", rect);
            } else if (shape.codon) {
                int x2 = x + fm.stringWidth(str);
                g.drawLine(x, y1Underline, x2, y1Underline);
            }

            // reverse
            if (shape.strandSymbol) {
                if (shape.str.equals("5'")) {
                    g.drawString("3'", x, y2);
                } else if (shape.str.equals("3'")) {
                    g.drawString("5'", x, y2);
                } else {
                    g.drawString(shape.str, x, y2);
                }
            } else if(!shape.pcrProduct) {
                String comp = BioUtil.complement(shape.str);
                g.drawString(comp, x, y2);
                if(shape.codon){
                    int x2 = x + fm.stringWidth(str);
                    g.drawLine(x, y2Underline, x2, y2Underline);
                }
            }

            x += fm.stringWidth(str);
        }

        return x;
    }

    private AddAttbSitesPanel getAddAttbSitesPanel() {
        AddAttbSitesPanel ret = null;
        if (ref == null) {
            AddAttbSitesPanel panel = UIUtil.getParent(this, AddAttbSitesPanel.class);
            if (panel != null) {
                ref = new WeakReference<AddAttbSitesPanel>(panel);
                ret = panel;
            }
        }
        if (ref != null) {
            ret = ref.get();
        }
        return ret;
    }

    private void drawAnnotation(Graphics g, String text, Rectangle rect) {
        final FontMetrics fm = FontUtil.getFontMetrics(g.getFont());
        final int height = fm.getHeight();
        final int ascent = fm.getAscent();
        final int strWidth = fm.stringWidth(text);
        int x = rect.x + Math.round((rect.width - strWidth) * 0.5f);
        int y = rect.y + ascent;
        g.drawString(text, x, y);

        g.drawLine(rect.x, rect.y + rect.height, rect.x, rect.y + height);
        g.drawLine(rect.x, rect.y + height, rect.x + rect.width - 1, rect.y + height);
        g.drawLine(rect.x + rect.width - 1, rect.y + height, rect.x + rect.width - 1, rect.y + rect.height);
    }
    
    private static class StrShapeList extends ArrayList<StrShape>{
        
        int getWidth(){
            int ret = 0;
            for(StrShape s: this){
                ret += s.getWidth();
            }
            return ret;
        }
        
    }

    private static class StrShape {

        String str;
        Font font;
        boolean prefix;
        boolean strandSymbol;
        boolean pcrProduct;
        boolean kozak;
        boolean sd;
        boolean codon;
        
        int getWidth(){
            FontMetrics fm = FontUtil.getFontMetrics(font);
            return fm.stringWidth(str);
        }
    }
}