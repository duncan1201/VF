/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.domain.core.ren.RMap;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author dq
 */
public class CurvedBracketList extends ArrayList<CurvedBracket>{    
    
    public void paint(Graphics2D g){
        Iterator<CurvedBracket> itr = iterator();
        while(itr.hasNext()){
            CurvedBracket b = itr.next();
            b.paint2(g);
        }
    }
    
    public CurvedBracket updateSelection(Point pt){
        CurvedBracket ret = null;
        for(int i = 0; i < size(); i++){
            CurvedBracket b = get(i);
            boolean within = b.isWithin(pt);
            if(within){
                ret = b;
            }
            b.setSelected(within);            
        }
        return ret;
    }
    
    public CurvedBracket getCurvedBracket(Point pt){
        CurvedBracket ret = null;
        Iterator<CurvedBracket> itr = iterator();
        while(itr.hasNext()){
            CurvedBracket b = itr.next();
            boolean within = b.isWithin(pt);
            if(within){
                ret = b;
                break;
            }
        }
        return ret;
    }
    
    public RMap.EntryList getSelectedEntries(){
        RMap.EntryList ret = new RMap.EntryList();
        CurvedBracketList selected = getSelected();
        Iterator<CurvedBracket> itr = selected.iterator();
        while(itr.hasNext()){
            CurvedBracket cb = itr.next();
            ret.add((RMap.Entry)cb.getData());
        }
        return ret;
    }
    
    private CurvedBracketList getSelected(){
        CurvedBracketList ret = new CurvedBracketList();
        Iterator<CurvedBracket> itr = iterator();
        while(itr.hasNext()){
            CurvedBracket cb = itr.next();
            if(cb.isSelected()){
                ret.add(cb);
            }
        }
        return ret;
    }
    
    public void setStartOffset(float offset){
        for(int i = 0; i < size(); i++){
            get(i).setStartOffset(offset);
        }
    }
    
    public void setTotalPos(int pos){
        for(int i = 0; i < size(); i++){
            get(i).setTotalPos(pos);
        }
    }
    
}
