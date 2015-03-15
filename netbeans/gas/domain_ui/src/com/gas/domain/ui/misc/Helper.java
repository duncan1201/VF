/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.misc;

import com.gas.common.ui.util.ReflectHelper;
import com.gas.domain.core.IFolderElement;
import java.lang.reflect.Method;

/**
 *
 * @author dq
 */
public class Helper {

    public static String getHibernateId(IFolderElement obj) {
        String ret = null;
        if (obj != null) {            
            ret = obj.getHibernateId();
        }
        return ret;
    }
}
