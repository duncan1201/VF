/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages.commanderPro;

import createimages.PathUtil;
import createimages.Util;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author dq
 */
public class CreateArrowButton {
    
    public static void main(String[] args){
        createArrowButtonX();
        createArrowButton8x8Down();
        createArrowDown();
        createBlank66x22();
        createBorder();
    }
    
    private static void createArrowButton8x8Down(){

        final int WIDTH = 8;
        final int HEIGHT = 8;        
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = ret.createGraphics();
        Util.setRenderingHints(g2);
        
        GeneralPath path = new GeneralPath();
        path.moveTo(0, 0);
        path.lineTo(4, 0);
        path.lineTo(2, 4);
        path.closePath();
        
        g2.setPaint(Color.BLACK);
        g2.fill(path);
        
        g2.dispose();
        
        File outputfile = new File("D:\\tmp\\commanderPro\\commander_pro_arrowButton8x8Down.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
    }
    
    private static void createArrowDown(){

        final int WIDTH = 16;
        final int HEIGHT = 16;        
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = ret.createGraphics();
        Util.setRenderingHints(g2);
        
        GeneralPath path = new GeneralPath();
        path.moveTo(WIDTH/2 - 2, HEIGHT/2);
        path.lineTo(WIDTH/2 + 2, HEIGHT/2);
        path.lineTo(WIDTH/2, HEIGHT/2 + 4);
        path.closePath();
        
        g2.setPaint(Color.BLACK);
        g2.fill(path);
        
        g2.dispose();
        
        File outputfile = new File("D:\\tmp\\commanderPro\\commander_pro_arrowDown.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
    }
    

    private static void createBlank66x22(){

        final int WIDTH = 66;
        final int HEIGHT = 22;        
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)ret.getGraphics();        
        g.setColor(new Color(255, 255, 255, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.dispose();
        File outputfile = new File("D:\\tmp\\commanderPro\\blank_66x22.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
    }
    
private static void createBorder(){

        final int WIDTH = 66;
        final int HEIGHT = 22;        
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)ret.getGraphics();        
        g.setColor(new Color(255, 255, 255));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.dispose();
        File outputfile = new File("D:\\tmp\\commanderPro\\commander_pro_border.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
    }    
    
    private static void createArrowButtonX(){

        final int WIDTH = 16;
        final int HEIGHT = 16;        
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)ret.getGraphics();        
        g.setColor(new Color(245, 246, 246, 0));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.dispose();
        File outputfile = new File("D:\\tmp\\commanderPro\\commander_pro_arrowButtonX.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
    }
}
