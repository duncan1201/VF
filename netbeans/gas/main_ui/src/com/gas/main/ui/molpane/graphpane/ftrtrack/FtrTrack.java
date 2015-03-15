/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane.ftrtrack;

import com.gas.common.ui.misc.Loc;
import com.gas.domain.ui.shape.IShape;
import com.gas.domain.ui.shape.Arrow;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JComponent;
import com.gas.domain.ui.shape.ArrowComparators;
import java.awt.Component;
import java.awt.Dimension;

/**
 *
 * @author dunqiang
 */
public class FtrTrack extends JComponent {

    private static Logger logger = Logger.getLogger(FtrTrack.class.getName());
    private String trackName;
    private List<IShape> arrows = new ArrayList<IShape>();

    public FtrTrack() {
        super();
        hookupListeners();
    }
    
    private void hookupListeners(){
        addComponentListener(new FtrTrackListeners.CompListener());
    }

    public void setArrowSeedColor(Color c) {
        Iterator<IShape> itr = arrows.iterator();
        while (itr.hasNext()) {
            itr.next().setSeedColor(c);
        }
    }    

    public void setSelection(Loc loc) {
        for (IShape arrow : arrows) {
            Loc aLoc = arrow.getLoc();
            if(loc.isSuperset(aLoc)){
                arrow.setSelected(true);
            }else{
                arrow.setSelected(false);
            }
        }
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public void addArrows(Collection<IShape> _arrows) {
        this.arrows.addAll(_arrows);
        ArrowComparators.StartPosComparator aComp = new ArrowComparators.StartPosComparator();
        /**
         * The z-order determines the order that components are painted. 
         * The component with the highest z-order paints first and the component 
         * with the lowest z-order paints last. Where components overlap, the 
         * component with the lower z-order paints over the component with the 
         * higher z-order.
         */
        Collections.sort(arrows, aComp);
        Iterator<IShape> itr = arrows.iterator();
        while (itr.hasNext()) {
            IShape arrow = itr.next();           
            add((Component)arrow);
        }

    }

    public void unselectAll() {
        Iterator<IShape> itr = arrows.iterator();
        while (itr.hasNext()) {
            IShape arrow = itr.next();
            if (arrow.isSelected()) {
                arrow.setSelected(false);
            }
        }
    }

    public void layoutSelf() {
        invalidate();
        getLayout().layoutContainer(this);
    }

    public List<IShape> getArrows() {
        return arrows;
    }

    @Override
    public void paintComponent(Graphics graphics) {
        Dimension size = getSize();
        if(size.width == 0 || size.height == 0){
            return;
        }
        //Insets insets = getInsets();
        graphics.setColor(getBackground());
        //graphics.fillRect(insets.left, insets.top, getSize().width - insets.left - insets.right, getSize().height - insets.top - insets.bottom);
        graphics.fillRect(0, 0, size.width, size.height);
    }

}
