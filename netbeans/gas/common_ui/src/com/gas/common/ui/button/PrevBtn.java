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
public class PrevBtn extends FlatBtn {

    public PrevBtn() {
        super(ImageHelper.createImageIcon(ImageNames.BR_PREV_16));
    }

    public void goPrivate() {
        setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
        setEnabled(false);
    }

    public void goPublic() {
        setIcon(ImageHelper.createImageIcon(ImageNames.BR_PREV_16));
        setEnabled(true);
    }
}
