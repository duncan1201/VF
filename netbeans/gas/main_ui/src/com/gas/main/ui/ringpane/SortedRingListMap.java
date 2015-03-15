/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.Feture;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.*;

/**
 *
 * @author dq
 */
public class SortedRingListMap extends LinkedHashMap<String, RingList> {
    
    public SortedRingListMap() {    
    }
    
    public void paint(Graphics2D g2d){
        Iterator<String> keyItr = keySet().iterator();
        while(keyItr.hasNext()){
            String key = keyItr.next();
            RingList ringList = get(key);
            ringList.paint(g2d);
        }
    }
    
    public List getAllData(){
        List ret = new ArrayList();
        Iterator<String> itr = keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            RingList ringList = get(key);
            ret.addAll(ringList.getAllData());
        }
        return ret;
    }
    
    public void create(AnnotatedSeq as){
        RingListMap ringListMap = new RingListMap();
        ringListMap.createRings(as);
        
        create(ringListMap);
    }
    
    public void setStartOffset(float startOffset){
        Iterator<RingList> itr = values().iterator();
        while(itr.hasNext()){
            RingList list = itr.next();
            list.setStartOffset(startOffset);
        }
    }
    
    public void setSeedColor(String k, Color color){
        Iterator<String> keyItr = keySet().iterator();
        while(keyItr.hasNext()){
            String key = keyItr.next();
            if(key.equalsIgnoreCase(k)){
                get(key).setSeedColor(color);
                break;
            }
        }
    }
    
    protected Ring getRing(Point pt){
        Ring ret = null;
        Iterator<String> itr = keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            RingList ringList = get(key);
            Ring ring = ringList.getRing(pt);
            if(ring != null){
                ret = ring;
                break;
            }
        }
        return ret;    
    }
    
    protected Ring updateSelection(Feture feture){
        Ring ret = null;
        Iterator<String> itr = keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            RingList ringList = get(key);
            Ring updated = ringList.updateSelection(feture);
            if(updated != null){
                ret = updated;
            }
        }
        return ret;  
    }
    
    public void updateMouseIn(Point pt){
        boolean success = false;
        Iterator<String> itr = keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            RingList ringList = get(key);
            if(!success){
                success = ringList.updateMouseIn(pt);
            }else{
                ringList.setMouseIn(false);
            }
        }
    }
    
    protected Ring updateSelection(Point pt){
        Ring ret = null;
        Iterator<String> itr = keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            RingList ringList = get(key);
            Ring updated = ringList.updateSelection(pt);
            if(updated != null){
                ret = updated;
            }
        }
        return ret;
    }

    protected synchronized void create(RingListMap ringListMap) {
        clear();
        List<RingGraphPanel.FetureMetaData> mds = new ArrayList<RingGraphPanel.FetureMetaData>();
        Iterator<String> itr = ringListMap.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            Iterator<Ring> ringItr = ringListMap.get(key).iterator();
            RingGraphPanel.FetureMetaData md = new RingGraphPanel.FetureMetaData();
            md.name = key;

            while (ringItr.hasNext()) {
                Ring ring = ringItr.next();
                Feture feture = (Feture) ring.getData();
                md.totalPos += feture.getLucation().width();
                md.featureCount++;
            }

            mds.add(md);
        }
        Collections.sort(mds, new FetureMetaDataComparator());

        Iterator<RingGraphPanel.FetureMetaData> mdItr = mds.iterator();
        while (mdItr.hasNext()) {
            RingGraphPanel.FetureMetaData md = mdItr.next();
            put(md.name, ringListMap.get(md.name));
        }
    }
    
    public SortedRingListMap getByRings(Collection<Ring> rings){
        SortedRingListMap ret = new SortedRingListMap();
        Iterator<String> itr = keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            RingList ringList = get(key);
            RingList filtered = ringList.getByRings(rings);
            if(!filtered.isEmpty()){
                ret.put(key, filtered);
            }
        }
        return ret;
    }
    
    public SortedRingListMap getByLocation(int start, int end){
        SortedRingListMap ret = new SortedRingListMap();
        Iterator<String> itr = keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            RingList ringList = get(key);
            RingList filtered = ringList.getByLocation(start, end);
            if(!filtered.isEmpty()){
                ret.put(key, filtered);
            }
        }
        return ret;
    }        
    
    public SortedRingListMap getByKey(String _key){
        SortedRingListMap ret = new SortedRingListMap();
        Iterator<String> itr = keySet().iterator();
        while(itr.hasNext()){
            String key = itr.next();
            RingList ringList = get(key);
            if(key.equalsIgnoreCase(_key)){
                ret.put(key, ringList);
            }            
        }
        return ret;
    }

    protected static class FetureMetaDataComparator implements Comparator<RingGraphPanel.FetureMetaData> {

        @Override
        public int compare(RingGraphPanel.FetureMetaData o1, RingGraphPanel.FetureMetaData o2) {
            return o1.getAvgFeatureLength().compareTo(o2.getAvgFeatureLength());
        }
    }
}
