/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.JobHoldUntil;
import javax.print.attribute.standard.PrinterResolution;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.junit.Test;
import junit.framework.Assert;

/**
 *
 * @author dq
 */
public class UIUtilTest {

    //@Test
    public void testTransform() {
        Rectangle2D r = new Rectangle2D.Float(200, 200, 10, 10);
        AffineTransform at = AffineTransform.getRotateInstance(3);
        Rectangle2D newR = UIUtil.transform(at, r, true);
        Assert.assertEquals(newR.getWidth(), r.getWidth());
        Assert.assertEquals(newR.getHeight(), r.getHeight());
    }

    //@Test
    public void testToImage() {

        JPanel instance = new JPanel();
        JButton btn = new JButton("12345678");
        instance.add(btn);
        //Component expResult = null;
        //Component result = instance.getSelected();

        //1. Create the frame.
        JFrame frame = new JFrame("FrameDemo");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(instance, BorderLayout.CENTER);

//4. Size the frame.
        //frame.pack();

//5. Show it.
        frame.setSize(550, 400);
        frame.setVisible(true);
        AffineTransform at = AffineTransform.getScaleInstance(0.9, 0.9);

        BufferedImage image = UIUtil.toImage(btn);
        Graphics g = image.createGraphics();
        UIUtil.toFile(image, "PNG", new File("D:\\tmp\\test.png"));

        while (true) {
        }
    }

    //@Test
    public void testGetDefaultPrintRequestAttributeSet() throws FileNotFoundException, PrintException {

        int r = Toolkit.getDefaultToolkit().getScreenResolution();
        PrintService printService = PrintServiceLookup.lookupDefaultPrintService();
        PrintRequestAttributeSet d = UIUtil.getDefaultPrintRequestAttributeSet(printService);
    }

    //@Test
    public void testPrinting() throws FileNotFoundException, PrintException, IOException {
        final Image img = ImageIO.read(new File("D:\\tmp\\ret2.png"));
        PrinterJob printJob = PrinterJob.getPrinterJob();
        PrintService printService2 = printJob.getPrintService();
        PrintRequestAttributeSet printRequestAttributeSet = UIUtil.getDefaultPrintRequestAttributeSet(printService2);
        printJob.printDialog(printRequestAttributeSet);
        PageFormat pf = printJob.getPageFormat(printRequestAttributeSet);
        System.out.print("");
        System.out.println("pf.getHeight()=" + pf.getHeight());
        System.out.println("pf.getWidth()=" + pf.getWidth());
        printJob.setPrintable(new Printable() {
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex != 0) {
                    return NO_SUCH_PAGE;
                }
                System.out.print("");
                Paper paper = pageFormat.getPaper();
                System.out.println("pageFormat.getHeight()=" + pageFormat.getHeight());
                System.out.println("pageFormat.getWidth()=" + pageFormat.getWidth());
                System.out.println("pageFormat.getImageableHeight()=" + pageFormat.getImageableHeight());
                System.out.println("pageFormat.getImageableHeight()=" + pageFormat.getImageableWidth());

                Graphics2D g2d = (Graphics2D) graphics;
                double scale = 72.0 / 96.0;
                g2d.scale(scale, scale);
                double xImageable = pageFormat.getImageableX();
                double yImageable = pageFormat.getImageableY();
                g2d.translate(xImageable / scale, yImageable / scale);
                g2d.drawImage(img, 0, 0, null);
                return PAGE_EXISTS;
            }
        });
        boolean doPrint = true;//printJob.printDialog();
        if (doPrint) {
            try {
                //set.add(pr);

                JobHoldUntil immediately = new JobHoldUntil(new Date(0L));
                printRequestAttributeSet.add(immediately);

                printJob.print(printRequestAttributeSet);

            } catch (Exception prt) {
                System.err.println(prt.getMessage());
            }
        }
    }

    @Test
    public void testABC() {
        BufferedImage image = UIUtil.createCompatibleImage(100, 100, Transparency.BITMASK);
        int type = image.getType();
        System.out.println(type);
        image = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g = image.createGraphics();

        g.drawString("Hello 2", 30, 30);
        g.dispose();

        UIUtil.toFile(image, "wbmp", new File("D:\\tmp\\hello.wbmp"));
    }
}
