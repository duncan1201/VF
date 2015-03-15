/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import javax.swing.Icon;
import org.jdesktop.swingx.renderer.IconValue;

/**
 *
 * @author dq
 */
public class TypeIconValue implements IconValue {

    @Override
    public Icon getIcon(Object value) {
        String str = (String) value;
        Icon ret = null;
        if (str != null && !str.isEmpty()) {
            ret = ImageHelper.createImageIcon(str);
        } else {
            ret = ImageHelper.createImageIcon(ImageNames.EMPTY_16);
        }
        return ret;
    }
}
