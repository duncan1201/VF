/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.image;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.openide.modules.InstalledFileLocator;
import org.openide.util.Exceptions;

/**
 *
 * @author dunqiang
 */
public class ImageHelper {

    private final static Map<String, URL> fileURLs = new HashMap<String, URL>();

    static {

        File file = null;
        try {
            file = InstalledFileLocator.getDefault().locate("modules/ext/images", "com.gas.common.ui", false);
        } catch (NoClassDefFoundError error) { // for running without NetBeans platform
            file = new File("D:\\unfuddle_gas_svn\\netbeans\\gas\\common_ui\\release\\modules\\ext\\images");
        } finally { // for running without NetBeans platform
            if (file == null) {
                file = new File("D:\\unfuddle_gas_svn\\netbeans\\gas\\common_ui\\release\\modules\\ext\\images");
            }
        }
        File[] files = file.listFiles();
        for (File f : files) {
            String name = f.getName();
            try {
                URL url = f.toURI().toURL();
                fileURLs.put(name, url);
            } catch (MalformedURLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
    
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

    public static BufferedImage chop(BufferedImage image, int x1, int y1, int x2, int y2) {
        BufferedImage ret = new BufferedImage(Math.abs(x2 - x1), Math.abs(y2 - y1), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) ret.getGraphics();
        g2d.drawImage(image, 0, 0, ret.getWidth() - 1, ret.getHeight() - 1, x1, y1, x2, y2, null);
        g2d.dispose();
        return ret;
    }

    public static Image createImage(String name) {
        //java.net.URL imgURL = clazz.getResource(path);
        java.net.URL imgURL = fileURLs.get(name);
        Image ret = null;
        if (imgURL == null) {
            imgURL = fileURLs.get(ImageNames.SMILE_16);
        }
        try {
            ret = ImageIO.read(imgURL);
        } catch (IOException ex) {
            Logger.getLogger(ImageHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static ImageIcon createImageIcon(String name) {
        return createImageIcon(name, null, null, "");
    }

    public static ImageIcon createImageIcon(String path, Integer width, Integer height) {
        return createImageIcon(path, width, height, "");
    }

    public static ImageIcon createImageIcon(String name, Integer width, Integer height,
            String description) {

        java.net.URL imgURL = fileURLs.get(name);
        ImageIcon ret = null;
        if (imgURL == null) {
            imgURL = fileURLs.get(ImageNames.SMILE_16);
        }
        try {
            Image image = ImageIO.read(imgURL);

            if (width != null && height != null) {
                image = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
                ret = new ImageIcon(image, description);
            } else {
                ret = new ImageIcon(imgURL);
            }

        } catch (IOException ex) {
            Logger.getLogger(ImageHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    public static ImageIcon createImageIcon(Image image) {
        return createImageIcon(image, "");
    }

    public static ImageIcon createImageIcon(Image image, String desc) {
        ImageIcon ret = null;
        ret = new ImageIcon(image, desc);
        return ret;
    }
}
