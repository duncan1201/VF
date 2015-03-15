/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import com.gas.common.ui.util.UIUtil;
import com.gas.main.ui.molpane.sitepanel.primer3.InternalOligoTmCalSettingsPanel;
import com.gas.main.ui.molpane.sitepanel.primer3.PrimerTmCalSettingsPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JRadioButton;

/**
 *
 * @author dq
 */
public class Convert2OligoListeners {
    static class OligoTypeListener extends AbstractAction {

        public OligoTypeListener(){
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            JRadioButton src = (JRadioButton)e.getSource();
            Convert2OligoPanel p = UIUtil.getParent(src, Convert2OligoPanel.class);
            String cmd = e.getActionCommand();
            if(cmd.equalsIgnoreCase("probe")){
                p.show(InternalOligoTmCalSettingsPanel.class);
            }else if(cmd.equalsIgnoreCase("forward") || cmd.equalsIgnoreCase("reverse")){
                p.show(PrimerTmCalSettingsPanel.class);
            }
        }
    }
}
