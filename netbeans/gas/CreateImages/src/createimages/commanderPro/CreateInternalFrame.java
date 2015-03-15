/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages.commanderPro;

import createimages.ColorUtil;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author dq
 */
public class CreateInternalFrame {
    
    final static Color[] colors = new Color[]{new Color(141,141,141), new Color(130,130,130), new Color(102,102,102), new Color(88,88,88)};
    
    static Color[] selected_colors ;
    
    static{
        selected_colors = new Color[colors.length];
        for(int i = 0; i < colors.length; i++){
            selected_colors[i] = ColorUtil.changeSaturation(colors[i], 1.1f);
        }
    }
    
    public static void main(String[] args){
        internalFrameTitlePaneBackground();
        internalFrameTitlePaneBackground_selected();
        internalFrameBorder();
        internalFrameBorder_selected();
    }
    
    
    
private static void internalFrameBorder_selected(){
    final int WIDTH = 20;
        final int HEIGHT = 20;        
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)ret.getGraphics();        
        
        g.setPaint(new Color(10,10,10));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.dispose();
        File outputfile = new File("D:\\tmp\\commanderPro\\commander_pro_internalFrameBorder_selected.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }            
    }            
            
    private static void internalFrameBorder(){
    final int WIDTH = 20;
        final int HEIGHT = 20;        
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)ret.getGraphics();        
        
        g.setPaint(new Color(88,88,88));
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.dispose();
        File outputfile = new File("D:\\tmp\\commanderPro\\commander_pro_internalFrameBorder.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }            
    }
    
    private static void internalFrameTitlePaneBackground(){

        final int WIDTH = 66;
        final int HEIGHT = 18;        
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)ret.getGraphics();        
        float[] fractions = new float[]{0, 0.1f, 0.8f, 1f};
        
        LinearGradientPaint paint = new LinearGradientPaint(0, 0, 0, HEIGHT - 1, fractions, colors);
        g.setPaint(paint);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.dispose();
        File outputfile = new File("D:\\tmp\\commanderPro\\commander_pro_internalFrameTitlePaneBackground.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
    }
    
    private static void internalFrameTitlePaneBackground_selected(){

        final int WIDTH = 66;
        final int HEIGHT = 18;        
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D)ret.getGraphics();        
        float[] fractions = new float[]{0, 0.1f, 0.8f, 1f};
        
        LinearGradientPaint paint = new LinearGradientPaint(0, 0, 0, HEIGHT - 1, fractions, selected_colors);
        g.setPaint(paint);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.dispose();
        File outputfile = new File("D:\\tmp\\commanderPro\\commander_pro_internalFrameTitlePaneBackground_selected.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }        
    }
}
