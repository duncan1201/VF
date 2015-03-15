/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

import static createimages.Create3DProteinsIcon.WIDTH;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author dq
 */
public class Create3DProteinNCBI {

    public static void main(String[] args) throws IOException {
        create();
    }

    public static void create() throws IOException {
        final int WIDTH = 16;
        final int HEIGHT = 16;

        BufferedImage ret = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) ret.getGraphics();

        Util.setRenderingHints(g);
        BufferedImage imageDb = ImageIO.read(Create3DProteinNCBI.class.getResourceAsStream("database_16.png"));

        g.drawImage(imageDb, 0, 0, null);

        BufferedImage image3D = ImageIO.read(Create3DProteinNCBI.class.getResourceAsStream("3D_protein_8.png"));
        g.drawImage(image3D, imageDb.getWidth() - image3D.getWidth(), imageDb.getHeight() - image3D.getHeight(), null);
        
        g.dispose();
        File outputfile = new File("D:\\tmp\\3D_NCBI_8.png");
        try {
            ImageIO.write(ret, "png", outputfile);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
