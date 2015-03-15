package com.gas.das.core.command.entrypts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntryPoints {

    private Integer hibernateId; // for hibernate
    private String href;
    private Set<Segment> segments = new HashSet<Segment>();

    public EntryPoints() {
        // for hibernate
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public Set<Segment> getSegments() {
        return segments;
    }

    public void setSegments(Set<Segment> segments) {
        this.segments = segments;
    }

    public static class Segment {

        private Integer hibernateId; // for hibernate
        private String type;
        private String id;
        private Integer start;
        private Integer stop;
        private String orientation;
        private String subparts;
        private Integer index;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getStop() {
            return stop;
        }

        public void setStop(Integer stop) {
            this.stop = stop;
        }

        public String getOrientation() {
            return orientation;
        }

        public void setOrientation(String orientation) {
            this.orientation = orientation;
        }

        public String getSubparts() {
            return subparts;
        }

        public void setSubparts(String subparts) {
            this.subparts = subparts;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }
        // for hibernate

        public Integer getHibernateId() {
            return hibernateId;
        }
        
        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }
    }
}
