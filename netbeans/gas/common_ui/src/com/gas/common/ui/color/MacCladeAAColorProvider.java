/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import com.gas.common.ui.combo.ImgComboRenderer.IComboItem;
import java.awt.Color;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.swing.ImageIcon;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IColorProvider.class)
public class MacCladeAAColorProvider implements IColorProvider, IComboItem {

    private final static Map<String, Color> colors = new HashMap<String, Color>();

    static {
        colors.put("A", new Color(0, 173, 239));
        colors.put("C", new Color(255, 171, 255));
        colors.put("D", new Color(174, 168, 118));
        colors.put("E", new Color(191, 185, 136));
        colors.put("F", new Color(148, 182, 255));
        colors.put("G", new Color(227, 6, 176));
        colors.put("H", new Color(185, 20, 255));
        colors.put("I", new Color(79, 155, 255));
        colors.put("K", new Color(167, 13, 227));
        colors.put("L", new Color(0, 18, 184));
        colors.put("M", new Color(94, 181, 181));
        colors.put("N", new Color(255, 0, 12));
        colors.put("P", new Color(20, 184, 110));
        colors.put("Q", new Color(194, 1, 5));
        colors.put("R", new Color(214, 138, 255));
        colors.put("S", new Color(255, 223, 10));
        colors.put("T", new Color(176, 21, 26));
        colors.put("V", new Color(21, 255, 17));
        colors.put("W", new Color(192, 255, 156));
        colors.put("Y", new Color(235, 144, 134));
        colors.put("*", Color.BLACK);
    }

    @Override
    public Color getColor(String s) {
        Color ret = null;
        s = s.toUpperCase(Locale.ENGLISH);
        boolean present = colors.containsKey(s);
        if (present) {
            ret = colors.get(s);
        }
        return ret;
    }

    @Override
    public Color getColor(Character c) {
        return getColor(c.toString());
    }

    @Override
    public String getName() {
        return "MacCladeAA";
    }

    @Override
    public String toString() {
        return "MacClade";
    }

    @Override
    public boolean isForProtein() {
        return true;
    }

    @Override
    public boolean isForNucleotide() {
        return false;
    }

    @Override
    public boolean isBackgroundColor() {
        return true;
    }

    @Override
    public ImageIcon getImageIcon() {
        return ImageIconCreator.createImageIcon(this);
    }

    @Override
    public String getDisplayName() {
        return "MacClade";
    }
}
