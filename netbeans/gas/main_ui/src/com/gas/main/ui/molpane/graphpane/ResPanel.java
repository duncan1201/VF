/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane;

import com.gas.domain.ui.editor.TopBracket;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.editor.TopBracketList;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class ResPanel extends JPanel {

    private List<TopBracket> topBrackets = new ArrayList<TopBracket>();

    ResPanel() {
        LayoutManager layout = new ResPanelLayout();
        setLayout(layout);
    }

    TopBracket getTopBracketByPtOnScreen(Point ptOnScreen) {
        Point pt = UIUtil.convertPointFromScreen(ptOnScreen, this);
        return getTopBracket(pt);
    }

    public TopBracket getTopBracket(Point pt) {
        TopBracket ret = null;
        Iterator<TopBracket> itr = topBrackets.iterator();
        while (itr.hasNext()) {
            TopBracket tp = itr.next();
            Rectangle bounds = tp.getBounds();
            if (bounds.contains(pt)) {
                ret = tp;
                break;
            }
        }
        return ret;
    }

    public void setRmap(RMap rmap) {
        refresh();
    }

    public void clearSelection() {
        Component[] comps = getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof TopBracket) {
                TopBracket tp = (TopBracket) comps[i];
                tp.setSelected(false);                
            }
        }
    }

    public void selectExclusively(TopBracket topBracket) {
        Component[] comps = getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof TopBracket) {
                TopBracket tp = (TopBracket) comps[i];
                if (comps[i] == topBracket) {
                    tp.setSelected(true);
                } else {
                    tp.setSelected(false);
                }
            }
        }
    }

    protected List<TopBracket> reinitTopBrackets() {
        removeAll();
        topBrackets.clear();
        return getTopBrackets();
    }

    void refresh() {
        reinitTopBrackets();
    }

    private GraphPanel getGraphPanel() {
        return UIUtil.getParent(this, GraphPanel.class);
    }

    protected List<TopBracket> getTopBrackets() {
        GraphPanel gp = getGraphPanel();
        if (topBrackets.isEmpty() && gp != null && gp.getAs() != null && gp.getAs().getRmap() != null) {
            //Map<Loc, TopBracket> maps = new HashMap<Loc, TopBracket>();
            Iterator<RMap.Entry> itr = gp.getAs().getRmap().getEntriesIterator();

            while (itr.hasNext()) {
                RMap.Entry entry = itr.next();

                TopBracket b = new TopBracket();
                b.setStartPos(entry.getStart());
                b.setEndPos(entry.getEnd());
                b.setText(entry.getName());
                b.setData(entry);
                topBrackets.add(b);
            }

            topBrackets = TopBracket.compact(topBrackets);
            Iterator<TopBracket> itrTB = topBrackets.iterator();
            while (itrTB.hasNext()) {
                TopBracket topBracket = itrTB.next();
                add(topBracket);
            }
        }
        return topBrackets;
    }
    
    public void selectExclusively(RMap.EntryList entries) {
        Component[] comps = getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof TopBracket) {
                TopBracket tp = (TopBracket) comps[i];
                RMap.Entry entry = entries.getEntry(tp.getText(), tp.getStartPos(), tp.getEndPos());
                if (entry == null) {
                    tp.setSelected(false);
                } else {
                    tp.setSelected(true);
                }
            }
        }    
    }
    
    public TopBracketList getSelectedTopBrackets() {
        TopBracketList ret = new TopBracketList();
        Component[] comps = getComponents();
        for (int i = 0; i < comps.length; i++) {
            if (comps[i] instanceof TopBracket) {
                TopBracket tp = (TopBracket) comps[i];
                if (tp.isSelected()) {
                    ret.add(tp);
                }
            }
        }
        return ret;
    }
}
