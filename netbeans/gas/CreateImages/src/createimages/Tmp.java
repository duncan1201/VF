/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author dq
 */
public class Tmp {

    public static void main(String[] args) throws FileNotFoundException, IOException{
        FileInputStream inputStream = new FileInputStream(new File("D:\\tmp\\tree_thumbnail.png"));
        BufferedImage image = ImageIO.read(inputStream);
        BufferedImage ret = ImageHelper.addBorder(image, Color.GRAY);

        File outputfile = new File("D:\\tmp\\tree_thumbnail_b.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }    
    }
    
    public static void main2(String[] args) throws FileNotFoundException, IOException {
        FileInputStream inputStream = new FileInputStream(new File("D:\\tmp\\tree2.png"));
        BufferedImage image = ImageIO.read(inputStream);
        int x1 = 5;
        int y1 = 20;
        int WIDTH = 424;
        int HEIGHT = 172;
        int x2 = x1 + WIDTH - 1;
        int y2 = y1 + HEIGHT - 1;
        BufferedImage ret = ImageHelper.chop(image, x1, y1, x2, y2);

        File outputfile = new File("D:\\tmp\\tree2_thumbnail.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
