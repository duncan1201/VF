/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.ref;

import com.gas.common.ui.util.FontUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.Reference;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.VerticalLayout;

/**
 *
 * @author dunqiang
 */
public class Wrapper extends JPanel implements Scrollable {

    public Wrapper() {
        super(new VerticalLayout());
    }

    public void setReferences(Set<Reference> refs) {
        removeAll();
        Iterator<Reference> refItr = refs.iterator();
        while (refItr.hasNext()) {
            Reference ref = refItr.next();
            RefPanel panel = new RefPanel();
            panel.setTitle(ref.getTitle());
            panel.setRemarks(ref.getRemark());
            panel.setPublisher(ref.getLocation());
            panel.setPmid(ref.getAccession());
            add(panel);
            
            if(refItr.hasNext()){
                JXTitledSeparator s = new JXTitledSeparator("");
                add(s);
            }
        }
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
        int height = this.getPreferredSize().height;
        JScrollPane scrollPane = UIUtil.getParent(this, JScrollPane.class);
        int scrollBarWidth = scrollPane.getVerticalScrollBar().getSize().width;
        Dimension ret = new Dimension();
        ret.height = height;
        ret.width = scrollPane.getViewport().getSize().width - scrollBarWidth;
        return ret;
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        Font font = FontUtil.getDefaultFont();
        int height = FontUtil.getFontMetrics(font).getHeight();
        return height;
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return getScrollableUnitIncrement(visibleRect, orientation, direction) * 2;
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
        return true;
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
}
