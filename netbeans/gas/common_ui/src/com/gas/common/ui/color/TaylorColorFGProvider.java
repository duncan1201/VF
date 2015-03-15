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
public class TaylorColorFGProvider extends TaylorColorProvider {

    @Override
    public boolean isBackgroundColor() {
        return false;
    }

    @Override
    public String getName() {
        return "Taylor2";
    }

    @Override
    public String getDisplayName() {
        return "";
    }
}
