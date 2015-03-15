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
public class RasMolColorFGProvider extends RasMolColorProvider {

    @Override
    public String getName() {
        return "RasMol2";
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
