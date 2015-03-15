/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane;

import com.gas.domain.ui.editor.TopBracket;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.domain.ui.editor.as.IASEditor;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.SwingConstants;

/**
 *
 * @author dq
 */
class ResPanelLayout implements LayoutManager {

    int y = 0;
    private IMolPane molPane;
    private GraphPanel graphPanel;

    @Override
    public void addLayoutComponent(String name, Component comp) {
    }

    @Override
    public void removeLayoutComponent(Component comp) {
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Dimension ret = new Dimension();        
        return ret;
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        Dimension ret = new Dimension();
        return ret;
    }

    @Override
    public void layoutContainer(Container parent) {
        if (molPane == null) {
            molPane = UIUtil.getParent(parent, IMolPane.class);
        }
        if (graphPanel == null) {
            graphPanel = UIUtil.getParent(parent, GraphPanel.class);
        }
        ResPanel resPanel = (ResPanel)parent;
        Dimension size = parent.getPreferredSize();
        if (size.width <= 0 || graphPanel == null || molPane == null) {
            return;
        }
        Insets insets = resPanel.getInsets();
        y = 0;
        Map<TopBracket, Rectangle> rects = new HashMap<TopBracket, Rectangle>();

        int totalPos = molPane.getAs().getLength();
        LocList locList = new LocList();
        
        Integer height = null;
        List<TopBracket> topBrackets = resPanel.getTopBrackets();

        Iterator<TopBracket> itr = topBrackets.iterator();
        while (itr.hasNext()) {
            TopBracket topBracket = itr.next();
            if(height == null){
                height = topBracket.getDisiredHeight();
            }
            int startPos = topBracket.getStartPos();
            int endPos = topBracket.getEndPos();

            boolean intersect = locList.intersect(startPos, endPos) != null;
            if (intersect) {
                y += height;
                locList.clear();
            }
            
            locList.add(new Loc(startPos, endPos));            

            int xStart = UIUtil.toScreen(totalPos, startPos, size.width - insets.left - insets.right, insets.left, SwingConstants.LEFT, Integer.class);
            int width = UIUtil.toScreenWidth(size.width - insets.left - insets.right, totalPos, endPos - startPos + 1, Integer.class);
            rects.put(topBracket, new Rectangle(xStart, y, width, height));            
        }

        if (!topBrackets.isEmpty()) {
            y += height;
        }

        itr = rects.keySet().iterator();
        while (itr.hasNext()) {
            TopBracket tb = itr.next();
            Rectangle rect = rects.get(tb);
            rect.y = y - rect.y - rect.height;
            tb.setBounds(rect);
        }

        UIUtil.setPreferredHeight(parent, y);
    }
}