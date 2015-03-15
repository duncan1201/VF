/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.shotgun;

import com.gas.common.ui.misc.Loc;
import com.gas.domain.core.tasm.Condig;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dq
 */
public class Coverage {

    private Integer hibernateId;
    public Set<Element> elements = new HashSet<Element>();
    private transient Integer maxCount;
    private transient Integer length;

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Coverage() {
    }

    public Coverage(Condig condig) {
    }

    public Integer getMaxCount() {
        if (maxCount == null) {
            Iterator<Element> itr = elements.iterator();
            while (itr.hasNext()) {
                Element element = itr.next();
                if (maxCount == null || maxCount < element.getCount()) {
                    maxCount = element.getCount();
                }
            }
        }
        return maxCount;
    }

    public Integer getLength() {
        if (length == null) {
            Iterator<Element> itr = elements.iterator();
            while (itr.hasNext()) {
                Element element = itr.next();
                if (length == null || length < element.getTo()) {
                    length = element.getTo();
                }
            }
        }
        return length;
    }

    public void resetLength() {
        length = null;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Set<Element> getElements() {
        return elements;
    }

    public List<Element> getSortedElements() {
        List<Element> ret = new ArrayList<Element>(elements);
        Collections.sort(ret, new Sorter());
        return ret;
    }

    public void setElements(Set<Element> elements) {
        this.elements = elements;
    }

    public static class Element {

        private Integer hibernateId;
        private Integer from;
        private Integer to;
        private Integer count;
        private Boolean forward;
        private transient Loc loc;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Loc getLoc() {
            if (loc == null) {
                loc = new Loc(from, to);
            }
            return loc;
        }

        public void resetLoc() {
            loc = null;
        }

        public Boolean getForward() {
            return forward;
        }

        public void setForward(Boolean forward) {
            this.forward = forward;
        }

        public Integer getFrom() {
            return from;
        }

        public void setFrom(Integer from) {
            this.from = from;
        }

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Integer getTo() {
            return to;
        }

        public void setTo(Integer to) {
            this.to = to;
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();
            ret.append(from);
            if (forward != null && forward) {
                ret.append('-');
                ret.append('>');
            } else if (forward != null && !forward) {
                ret.append('<');
                ret.append('-');
            } else if (forward == null) {
                ret.append('-');
            }
            ret.append(to);
            ret.append(':');
            ret.append(count);
            return ret.toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        Iterator<Element> itr = getSortedElements().iterator();
        while (itr.hasNext()) {
            Element e = itr.next();
            String s = e.toString();
            ret.append(s);
            if (itr.hasNext()) {
                ret.append(',');
            }
        }
        return ret.toString();
    }

    public static class Sorter implements Comparator<Element> {

        @Override
        public int compare(Element o1, Element o2) {
            int ret = 0;
            ret = o1.getFrom().compareTo(o2.getFrom());
            return ret;
        }
    }
}
