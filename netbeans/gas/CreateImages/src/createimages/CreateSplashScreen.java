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
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author dq
 */
public class CreateSplashScreen {
    
    public static void main(String[] args) throws IOException{
        create();
    }
    
    static Color V = new Color(207, 39, 36);
    static Color V_SHADOW = new Color(251, 100, 100);
    
    public static void create() throws IOException {

        final String c4_flipped_crop = "c4-flipped-cropped.png";
        BufferedImage image = ImageIO.read(CreateImages.class.getResourceAsStream(c4_flipped_crop));
        int beforeWidth = image.getWidth();
        image = ImageHelper.chop(image, 0, 0, image.getWidth() - 1, image.getHeight() - 30);
        int afterWidth = image.getWidth();
        final int WIDTH = image.getWidth();
        final int HEIGHT = image.getHeight();
        final BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) ret.getGraphics();
        Util.setRenderingHints(g2d);
        g2d.drawImage(image, 0, 0, null);

        Map<AttributedCharacterIterator.Attribute, Object> attributes = new HashMap<AttributedCharacterIterator.Attribute, Object>();
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD);

        //drawOverlay(g2d, WIDTH, HEIGHT);

        drawVF(g2d, WIDTH, HEIGHT);

        drawMiscText(g2d, WIDTH, HEIGHT);

        g2d.dispose();

        File outputfile = new File("D:\\tmp\\splash_screen.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }   
    

    private static void drawOverlay(Graphics2D g2d, int width, int height) {
        Color c = new Color(0.4f, 0.4f, 0.4f, 0.4f);
        g2d.setColor(c);
        g2d.fillRect(0, 0, width, height);;
    }

    private static void drawVF(Graphics2D g2d, int WIDTH, int height) {
        Map<AttributedCharacterIterator.Attribute, Object> attributes = new HashMap<AttributedCharacterIterator.Attribute, Object>();
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD);

        Font font = g2d.getFont().deriveFont(25f).deriveFont(Font.ITALIC).deriveFont(attributes);
        g2d.setFont(font);
        final FontMetrics fm = g2d.getFontMetrics();
        final int ascent = fm.getAscent();
        final float fontHeight = fm.getHeight();

        String VECTOR = "Vector";
        GlyphVector glyphvector = font.createGlyphVector(g2d.getFontRenderContext(), VECTOR);
        GeneralPath path = new GeneralPath();
        GeneralPath shadow;
        path.append(glyphvector.getOutline(), false);
        float x = fm.stringWidth("A");
        float y = height * 0.23f + fontHeight * 0.5f;
        int strWidth = fm.stringWidth(VECTOR);
        Point2D start = new Point2D.Float(0, y - ascent);
        Point2D end = new Point2D.Float(0, y + (fontHeight - ascent));
        final float[] fractions = {0, 0.5f, 1.0f};
        Color[] colors = new Color[]{V, V, V};
        Paint paint = new LinearGradientPaint(start, end, fractions, colors);
        g2d.setPaint(paint);
        path = PathUtil.transform(AffineTransform.getTranslateInstance(x, y), path);
        shadow = PathUtil.transform(AffineTransform.getTranslateInstance(1, 1), path);
        g2d.setColor(V_SHADOW);
        g2d.fill(shadow);
        g2d.setPaint(paint);
        g2d.fill(path);
        x += strWidth;

        final String FRIENDS = "Friends";
        path = new GeneralPath();
        glyphvector = font.createGlyphVector(g2d.getFontRenderContext(), FRIENDS);
        path.append(glyphvector.getOutline(), false);
        path = PathUtil.transform(AffineTransform.getTranslateInstance(x, y), path);
        colors = new Color[]{new Color(60, 60, 215), new Color(60, 60, 235), new Color(60, 60, 215)};
        paint = new LinearGradientPaint(start, end, fractions, colors);
        shadow = PathUtil.transform(AffineTransform.getTranslateInstance(1, 1), path);
        g2d.setColor(new Color(140, 140, 255));
        g2d.fill(shadow);
        g2d.setPaint(paint);
        g2d.fill(path);
        strWidth = fm.stringWidth(FRIENDS);
        x += strWidth;

        final String VERSION = "1.2";
        path = new GeneralPath();
        glyphvector = font.createGlyphVector(g2d.getFontRenderContext(), VERSION);
        path.append(glyphvector.getOutline(), false);
        path = PathUtil.transform(AffineTransform.getTranslateInstance(x, y), path);
        colors = new Color[]{new Color(180, 180, 180), new Color(180, 180, 180), new Color(180, 180, 180)};
        paint = new LinearGradientPaint(start, end, fractions, colors);
        shadow = PathUtil.transform(AffineTransform.getTranslateInstance(1, 1), path);
        g2d.setColor(new Color(250, 250, 250));
        g2d.fill(shadow);
        g2d.setPaint(paint);
        g2d.fill(path);
    }

    private static void drawMiscText(Graphics2D g, int width, int height) {
        String text = String.format("Copyright %s2012 BioFriends(Pte Ltd). All rights reserved", Unicodes.COPYRIGHT);
        Font font = g.getFont().deriveFont(Font.PLAIN).deriveFont(9f);
        g.setFont(font);
        final FontMetrics fm = g.getFontMetrics();
        final int fontHeight = fm.getHeight();
        final int ascent = fm.getAscent();
        int strWidth = fm.stringWidth(text);
        g.setColor(new Color(200, 200, 200));
        g.drawString(text, width - strWidth - 4, height - (fontHeight - ascent) - 3);
    }    
}
