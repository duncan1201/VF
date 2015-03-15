/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author dq
 */
public class CreateDeletePrimerImage {
    
    public static void main(String[] args) throws IOException{
        int WIDTH = 16;
        int HEIGHT = 16;
        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = ret.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        BufferedImage image = ImageIO.read(CreateImages.class.getResourceAsStream("primer_16.png"));
        int x = 0;
        int y = 0;        
        g.drawImage(image, Math.round(x), Math.round(y), null);
        
        //Shape circle = null;
        //g.draw(circle);
        g.setColor(new Color(204, 58, 31));
        g.fillOval(3, 3, 7, 7);
        
        g.setColor(Color.white);
        g.fillRect(3, 4, 7, 2);
        
        
        g.dispose();
        File outputfile = new File("D:\\tmp\\primer_delete_16.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
