/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.color;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ColorProviderFetcher {

    public enum TYPE {

        DNA, PROTEIN
    };

    public static <T extends Collection> T getColorProviders(TYPE type, Class<T> retType) {
        T ret = null;
        List<IColorProvider> retList = new ArrayList<IColorProvider>();
        for (IColorProvider colorProvider : Lookup.getDefault().lookupAll(IColorProvider.class)) {
            boolean ok = true;
            if (type == TYPE.PROTEIN) {
                if (colorProvider.isForProtein()) {
                    ok = true;
                } else {
                    ok = false;
                }
            }

            if (type == TYPE.DNA) {
                if (colorProvider.isForNucleotide()) {
                    ok = true;
                } else {
                    ok = false;
                }
            }

            if (ok) {
                retList.add(colorProvider);
            }
        }

        if (retType.isAssignableFrom(List.class)) {
            ret = (T) retList;
        } else if (retType.isAssignableFrom(Vector.class)) {
            Vector<IColorProvider> vec = new Vector<IColorProvider>(retList);
            ret = (T) vec;
        } else {
            throw new IllegalArgumentException(String.format("'%s' not supported", retType.getName()));
        }
        return ret;
    }

    public static IColorProvider getColorProvider(String name) {
        IColorProvider ret = null;
        for (IColorProvider colorProvider : Lookup.getDefault().lookupAll(IColorProvider.class)) {
            if (name.equals(colorProvider.getName())) {
                ret = colorProvider;
                break;
            }
        }
        return ret;
    }
}
