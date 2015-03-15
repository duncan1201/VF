/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.accordian2;

import com.gas.common.ui.IReclaimable;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.ColorCnst;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

/**
 *
 * @author dq
 */
public class OutlookPane extends JPanel implements IReclaimable {

    JSplitPane splitPane;
    BottomPanel bottomPanel;
    TopPanel topPanel;
    Map<String, IOutlookPanel> contentsMap = new LinkedHashMap<String, IOutlookPanel>();
    Map<String, Icon> iconsMap = new HashMap<String, Icon>();
    static Icon defaultExpandedIcon = ImageHelper.createImageIcon(ImageNames.ARROW_EXPANDED_16);
    static Icon defaultCollapsedIcon = ImageHelper.createImageIcon(ImageNames.ARROW_COLLAPSED_16);
    Color selectedColor = new Color(255, 245, 130);
    Color color = ColorCnst.BABY_BLUE;
    Color contentBgColor;

    public OutlookPane() {
        setLayout(new BorderLayout());
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setOneTouchExpandable(true);

        topPanel = new TopPanel();
        splitPane.setTopComponent(topPanel);

        bottomPanel = new BottomPanel();
        splitPane.setBottomComponent(bottomPanel);

        add(splitPane, BorderLayout.CENTER);

        hookupListeners();
    }

    public void setContentBgColor(Color color) {
        this.contentBgColor = color;
    }

    public void setHorizontalScrollBarPolicy(int policy) {
        topPanel.scrollPaneRef.get().setHorizontalScrollBarPolicy(policy);
    }

    @Override
    public void cleanup() {
        contentsMap.clear();
        iconsMap.clear();
    }

    void hookupListeners() {
        addComponentListener(new OutlookPaneListeners.CompAdpt());
        splitPane.addPropertyChangeListener(new OutlookPaneListeners.PtyListener(this));
    }

    public void addBar(String barName, Icon icon, IOutlookPanel panel) {
        contentsMap.put(barName, panel);
        iconsMap.put(barName, icon);
        BottomPanel bPanel = (BottomPanel) splitPane.getBottomComponent();
        bPanel.addBar(barName);
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public Color getColor() {
        return color;
    }

    public void show(String name) {
        topPanel.setBarName(name);
        bottomPanel.setSelected(name);
    }

    public void showNext(String name) {
        String nameNext = getNext(name);
        if (nameNext != null) {
            show(nameNext);
        }
    }

    String getNext(String name) {
        String ret = null;
        Iterator<String> itr = contentsMap.keySet().iterator();
        while (itr.hasNext()) {
            String cur = itr.next();
            if (cur.equals(name)) {
                if (itr.hasNext()) {
                    ret = itr.next();
                    break;
                }
            }
        }
        if (ret == null) {
            itr = contentsMap.keySet().iterator();
            if (itr.hasNext()) {
                ret = itr.next();
            }
        }
        return ret;
    }
}
