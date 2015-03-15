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
public class ZappoColorProvider implements IColorProvider, IComboItem {

    private static Map<String, Color> colors = new HashMap<String, Color>();

    static {
        int size = Math.min(ColorProviderCnsts.aa.length, ColorProviderCnsts.zappo.length);
        for (int i = 0; i < size; i++) {
            String key = ColorProviderCnsts.aa[i];
            Color color = ColorProviderCnsts.zappo[i];
            colors.put(key, color);
        }
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
        return "Zappo";
    }

    @Override
    public String getDisplayName() {
        return "Zappo";
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
}
