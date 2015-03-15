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
public class CreateAboutImage {
        public static void create() throws IOException {
        final int WIDTH = 221;
        final int HEIGHT = 38;
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = ret.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Map<AttributedCharacterIterator.Attribute, Object> attributes = new HashMap<AttributedCharacterIterator.Attribute, Object>();
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD);
        Font font = g.getFont().deriveFont(Font.ITALIC).deriveFont(attributes).deriveFont(24f);
        g.setFont(font);
        final FontMetrics FM = g.getFontMetrics();
        final int ASCENT = FM.getAscent();
        final int FONT_HEIGHT = FM.getHeight();

        float x = 0;
        float y;
        BufferedImage image = ImageIO.read(CreateImages.class.getResourceAsStream("DNA1_flipped_24.png"));
        y = HEIGHT * 0.5f - image.getHeight() * 0.5f;
        g.drawImage(image, Math.round(x), Math.round(y), null);

        x += image.getWidth();
        y = HEIGHT * 0.5f + (ASCENT - FONT_HEIGHT * 0.5f);
        String VECTOR = "Vector";
        GlyphVector glyphvector = font.createGlyphVector(g.getFontRenderContext(), VECTOR);
        GeneralPath path = new GeneralPath();
        GeneralPath shadow;
        path.append(glyphvector.getOutline(), false);
        path = PathUtil.transform(AffineTransform.getTranslateInstance(x, y), path);
        shadow = PathUtil.transform(AffineTransform.getTranslateInstance(2, 2), path);
        g.setPaint(Color.LIGHT_GRAY);
        g.fill(shadow);
        Point2D start = new Point2D.Float(x, y - ASCENT);
        Point2D end = new Point2D.Float(x, y + FONT_HEIGHT - ASCENT);
        float[] fractions = new float[]{0, 1f};
        Color[] colors = new Color[]{new Color(80,0,0), new Color(255, 0, 0)};
        Paint paint = new LinearGradientPaint(start, end, fractions, colors);
        g.setPaint(paint);
        g.fill(path);
        int strWidth = FM.stringWidth(VECTOR);
        x += strWidth;

        String FRIENDS = "Friends";
        glyphvector = font.createGlyphVector(g.getFontRenderContext(), FRIENDS);
        path = new GeneralPath();
        path.append(glyphvector.getOutline(), false);
        path = PathUtil.transform(AffineTransform.getTranslateInstance(x, y), path);
        shadow = PathUtil.transform(AffineTransform.getTranslateInstance(2, 2), path);
        g.setPaint(Color.lightGray);
        g.fill(shadow);
        colors = new Color[]{new Color(0, 0, 0), new Color(0, 0, 255)};
        start = new Point2D.Float(x, y - ASCENT);
        end = new Point2D.Float(x, y + FONT_HEIGHT - ASCENT);        
        paint = new LinearGradientPaint(start, end, fractions, colors);
        g.setPaint(paint);
        g.fill(path);
        strWidth = FM.stringWidth(FRIENDS);
        x += strWidth;
        
        String VERSION = "1.1";
        glyphvector = font.createGlyphVector(g.getFontRenderContext(), VERSION);
        path = new GeneralPath();
        path.append(glyphvector.getOutline(), false);
        path = PathUtil.transform(AffineTransform.getTranslateInstance(x, y), path);
        shadow = PathUtil.transform(AffineTransform.getTranslateInstance(2, 2), path);
        g.setPaint(Color.lightGray);
        g.fill(shadow);
        colors = new Color[]{new Color(255, 255, 255), new Color(255, 255, 255)};
        start = new Point2D.Float(x, y - ASCENT);
        end = new Point2D.Float(x, y + FONT_HEIGHT - ASCENT);        
        paint = new LinearGradientPaint(start, end, fractions, colors);
        g.setPaint(paint);
        g.fill(path);        

        g.dispose();
        File outputfile = new File("D:\\tmp\\about_logo.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
