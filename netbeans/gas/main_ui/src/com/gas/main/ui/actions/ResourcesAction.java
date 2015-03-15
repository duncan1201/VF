/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.actions;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.CommonUtil;
import java.awt.Component;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import org.openide.util.actions.Presenter;

public class ResourcesAction extends AbstractAction implements Presenter.Toolbar {

    public ResourcesAction(){
        super("Resources");
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String url = "http://www.vectorfriends.com/resources.html";
        CommonUtil.browse(url);
    }

    @Override
    public Component getToolbarPresenter() {
        JButton ret = new JButton();       
        ret.setAction(this);
        ret.setVerticalTextPosition(SwingConstants.BOTTOM);
        ret.setHorizontalTextPosition(SwingConstants.CENTER);        
        return ret;
    }
    
}
