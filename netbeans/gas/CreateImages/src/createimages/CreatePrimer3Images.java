/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author dq
 */
public class CreatePrimer3Images {
    
    public static void main(String[] args) throws IOException{
        BufferedImage image = ImageIO.read(CreateImages.class.getResourceAsStream("primer3_params.png"));
        BufferedImage ret = ImageHelper.chop(image, 1060, 108, 1359, 704);
        
        File outputfile = new File("D:\\tmp\\primer3_params_2.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }        
}
