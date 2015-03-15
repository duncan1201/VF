/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import com.gas.common.ui.util.ColorCnst;
import java.awt.Color;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IColorProvider.class)
public class MacCladeDNAColorFGProvider extends MacCladeDNAColorProvider {

    public MacCladeDNAColorFGProvider() {
        colors.put("A", new Color(222, 8, 0));
        colors.put("C", new Color(16, 255, 99));
        colors.put("G", ColorCnst.GOLDENROD);
        colors.put("T", new Color(0, 173, 239));
        colors.put("U", new Color(0, 173, 239));
    }

    @Override
    public String getName() {
        return "MacClade2";
    }

    @Override
    public String getDisplayName() {
        return "";
    }

    @Override
    public boolean isBackgroundColor() {
        return false;
    }
}
