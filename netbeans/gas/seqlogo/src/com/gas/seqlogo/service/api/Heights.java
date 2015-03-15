/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.seqlogo.service.api;

import java.util.*;

/**
 *
 * @author dq
 */
public class Heights {

    private Integer pos;
    private Set<Entry> entries = new HashSet<Entry>();

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }
    
    public void put(String name, Double bits){
        entries.add(new Entry(name, bits));
    }
    
    public List<Entry> getSortedEntries(EntrySorter sorter){
        List<Entry> ret = new ArrayList<Entry>(entries);
        Collections.sort(ret, sorter);
        return ret;
    }

    public Set<Entry> getEntries() {
        return entries;
    }

    public void setEntries(Set<Entry> entries) {
        this.entries = entries;
    }
    
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(pos);
        List<Entry> tmp = new ArrayList<Entry>(entries);
        Collections.sort(tmp, new EntrySorter(false));
        Iterator<Entry> itr = tmp.iterator();
        while(itr.hasNext()){
            Entry entry = itr.next();
            ret.append(entry);
            if(itr.hasNext()){
                ret.append(',');
            }
        }
        return ret.toString();
    }
    
    public static class Entry {
        
        private String name;
        private Double bits;

        public Entry(){}
        
        public Entry(String name, Double bits){
            this.name = name;
            this.bits = bits;
        }
        
        public Double getBits() {
            return bits;
        }

        public void setBits(Double bits) {
            this.bits = bits;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
        
        @Override
        public String toString(){
            StringBuilder ret = new StringBuilder();
            ret.append('[');
            ret.append(name);
            ret.append(',');
            ret.append(bits);
            ret.append(']');
            return ret.toString();
        }
        
    }
    
    public static class EntrySorter implements Comparator<Entry> {

        private boolean ascending ;
        
        public EntrySorter(){
            this(true);
        }
        
        public EntrySorter(boolean ascending){
            this.ascending = ascending;
        }
        
        @Override
        public int compare(Entry o1, Entry o2) {
            int ret = o1.getBits().compareTo(o2.getBits());
            if(!ascending){
                ret = ret * -1;
            }
            return ret;
        }
        
    }
}
