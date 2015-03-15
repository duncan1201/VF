/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.statusline;

import org.openide.awt.StatusLineElementProvider;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class StatusLineHelper {

    public static <T> T getStatusLineElementProvider(Class<T> clazz) {
        T ret = null;
        for (StatusLineElementProvider p : Lookup.getDefault().lookupAll(StatusLineElementProvider.class)) {
            if (clazz.isAssignableFrom(p.getClass())) {
                ret = (T) p;
                break;
            }
        }
        return ret;
    }

    //Cursor before base 9. Mouse over base 8(A)
    //Selected x bases from base a to b. Mouse over base 8(A)
    //145bp-252bp (108bp); 1bp(A)
    public static StatusLineCompProvider getDefaultStatusLineCompProvider() {
        return getStatusLineElementProvider(StatusLineCompProvider.class);
    }
}
