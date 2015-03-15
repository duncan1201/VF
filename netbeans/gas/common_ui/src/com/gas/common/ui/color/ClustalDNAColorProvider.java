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
public class ClustalDNAColorProvider implements IColorProvider, IComboItem {

    public static final String NAME = "Clustal";
    /*
     Color                   Residue Code
    
     ORANGE                  A
     RED                     C
     BLUE                    T
     GREEN                   G      
    
     */
    private final static Map<String, Color> colors = new HashMap<String, Color>();

    static {
        colors.put("A", ClustalCnsts.ORANGE);
        colors.put("C", ClustalCnsts.RED);
        colors.put("T", ClustalCnsts.BLUE);
        colors.put("U", ClustalCnsts.BLUE);
        colors.put("G", ClustalCnsts.GREEN);
    }

    @Override
    public String getDisplayName() {
        return NAME;
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
        return NAME;
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
}
