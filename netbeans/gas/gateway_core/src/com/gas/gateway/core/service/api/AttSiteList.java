/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.service.api;

import com.gas.common.ui.misc.Loc;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author dq
 */
public class AttSiteList extends ArrayList<AttSite>{

    //private List<AttSite> attSites = new ArrayList<AttSite>();

    public AttSiteList() {
    }

    public AttSiteList(List<AttSite> attSites) {
        this.clear();
        this.addAll(attSites);
    }

    public List<AttSite> getAttSites(Character clazz) {
        List<AttSite> ret = new ArrayList<AttSite>();
        for (AttSite attSite : this) {
            if (clazz.toString().equalsIgnoreCase(attSite.getClazz().toString())) {
                ret.add(attSite);
            }
        }
        return ret;
    }

    public AttSite get(String name) {
        AttSite ret = null;
        for (AttSite attSite : this) {
            if (attSite.getName().equalsIgnoreCase(name)) {
                ret = attSite;
            }
        }
        return ret;
    }

    public boolean contains(String name) {
        boolean ret = false;
        for (AttSite attSite : this) {
            if (attSite.getName().equalsIgnoreCase(name)) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * @return in the form of "B1-B2"
     */
    public String getShortNames(){
        return getShortNames(false);
    }
    
    /**
     * @param reverse
     */
    public String getShortNames(boolean reverse) {        
        return getNames(false, reverse);
    }

    /**
     * 
     */
    public String getNames(boolean includeLoc, boolean reverse) {
        StringBuilder ret = new StringBuilder();
        Collections.sort(this, new AttSite.LocSorter(reverse));
        Iterator<AttSite> itr = iterator();
        while (itr.hasNext()) {
            AttSite site = itr.next();
            String shortName = site.getShortName();
            ret.append(shortName);
            if (includeLoc) {
                ret.append('(');
                ret.append(site.getLoc().toString());
                ret.append(')');
            }
            if (itr.hasNext()) {
                ret.append('-');
            }
        }
        return ret.toString();
    }
    
    public void setAttSites(List<AttSite> attSites) {
        clear();
        addAll(attSites);        
    }

    public AttSite first() {
        AttSite ret = null;

        for (AttSite attSite : this) {
            Loc loc = attSite.getLoc();
            if (ret == null) {
                ret = attSite;
                continue;
            }
            if (ret.getLoc().getEnd() > loc.getEnd()) {
                ret = attSite;
            }
        }
        return ret;
    }

    public AttSite last() {
        AttSite ret = null;

        for (AttSite attSite : this) {
            Loc loc = attSite.getLoc();
            if (ret == null) {
                ret = attSite;
                continue;
            }
            if (ret.getLoc().getEnd() < loc.getEnd()) {
                ret = attSite;
            }
        }
        return ret;
    }
    
    public String toHtmlList(){
        StringBuilder ret = new StringBuilder();
        ret.append("<ul>");
        for(AttSite site: this){
            ret.append("<li>");
            ret.append(site.getName());
            ret.append("&nbsp;");
            Loc loc = site.getLoc();
            ret.append(loc.getStart());
            ret.append(" bp ");
            ret.append(loc.isStrand()?"":" &lt;");
            ret.append("-");
            ret.append(loc.isStrand()?"&gt; ":"");
            ret.append(loc.getEnd());
            ret.append(" bp ");
            ret.append("</li>");
        }
        ret.append("</ul>");
        return ret.toString();
    }
}
