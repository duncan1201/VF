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
import java.awt.RenderingHints;
import java.awt.font.TextAttribute;
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
public class CreateLinkedinLogo {
    
    public static void main(String[] args) throws IOException{
        //create();
        //test2();
        test3();
    }
    
    interface MyInterface{
        void hello() throws IOException;
    }
    
    class MyClass {
        public void hello() {
            throw new RuntimeException();
        }
    }
    
    public static void test3(){
        int i = Integer.MAX_VALUE + 1;
        System.out.println(i);
        System.out.println(Integer.MIN_VALUE);
    }
    
    public static void test2(){        
        String abc = "abc";
        String abc2 = "abc";
        String abc3 = new String("abc");
        if(abc == abc2){
            System.out.println("abc == abc2");
        }
        if(abc == abc3){
            System.out.println("abc == abc3");
        }
        
        class A {
            final static int a = 0;
        }
    }
    
    private class Inner {
        final static int a = 1;
    }
    
    public static void test(){
        String[] table = {"ab"};
        int ii = 0;
        do while(ii < table.length)
            System.out.println(ii++);
        while(ii < table.length);
    }
    
    public static void create() throws IOException {
        final int WIDTH = 149;
        final int HEIGHT = 101;
        final int ARC_WIDTH = 9;
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = ret.createGraphics();
        Util.setRenderingHints(g2);
        
        // fill the background
        Point2D start = new Point2D.Float(0, 0);
        Point2D end = new Point2D.Float(0, HEIGHT - 1);
        float[] fractions = new float[]{0, 1.0f};
        Color[] colors = {new Color(0, 34, 0), new Color(0, 17, 0)};
        LinearGradientPaint paint = new LinearGradientPaint(start, end, fractions, colors);
        g2.setPaint(paint);
        //g2.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_WIDTH);

        // 
        BufferedImage image = ImageIO.read(CreateImages.class.getResourceAsStream("DNA1_flipped_32.png"));
        g2.drawImage(image, 6, 13, null);

        Font font = g2.getFont().deriveFont(g2.getFont().getSize2D() * 2.8f).deriveFont(Font.BOLD).deriveFont(Font.ITALIC);
        Map<AttributedCharacterIterator.Attribute, Object> attributes = new HashMap<AttributedCharacterIterator.Attribute, Object>();
        attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD * 1.5f);
        font = font.deriveFont(attributes);
        g2.setFont(font);
        FontMetrics fm = g2.getFontMetrics();
        final String V = "Vector";
        final String F = "Friends";
        int fontHeight = fm.getHeight();
        int ascent = fm.getAscent();
        int strWidth = fm.stringWidth(V);


        float x = (WIDTH - strWidth - image.getWidth()) * 0.5f + image.getWidth();
        float y = fontHeight;
        g2.setColor(Color.red);
        g2.drawString(V, x, y);


        font = g2.getFont().deriveFont(g2.getFont().getSize2D() * 1.1f);
        g2.setFont(font);
        fm = g2.getFontMetrics();
        strWidth = fm.stringWidth(F);
        x = (WIDTH - strWidth) * 0.5f;
        y += ascent;
        g2.setColor(new Color(17, 17, 255));
        g2.drawString(F, x, y);

        g2.dispose();

        File outputfile = new File("D:\\tmp\\logo_softonic.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }    
}
