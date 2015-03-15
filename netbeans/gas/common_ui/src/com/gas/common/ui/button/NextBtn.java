/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.button;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import javax.swing.ImageIcon;

/**
 *
 * @author dq
 */
public class NextBtn extends FlatBtn {

    public NextBtn() {
        super(new ImageIcon(ImageHelper.createImage(ImageNames.BR_NEXT_16)));
    }
}
