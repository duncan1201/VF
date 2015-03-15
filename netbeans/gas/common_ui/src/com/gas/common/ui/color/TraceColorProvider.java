/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import static com.gas.common.ui.color.ChromaColorProvider.NAME;
import com.gas.common.ui.util.ColorCnst;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import org.biojava.bio.seq.DNATools;
import org.biojava.bio.seq.RNATools;

/**
 *
 * @author dq
 */
public class TraceColorProvider implements IColorProvider{
    
    protected final static String NAME = "Trace";
    private final static Map<String, Color> colors = new HashMap<String, Color>();

    static {
        colors.put(DNATools.a().getName(), ColorCnst.PAKISTAN_GREEN);
        colors.put(DNATools.c().getName(), Color.blue);
        colors.put(DNATools.g().getName(), Color.BLACK);
        colors.put(DNATools.t().getName(), Color.RED);
        colors.put(RNATools.u().getName(), Color.RED);
        colors.put(DNATools.n().getName(), Color.BLACK);
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
    
}
