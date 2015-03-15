/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.CommonUtil;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author dq
 */
class BuyAction extends AbstractAction {

    BuyAction(){
        super("Buy VectorFriends", ImageHelper.createImageIcon(ImageNames.CART_16));
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        final String url = "http://www.vectorfriends.com/purchase.html";
        CommonUtil.browse(url);
    }
    
}
