/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.misc.Loc;
import com.gas.domain.core.as.Feture;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dq
 */
public class RingList extends ArrayList<Ring> {

    public void paint(Graphics2D g2d) {
        for (int i = 0; i < size(); i++) {
            Ring ring = get(i);
            ring.paint2(g2d);
        }
    }

    public List getAllData() {
        List ret = new ArrayList();
        Iterator<Ring> itr = iterator();
        while (itr.hasNext()) {
            Ring ring = itr.next();
            ret.add(ring.getData());
        }
        return ret;
    }
    
    public RingList getByRings(Collection<Ring> rings){
        RingList ret = new RingList();
        Iterator<Ring> itr = iterator();
        while(itr.hasNext()){
            Ring ring = itr.next();
            if(rings.contains(ring)){
                ret.add(ring);
            }
        }
        return ret;
    }

    public RingList getByLocation(int start, int end) {
        RingList ret = new RingList();
        Iterator<Ring> itr = iterator();
        while (itr.hasNext()) {
            Ring ring = itr.next();
            Feture feture = (Feture) ring.getData();
            Loc loc = feture.getLucation().toLoc();
            if (Math.min(start, end) == loc.getMin() && Math.max(start, end) == loc.getMax()) {
                ret.add(ring);
            }
        }
        return ret;
    }

    public RingList getByKey(String key) {
        RingList ret = new RingList();
        Iterator<Ring> itr = iterator();
        while (itr.hasNext()) {
            Ring ring = itr.next();
            Feture feture = (Feture) ring.getData();
            String _key = feture.getKey();
            if (_key.equalsIgnoreCase(key)) {
                ret.add(ring);
            }
        }
        return ret;
    }

    public void setStartOffset(float startOffset) {
        for (int i = 0; i < size(); i++) {
            Ring ring = get(i);
            ring.setStartOffset(startOffset);
        }
    }

    public Rectangle getLargestBound() {
        Rectangle ret = null;
        for (int i = 0; i < size(); i++) {
            Ring ring = (Ring) get(i);
            Rectangle bounds = ring.getBounds();
            if (ret == null) {
                ret = bounds;
            } else if (ret.getWidth() * ret.getHeight() < bounds.width * bounds.height) {
                ret = bounds;
            }
        }
        return ret;
    }
    
    public void setSeedColor(Color color){
        for(Ring r: this){
            r.setSeedColor(color);
        }
    }

    public Ring getRing(Point pt) {
        Ring ret = null;
        for (int i = 0; i < size(); i++) {
            Ring ring = get(i);
            boolean within = ring.isWithin(pt);
            if (within) {
                ret = ring;
                break;
            }
        }
        return ret;
    }
    
    public void setMouseIn(boolean mouseIn){
        for (int i = 0; i < size(); i++) {
            Ring ring = get(i);
            ring.setMouseIn(mouseIn);
        }
    }
    
    public boolean updateMouseIn(Point pt){
        boolean ret = false;
        for (int i = 0; i < size(); i++) {
            Ring ring = get(i);
            boolean within = ring.isWithin(pt);
            ring.setMouseIn(within);
            if (within) {
                ret = true;
            }
        }
        return ret;
    }

    public Ring updateSelection(Point pt) {
        Ring ret = null;
        for (int i = 0; i < size(); i++) {
            Ring ring = get(i);
            boolean within = ring.isWithin(pt);
            ring.setSelected(within);
            if (within) {
                ret = ring;
            }
        }
        return ret;
    }

    public Ring updateSelection(Feture feture) {
        Ring ret = null;
        for (int i = 0; i < size(); i++) {
            Ring ring = get(i);
            ring.setSelected(ring.getData() == feture);            
        }
        return ret;
    }
}
