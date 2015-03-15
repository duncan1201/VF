/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IColorProvider.class)
public class HydroColorFGProvider extends HydroColorProvider {

    @Override
    public String getName() {
        return "Hydrophobicity2";
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
