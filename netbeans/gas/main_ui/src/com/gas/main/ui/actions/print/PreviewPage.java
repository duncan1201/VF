/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions.print;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.IPrintEditor;
import com.gas.domain.ui.editor.PrintParam;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

/**
 *
 * @author dq
 */
class PreviewPage extends JComponent implements Printable {

    private PrintParam printParam;
    private Image image;

    public PreviewPage() {
        setBackground(Color.WHITE);
        hookupListeners();
    }

    private void hookupListeners() {
        addPropertyChangeListener(new PreviewPageListeners.PtyListener());
    }

    @Override
    public void paintComponent(Graphics g) {
        if (image == null || printParam == null || printParam.getZoomScale() == null) {
            return;
        }
        PageFormat pf = printParam.getPageFormat();
        final double widthPf = pf.getWidth();
        final double heightPf = pf.getHeight();
        Dimension size = getSize();
        if (widthPf == 0 || heightPf == 0) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, size.width, size.height);

        paintPageBorder(g2d);
        paintFooterHeader(g2d, getBounds(), this.printParam.getZoomScale().floatValue());
        paintImage(g2d, getBounds(), image, this.printParam.getZoomScale());

        JScrollPane scrollPane = UIUtil.getParent(this, JScrollPane.class);
        scrollPane.revalidate();
    }

    private void paintFooterHeader(Graphics2D g2d, Rectangle rect, float scale) {
        g2d.setColor(Color.BLACK);
        final Font font = getFont();
        final FontMetrics fm = FontUtil.getFontMetrics(font);
        final Dimension size = rect.getSize();
        final PageFormat pageFormat = this.printParam.getPageFormat();
        final Double widthPaper = UIUtil.paperLengthToPixel(pageFormat.getWidth());
        final Double heightPaper = UIUtil.paperLengthToPixel(pageFormat.getHeight());

        final float distance2NorthBorder = (size.height - heightPaper.floatValue() * scale) * 0.5f;
        final float distance2EastBorder = (size.width - widthPaper.floatValue() * scale) * 0.5f;

        float x;
        float y;

        if (this.printParam.getPrintPageNo()) {
            String pageNoStr ;
            if(getPrintEditor().isPrintableJComponent()){
                pageNoStr = String.format("Page %d", printParam.getPageNo());
            }else{
                pageNoStr = String.format("Page %d of %d", printParam.getPageNo(), printParam.getPageCount());
            }            
            x = (size.width - fm.stringWidth(pageNoStr)) * 0.5f;
            y = (size.height - distance2NorthBorder - fm.getHeight());
            g2d.drawString(pageNoStr, x, y);
        }

        if (this.printParam.getPrintDate() && !getPrintEditor().isPrintableJComponent()) {
            String dateStr = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH).format(new Date());
            x = (size.width - distance2EastBorder - fm.stringWidth(dateStr) - fm.getHeight());
            y = distance2NorthBorder + fm.getHeight();
            g2d.drawString(dateStr, x, y);
        }

        if (this.printParam.getPrintName()) {
            String nameStr = this.printParam.getJobName().getValue();
            if(getPrintEditor().isPrintableJComponent()){
                x = (size.width - fm.stringWidth(nameStr)) * 0.5f;
            }else{
                x = distance2EastBorder + fm.getHeight();
            }
            
            y = distance2NorthBorder + fm.getHeight();
            g2d.drawString(nameStr, x, y);
        }
    }

    private void paintMargin(Graphics2D g2d) {
        PageFormat pf = printParam.getPageFormat();
        double widthImageable = UIUtil.paperLengthToPixel(pf.getImageableWidth());
        double heightImageable = UIUtil.paperLengthToPixel(pf.getImageableHeight());

        final Dimension size = getSize();
        final Double scale = printParam.getZoomScale();

        int x = MathUtil.round((size.width - widthImageable * scale) * 0.5);
        int y = MathUtil.round((size.height - heightImageable * scale) * 0.5);
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10.0f, new float[]{5, 5}, 0.0f));
        g2d.drawRect(x, y, MathUtil.round(widthImageable * scale), MathUtil.round(heightImageable * scale));
    }

    private void paintPageBorder(Graphics2D g2d) {
        PageFormat pf = printParam.getPageFormat();
        final double widthPf = UIUtil.paperLengthToPixel(pf.getWidth());
        final double heightPf = UIUtil.paperLengthToPixel(pf.getHeight());

        final Dimension size = getSize();
        final Double scale = printParam.getZoomScale();

        int x = MathUtil.round((size.width - widthPf * scale) * 0.5);
        int y = MathUtil.round((size.height - heightPf * scale) * 0.5);
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x, y, MathUtil.round(widthPf * scale), MathUtil.round(heightPf * scale));

        final int borderWidth = 2;
        BevelBorder border = new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.DARK_GRAY);
        border.paintBorder(this, g2d, x - borderWidth, y - borderWidth, MathUtil.round(widthPf * scale + 2 * borderWidth), MathUtil.round(heightPf * scale + 2 * borderWidth));
    }

    /**
     * Paints the image at the center
     */
    private void paintImage(Graphics g, Rectangle rect, Image image, double scale) {
        final Dimension size = rect.getSize();
        PageFormat pf = printParam.getPageFormat();
        /* -2 for possible rounding errors */
        final double widthImageable = UIUtil.paperLengthToPixel(pf.getImageableWidth()) - 2;
        final double heightImageable = UIUtil.paperLengthToPixel(pf.getImageableHeight()) - 2;

        final int widthImage = image.getWidth(this);

        final int heightImage = image.getHeight(this);

        int widthImageableScaled = (int)Math.floor(widthImageable * scale);
        int heightImageableScaled = (int)Math.floor(heightImage * widthImageableScaled / widthImage);

        if(widthImage > widthImageable){
            widthImageableScaled = (int)Math.floor(widthImageable * scale);
            heightImageableScaled = (int)Math.floor(heightImage * widthImageableScaled / widthImage);            
        }else if(heightImage > heightImageable){
            heightImageableScaled = (int)Math.floor(heightImageable * scale);
            widthImageableScaled = (int)Math.floor(widthImage * heightImageableScaled / heightImage);                
        }
        
        Image imageScaled;
        int x;
        int y;
        if (widthImage > widthImageable || heightImage > heightImageable) {

            imageScaled = image.getScaledInstance(widthImageableScaled, heightImageableScaled, Image.SCALE_DEFAULT);
            x = MathUtil.round((size.width - imageScaled.getWidth(this)) * 0.5);
            y = MathUtil.round((size.height - imageScaled.getHeight(this)) * 0.5);
            
        } else {
            imageScaled = image.getScaledInstance(MathUtil.round(widthImage * scale), MathUtil.round(heightImage * scale), Image.SCALE_DEFAULT);
            x = MathUtil.round((size.width - imageScaled.getWidth(this)) * 0.5);
            y = MathUtil.round((size.height - imageScaled.getHeight(this)) * 0.5);
        }
        g.drawImage(imageScaled, x, y, this);
    }

    public void setPrintParam(PrintParam printParam) {
        PrintParam old = this.printParam;
        this.printParam = printParam;
        firePropertyChange("printParam", old, this.printParam);
    }

    protected IPrintEditor getPrintEditor() {
        IPrintEditor ret = null;
        PrintPanel printPanel = UIUtil.getParent(this, PrintPanel.class);
        if (printPanel != null) {
            ret = printPanel.getPrintEditor();
        }
        return ret;
    }

    protected PrintParam getPrintParam() {
        return printParam;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension ret = new Dimension();
        PageFormat pf = printParam.getPageFormat();
        Double scale = printParam.getZoomScale();
        double height = UIUtil.paperLengthToPixel(pf.getHeight());
        double width = UIUtil.paperLengthToPixel(pf.getWidth());

        ret.setSize(width * scale, height * scale + 10);
        return ret;
    }

    public void setImage(Image image) {
        Image old = this.image;
        this.image = image;
        firePropertyChange("image", old, this.image);
    }

    /**
     * @param pageIndex 0-based
     */
    @Override
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
        int pageCount = printParam.getPageCount();
        final double xImageable = pageFormat.getImageableX();
        final double yImageable = pageFormat.getImageableY();
        final double widthPaper = UIUtil.paperLengthToPixel(pageFormat.getWidth());
        final double heightPaper = UIUtil.paperLengthToPixel(pageFormat.getHeight());
        Rectangle paperRect = new Rectangle();
        paperRect.setFrame(0, 0, widthPaper, heightPaper);
        printParam.setZoomScale(1.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        IPrintEditor editor = getPrintEditor();
        if (pageIndex < pageCount) {
            double scale = 72.0 / UIUtil.getScreenResolution();
            g2d.scale(scale, scale);

            printParam.setPageNo(pageIndex + 1);
            Image imagePrintable = editor.createImageForPrinting(printParam);
            BufferedImage image3 = UIUtil.createCompatibleImage(paperRect.width, paperRect.height);
            Graphics2D gImage = image3.createGraphics();
            gImage.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            paintImage(gImage, paperRect, imagePrintable, 1);
            paintFooterHeader(gImage, paperRect, 1);
            gImage.dispose();

            g2d.drawImage(image3, 0, 0, null);
            return Printable.PAGE_EXISTS;
        } else {
            return Printable.NO_SUCH_PAGE;
        }
    }
}
