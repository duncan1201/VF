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
public class MacCladeDNAColorProvider implements IColorProvider, IComboItem {

    protected final Map<String, Color> colors = new HashMap<String, Color>();

    public MacCladeDNAColorProvider() {
        colors.put("A", new Color(222, 8, 0));
        colors.put("C", new Color(16, 255, 99));
        colors.put("G", new Color(255, 255, 0));
        colors.put("T", new Color(0, 173, 239));
        colors.put("U", new Color(0, 173, 239));
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
        return "MacClade";
    }

    @Override
    public boolean isForProtein() {
        return false;
    }

    @Override
    public boolean isForNucleotide() {
        return true;
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
