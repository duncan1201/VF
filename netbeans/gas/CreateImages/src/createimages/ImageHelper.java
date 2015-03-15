/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 *
 * @author dunqiang
 */
public class ImageHelper {
    
    /**
     * Copy the source image into destination image at the specified coordinate
     *
     * @param src the source image
     * @param dest the destination image
     * @param pt the coordinate of the destination image
     */
    public static void copyImage(Image src, Image dest, Point pt) {
        Graphics g = dest.getGraphics();
        g.drawImage(src, pt.x, pt.y, null);
        g.dispose();
    }
    
    public static BufferedImage addBorder(BufferedImage image, Color borderColor){
        int width = image.getWidth();
        int height = image.getHeight();
        Graphics2D g2d = (Graphics2D)image.getGraphics();
        g2d.setColor(borderColor);
        g2d.drawLine(0, 0, 0, height);
        g2d.drawLine(0, 0, width, 0);
        g2d.drawLine(0, height-1, width, height-1);
        g2d.drawLine(width - 1, 0, width - 1, height);
        g2d.dispose();
        return image;
    }

    public static BufferedImage chop(BufferedImage image, int x1, int y1, int x2, int y2) {
        BufferedImage ret = new BufferedImage(Math.abs(x2 - x1) + 1, Math.abs(y2 - y1) + 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) ret.getGraphics();
        g2d.drawImage(image, 0, 0, ret.getWidth(), ret.getHeight(), x1, y1, x2, y2, null);
        g2d.dispose();
        return ret;
    }

}
