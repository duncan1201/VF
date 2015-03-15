/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import static com.gas.common.ui.color.ClustalDNAColorProvider.NAME;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IColorProvider.class)
public class ClustalDNAColorFGProvider extends ClustalDNAColorProvider {

    @Override
    public String getDisplayName() {
        return "";
    }

    @Override
    public boolean isBackgroundColor() {
        return false;
    }

    @Override
    public String getName() {
        return "Clustal-fg";
    }
}
