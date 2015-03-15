/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.as;

import com.gas.common.ui.ruler.RulerLocList;
import com.gas.common.ui.util.CommonUtil;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author dq
 */
public class ParentLocSet implements Cloneable {

    private Integer hibernateId;
    private Set<ParentLoc> parentLocs = new HashSet<ParentLoc>();
    
    @Override
    public ParentLocSet clone(){
        ParentLocSet ret = new ParentLocSet();
        ret.setParentLocs(CommonUtil.copyOf(parentLocs));
        return ret;
    }
    
    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }
    
    public int size(){
        return parentLocs.size();
    }

    public Set<ParentLoc> getParentLocs() {
        return parentLocs;
    }

    public void setParentLocs(Set<ParentLoc> parentLocs) {
        this.parentLocs = parentLocs;
    }
    
    public void add(ParentLoc parentLoc){
        this.parentLocs.add(parentLoc);
    }
    
    public void clear(){
        this.parentLocs.clear();
    }
    
    public Iterator<ParentLoc> iterator(){
        return this.parentLocs.iterator();
    }

    public void touchAll(){
        Iterator<ParentLoc> itr = parentLocs.iterator();
        while(itr.hasNext()){
            itr.next();
        }
    }
    
    public boolean isEmpty(){
        return parentLocs.isEmpty();
    }
    
    public void replace(int start, int end, int replacementLength){
        insertSeq(end + 1,replacementLength);
        removeSeq(start, end);
    }
    
    public void insertSeq(int pos, int length){
        if(length == 0){
            return;
        }
        Iterator<ParentLoc> itr = iterator();
        while(itr.hasNext()){
            ParentLoc loc = itr.next();
            loc.insertSeq(pos, length);
        }
    }
    
    public void removeSeq(int start, int end){
        Iterator<ParentLoc> itr = iterator();
        while(itr.hasNext()){
            ParentLoc loc = itr.next();
            if(loc.isSubsetOf(start, end)){
                itr.remove();
            }else{
                loc.removeSeq(start, end);
            }
            System.out.print("");
        }
        System.out.print("");
    }
    
    public RulerLocList toRulerLocList(){
        RulerLocList ret = new RulerLocList();
        Iterator<ParentLoc> itr = iterator();
        while(itr.hasNext()){
            ParentLoc parentLoc = itr.next();
            ret.add(parentLoc.toRulerLoc());
        }
        return ret;
    }
}
