/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tabbedpane;

import com.gas.common.ui.util.ColorCnst;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

/**
 *
 * @author dq
 */
public class JTabbedPaneFactory {

    public static JTabbedPane create(int tabPosition, final Color selectedTab) {

        JTabbedPane ret = new JTabbedPane(tabPosition);

        ret.setOpaque(true);
        CrossPlatformTabbedPaneUI ui = new CrossPlatformTabbedPaneUI();
        ui.setSelectedColor(ColorCnst.AIR_FORCE_BLUE);
        ui.setShadow(ColorCnst.AIR_FORCE_BLUE);
        ui.setDarkShadow(ColorCnst.AIR_FORCE_BLUE);
        ui.setContentBorderInsets(new Insets(0, 0, 0, 2));
        ui.setTabAreaInsets(new Insets(22, 0, 0, 0));
        //ret.setUI(ui);


        return ret;
    }
}
