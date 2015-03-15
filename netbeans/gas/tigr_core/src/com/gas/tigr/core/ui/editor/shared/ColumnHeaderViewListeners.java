/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.editor.shared;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.ruler.RulerLoc;
import com.gas.common.ui.ruler.RulerLocList;
import com.gas.common.ui.util.UIUtil;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dq
 */
class ColumnHeaderViewListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            ColumnHeaderView src = (ColumnHeaderView) evt.getSource();
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (pName.equals("seq")) {
                // must set the sequence first, then get the preferred width
                src.concensus.setData(src.seq);
                int desiredWidth = src.getDesiredWidth();
                UIUtil.setPreferredWidth(src, desiredWidth);
                src.ruler.setLocList(new RulerLocList(new RulerLoc(1, src.seq.length(), 0)));
                src.ruler.repaint();

                src.revalidate();
            } else if (pName.equals("baseFont")) {
                src.concensus.setFont(src.baseFont);

                int desiredWidth = src.getDesiredWidth();
                UIUtil.setPreferredWidth(src, desiredWidth);
                src.revalidate();
            } else if (pName.equals("qv")) {
                src.qualityHistogram.setData(src.qv);
            } else if (pName.equals("layoutState")) {
                if(v.toString().equals(CNST.LAYOUT.ENDING.toString())){
                    AssemblyScroll pane = UIUtil.getParent(src, AssemblyScroll.class);
                    if(pane != null){
                        pane.getCornerUI().resetPreferredWidth();
                        pane.getCornerUI().repaint();
                    }
                }
            }
        }
    }
}
