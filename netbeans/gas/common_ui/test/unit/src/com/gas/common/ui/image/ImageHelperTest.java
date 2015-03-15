/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.image;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
public class ImageHelperTest {

    @Test
    public void testChopImage() throws IOException {
        File[] files = {
            new File("D:\\tmp\\isothermal_start.png"), 
            new File("D:\\tmp\\isothermal_finish.png"),
            //new File("D:\\tmp\\ligr.png"),
            //new File("D:\\tmp\\traces.png"),
        };
        Rectangle2D[] rects = {
            new Rectangle2D.Double(1,1,468,306), 
            new Rectangle2D.Double(1,1,468,306),
            //new Rectangle2D.Double(177,146, 472,307),
            //new Rectangle2D.Double(588,375, 472,307)
        };
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            Rectangle2D rect = rects[i];
            
            String nameFile = file.getName();
            BufferedImage image = ImageIO.read(file);

            int x1 = (int)rect.getMinX();
            int y1 = (int)rect.getMinY();
            int x2 = (int)rect.getMaxX();
            int y2 = (int)rect.getMaxY();
            BufferedImage chop = new BufferedImage((int)rect.getWidth(), (int)rect.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = (Graphics2D) chop.getGraphics();
            g2d.drawImage(image, 0, 0, chop.getWidth() - 1, chop.getHeight() - 1, x1, y1, x2, y2, null);
            g2d.dispose();
            File outputfile = new File("D:\\tmp\\" + "chop_" + nameFile);
            try {
                ImageIO.write(chop, "png", outputfile);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }        
}
