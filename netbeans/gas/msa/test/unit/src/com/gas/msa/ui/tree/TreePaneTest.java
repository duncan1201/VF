/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.tree;

import com.gas.common.ui.util.ColorUtil;
import com.gas.common.ui.util.MathUtil;
import com.gas.domain.core.misc.NewickParser;
import com.gas.domain.core.misc.api.Newick;
import com.gas.domain.core.msa.MSA;
import com.gas.msa.ui.data.Data;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.font.TextAttribute;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.UIManager;
import org.junit.*;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class TreePaneTest {

    public TreePaneTest() {
    }
    private static MSA primates_msa = new MSA();
    private static MSA negative_msa = new MSA();
    private static MSA positise_msa = new MSA();
    //positise_newick.ph
    private static NewickParser newickParser = new NewickParser();

    @BeforeClass
    public static void setUpClass() throws Exception {
        UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
        Newick newick = newickParser.parse(Data.class, "primates.nex");
        primates_msa.setNewick(newick);

        newick = newickParser.parse(Data.class, "negative_newick.ph");
        negative_msa.setNewick(newick);

        newick = newickParser.parse(Data.class, "positise_newick.ph");
        positise_msa.setNewick(newick);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of setMsa method, of class TreePane.
     */
    //@Test
    public void testSetMsa() {
        System.out.println("testSetMsa");
        MSA _msa = primates_msa;
        TreePane instance = new TreePane();


//1. Create the frame.
        JFrame frame = new JFrame("TreePaneTest");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(instance, BorderLayout.CENTER);

//4. Size the frame.
        frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(size.width - 250, size.height - 300);
        frame.setVisible(true);
        while (true) {
        }
    }

    //@Test
    public void testScaleImage() throws IOException {
        File[] files = {new File("D:\\vfsite\\sequenceanalysis_vfsite\\trunk\\war\\img\\ligr.png"), new File("D:\\vfsite\\sequenceanalysis_vfsite\\trunk\\war\\img\\traces.png")};
        for (File file : files) {
            String nameFile = file.getName();
            BufferedImage image = ImageIO.read(file);
            int width = image.getWidth();
            int height = image.getHeight();
            float ratio = 0.25f;
            BufferedImage scaled = new BufferedImage(Math.round(width * ratio), Math.round(height * ratio), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) scaled.getGraphics();
            g2d.drawImage(image, 0, 0, scaled.getWidth(), scaled.getHeight(), null);

            g2d.dispose();

            File outputfile = new File("D:\\tmp\\" + "scaled_" + nameFile);
            try {
                ImageIO.write(scaled, "png", outputfile);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    //@Test
    public void testCreateTree() {
        final double centerX = 7;
        final double centerY = 7;
        final Point2D.Double center = new Point2D.Double(centerX, centerY);
        System.out.println();
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);

        Arc2D.Float f2 = new Arc2D.Float(Arc2D.OPEN);
        f2.setFrameFromCenter(centerX, centerY, centerX + 2, centerY + 2);
        f2.setAngleStart(0);
        f2.setAngleExtent(270);
        g2.draw(f2);

        Arc2D.Float f = new Arc2D.Float(Arc2D.OPEN);
        f.setFrameFromCenter(centerX, centerY, centerX + 7, centerY + 7);
        f.setAngleStart(330);
        f.setAngleExtent(100);
        g2.draw(f);
        Point2D.Double d = MathUtil.getCoordsDeg(center, f.getWidth() / 2, f.getAngleStart() + f.getAngleExtent() / 2);
        Point2D.Double d2 = MathUtil.getCoordsDeg(center, 2, f.getAngleStart() + f.getAngleExtent() / 2);
        g2.drawLine(MathUtil.round(d.x), MathUtil.round(d.y), MathUtil.round(d2.x), MathUtil.round(d2.y));

        f = new Arc2D.Float(Arc2D.OPEN);
        f.setFrameFromCenter(centerX, centerY, centerX + 6, centerY + 6);
        f.setAngleStart(140);
        f.setAngleExtent(70);
        g2.draw(f);
        d = MathUtil.getCoordsDeg(center, f.getWidth() / 2, f.getAngleStart() + f.getAngleExtent() / 2);
        d2 = MathUtil.getCoordsDeg(center, 2, f.getAngleStart() + f.getAngleExtent() / 2);
        g2.drawLine(MathUtil.round(d.x), MathUtil.round(d.y), MathUtil.round(d2.x), MathUtil.round(d2.y));

        f = new Arc2D.Float(Arc2D.OPEN);
        f.setFrameFromCenter(centerX, centerY, centerX + 6, centerY + 6);
        f.setAngleStart(240);
        f.setAngleExtent(30);
        g2.draw(f);
        d = MathUtil.getCoordsDeg(center, f.getWidth() / 2, f.getAngleStart() + f.getAngleExtent() / 2);
        d2 = MathUtil.getCoordsDeg(center, 2, f.getAngleStart() + f.getAngleExtent() / 2);
        g2.drawLine(MathUtil.round(d.x), MathUtil.round(d.y), MathUtil.round(d2.x), MathUtil.round(d2.y));


        //g2.drawLine(MathUtil.round(centerX), MathUtil.round(centerY), MathUtil.round(d.x), MathUtil.round(d.y));



        g2.dispose();

        File outputfile = new File("D:\\tmp\\tree.png");
        try {
            ImageIO.write(image, "png", outputfile);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
