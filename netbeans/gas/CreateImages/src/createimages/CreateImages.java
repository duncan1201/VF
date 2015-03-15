/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.font.GlyphVector;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;
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

/**
 *
 * @author dq
 */
public class CreateImages {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //CreateSplashScreen.create();
        //CreateLinkedinLogo.create();
        CreateAboutImage.create();
    }

    public static void createAssembly() {
        Color colorF1 = new Color(0, 0, 185);
        Color colorF1Dark = ColorUtil.changeSB(colorF1, 1.4f, 0.65f);

        Color colorF2 = new Color(186, 0, 0);
        Color colorF2Dark = ColorUtil.changeSB(colorF2, 1.4f, 0.65f);

        Color colorF3 = new Color(0, 186, 0);
        Color colorF3Dark = ColorUtil.changeSB(colorF3, 1.4f, 0.65f);

        int[] widths = {16, 24, 32};
        for (int width : widths) {
            BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();

            double widthBar = width * 0.4;
            int heightTop = Math.round(width * 0.12f);
            if (heightTop % 2 != 0) {
                heightTop += 1;
            }
            int height = Math.round((width - 7) * 0.24f);
            if (height % 2 != 0) {
                height += 1;
            }

            double x;
            double y;
            Rectangle2D top = new Rectangle2D.Double(0, 0, width, heightTop);
            Rectangle2D topUp = new Rectangle2D.Double(top.getX(), top.getY(), top.getWidth(), top.getHeight() * 0.5);

            y = top.getMaxY() + 2;
            Rectangle2D first = new Rectangle2D.Double(0, y, widthBar, height);
            Rectangle2D firstUp = new Rectangle2D.Double(first.getX(), first.getY(), first.getWidth(), first.getHeight() * 0.5);

            x = width * 0.08;
            y = first.getMaxY() + 2;
            Rectangle2D second = new Rectangle2D.Double(x, y, width - x * 1.2, height);
            Rectangle2D secondUp = new Rectangle2D.Double(second.getX(), second.getY(), second.getWidth(), second.getHeight() * 0.5);

            x = width * 0.6;
            y = second.getMaxY() + 2;
            Rectangle2D third = new Rectangle2D.Double(x, y, width - x, height);
            Rectangle2D thirdUp = new Rectangle2D.Double(third.getX(), third.getY(), third.getWidth(), third.getHeight() * 0.5);

            g2.setColor(Color.DARK_GRAY);
            g2.fill(top);
            g2.setColor(Color.GRAY);
            g2.fill(topUp);

            g2.setColor(colorF1Dark);
            g2.fill(first);
            g2.setColor(colorF1);
            g2.fill(firstUp);

            g2.setColor(colorF2Dark);
            g2.fill(second);
            g2.setColor(colorF2);
            g2.fill(secondUp);

            g2.setColor(colorF3Dark);
            g2.fill(third);
            g2.setColor(colorF3);
            g2.fill(thirdUp);

            File outputfile = new File("D:\\tmp\\sanger_" + width + ".png");
            try {
                ImageIO.write(image, "png", outputfile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void createTracesNoOverlap() {
        final Color blue = new Color(0, 0, 255);
        final Color red = new Color(255, 0, 0);
        final Color green = new Color(0, 102, 0);
        int[] widths = {16, 24, 36};
        for (int width : widths) {

            BufferedImage image = new BufferedImage(width, width, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = image.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Arc2D first = new Arc2D.Float();
            first.setArc(width * 0, width * 0.2, width * 0.32, width * 1.5, 0, 180, Arc2D.OPEN);

            Arc2D second = new Arc2D.Float();
            second.setArc(first.getMaxX(), width * 0.1, width * 0.32, width * 1.7, 0, 180, Arc2D.OPEN);

            Arc2D third = new Arc2D.Float();
            double x = second.getMaxX();
            third.setArc(x, width * 0.2, second.getWidth(), width * 1.5, 0, 180, Arc2D.OPEN);

            g2.setColor(green);
            g2.draw(third);

            g2.setColor(blue);
            g2.draw(first);

            g2.setColor(red);
            g2.draw(second);


            g2.setColor(Color.DARK_GRAY);
            double maxY = first.getMaxY();
            Line2D line = new Line2D.Double(first.getMinX(), first.getMaxY(), third.getMaxX(), third.getMaxY());
            g2.draw(line);

            g2.dispose();

            File outputfile = new File("D:\\tmp\\traces_" + width + ".png");
            try {
                ImageIO.write(image, "png", outputfile);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
