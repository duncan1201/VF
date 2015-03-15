/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages.commanderPro;

import createimages.CreateImages;
import createimages.PathUtil;
import createimages.Util;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.font.TextAttribute;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.AttributedCharacterIterator;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author dq
 */
public class CreateButton {
    
    public static void main(String[] args){
                final int WIDTH = 20;
        final int HEIGHT = 20;
        final int ARC_WIDTH = 5;
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = ret.createGraphics();
        Util.setRenderingHints(g2);
        
        GeneralPath path = new GeneralPath();
        path.moveTo(ARC_WIDTH, 0);
        path.lineTo(WIDTH - ARC_WIDTH, 0);
        path.quadTo(WIDTH, 0, WIDTH - 1, ARC_WIDTH);
        path.lineTo(WIDTH - 1, HEIGHT - 1 - ARC_WIDTH);
        path.quadTo(WIDTH, HEIGHT - 1, WIDTH - ARC_WIDTH, HEIGHT - 1);
        
        path.lineTo(ARC_WIDTH, HEIGHT - 1);
        path.quadTo(0, HEIGHT - 1, 0, HEIGHT - 1 - ARC_WIDTH);
        
        path.lineTo(0, ARC_WIDTH);
        path.quadTo(0, 0, PathUtil.getFirstPoint(path).getX(), PathUtil.getFirstPoint(path).getY());
        
        path.closePath();
        
        // fill the background
        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float(WIDTH - 1, 0);
        float[] fractions = new float[]{0, 0.4f, 1.0f};
        Color[] colors = {new Color(231, 231, 231), new Color(231, 231, 231), new Color(192, 192, 192)};
        LinearGradientPaint paint = new LinearGradientPaint(start, end, fractions, colors);
        g2.setPaint(paint);
        g2.fill(path);
        g2.setColor(new Color(150,150,150));
        g2.draw(path);
       
        g2.dispose();

        File outputfile = new File("D:\\tmp\\commanderPro\\commander_pro_button.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
