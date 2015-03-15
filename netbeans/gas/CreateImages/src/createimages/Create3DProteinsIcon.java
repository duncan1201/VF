/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author dq
 */
public class Create3DProteinsIcon {

    final static int WIDTH = 10;
    final static int HEIGHT = 10;

    final static Color[] BLUE_COLORS = new Color[]{new Color(120, 120, 255), new Color(0, 0, 55)};
    final static Color[] YELLOW_COLORS = new Color[]{new Color(205, 205, 70), new Color(25, 25, 0)};
    final static Color[] GREEN_COLORS = new Color[]{new Color(70, 195, 70), new Color(0, 24, 0)};  
    final static Color[] GRAY_COLORS = new Color[]{new Color(155, 155, 155), new Color(60, 60, 60)};
    final static Color[] RED_COLORS = new Color[]{new Color(255, 150, 150), new Color(105, 0, 0)};
    
    public static void main(String[] args) {
        create();
    }

    public static void create() {

        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) ret.getGraphics();

        Util.setRenderingHints(g);

        drawMiddleRight(g);
        drawBottomLeft(g);
        drawCenter(g);

        drawTopLeft(g);
        
        g.dispose();
        File outputfile = new File("D:\\tmp\\3D_protein_16_8.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void drawCenter(Graphics2D g) {
        Point2D center;
        int x = Math.round(WIDTH * 0.1f);
        int y = Math.round(WIDTH * 0.1f);
        int width = Math.round(WIDTH * 0.70f);
        int height = Math.round(WIDTH * 0.70f);
        float radius = WIDTH * 0.4f;
        float[] fractions = new float[]{0, 1f};        
        center = new Point2D.Float(x + WIDTH * 0.3f, y + WIDTH * 0.3f);
        Paint p = new RadialGradientPaint(center, radius, fractions, GRAY_COLORS);
        g.setPaint(p);
        g.fillOval(x, y, width, height);
    }

    private static void drawTopLeft(Graphics2D g) {
        int x = Math.round(WIDTH * 0.0f);
        int y = Math.round(WIDTH * 0.0f);
        int width = Math.round(WIDTH * 0.45f);
        int height = Math.round(WIDTH * 0.45f);
        float radius = WIDTH * 0.4f;
        float[] fractions = new float[]{0f, 1f};
        Point2D center = new Point2D.Float(x + width * 0.3f, y + height * 0.3f);        
        Paint p = new RadialGradientPaint(center, radius, fractions, RED_COLORS);
        g.setPaint(p);
        g.fillOval(x, y, width, height);
    }

    private static void drawMiddleRight(Graphics2D g) {
        int x = Math.round(WIDTH * 0.67f);
        int y = Math.round(HEIGHT * 0.4f);
        int width = Math.round(WIDTH * 0.34f);
        int height = Math.round(HEIGHT * 0.34f);
        float radius = WIDTH * 0.4f;
        float[] fractions = new float[]{0f, 0.8f};
        Point2D center = new Point2D.Float(x + width * 0.5f, y + height * 0.5f);        
        Paint p = new RadialGradientPaint(center, radius, fractions, YELLOW_COLORS);
        g.setPaint(p);
        g.fillOval(x, y, width, height);
    }

    private static void drawBottomLeft(Graphics2D g) {
        int x = Math.round(WIDTH * 0.06f);
        int y = Math.round(HEIGHT * 0.66f);
        int width = Math.round(WIDTH * 0.34f);
        int height = Math.round(HEIGHT * 0.34f);
        float radius = WIDTH * 0.4f;
        float[] fractions = new float[]{0, 1};
        Point2D center = new Point2D.Float(x + width * 0.4f, y + height * 0.4f);        
        Paint p = new RadialGradientPaint(center, radius, fractions, BLUE_COLORS);
        g.setPaint(p);
        g.fillOval(x, y, width, height);
    }
}
