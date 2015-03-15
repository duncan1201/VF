/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import com.gas.common.ui.combo.ImgComboRenderer.IComboItem;
import com.gas.common.ui.util.ColorCnst;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.RNATools;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IColorProvider.class)
public class ChromaColorProvider implements IColorProvider, IComboItem {

    protected final static String NAME = "Chromatogram";
    private final static Map<String, Color> colors = new HashMap<String, Color>();

    static {
        colors.put(DNATools.a().getName(), Color.red);
        colors.put(DNATools.c().getName(), Color.blue);
        colors.put(DNATools.g().getName(), ColorCnst.GOLDENROD);
        colors.put(DNATools.t().getName(), ColorCnst.PAKISTAN_GREEN);
        colors.put(RNATools.u().getName(), ColorCnst.PAKISTAN_GREEN);
        colors.put(DNATools.n().getName(), Color.black);
    }

    @Override
    public Color getColor(Character c) {
        return getColor(c.toString());
    }

    @Override
    public Color getColor(String s) {
        Color ret = null;
        if (s.equalsIgnoreCase("a") || s.equals(DNATools.a().getName())) {
            ret = colors.get(DNATools.a().getName());
        } else if (s.equalsIgnoreCase("t") || s.equals(DNATools.t().getName())) {
            ret = colors.get(DNATools.t().getName());
        } else if (s.equalsIgnoreCase("u") || s.equals(RNATools.u().getName())) {
            ret = colors.get(RNATools.u().getName());
        } else if (s.equalsIgnoreCase("c") || s.equals(DNATools.c().getName())) {
            ret = colors.get(DNATools.c().getName());
        } else if (s.equalsIgnoreCase("g") || s.equals(DNATools.g().getName())) {
            ret = colors.get(DNATools.g().getName());
        }
        return ret;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDisplayName() {
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
