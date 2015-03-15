/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package createimages;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/**
 *
 * @author dq
 */
public class Util {
    public static void setRenderingHints(Graphics2D g){
        RenderingHints hints = new RenderingHints(null);
        int n = 0;
        hints.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        hints.put(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        hints.put(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        hints.put(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        hints.put(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_NORMALIZE);
        hints.put(RenderingHints.KEY_DITHERING,
                RenderingHints.VALUE_DITHER_ENABLE);
        g.setRenderingHints(hints);    
    }
}
