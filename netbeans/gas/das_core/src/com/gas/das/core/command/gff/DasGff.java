package com.gas.das.core.command.gff;

import java.util.HashSet;
import java.util.Set;

public class DasGff {

    private Gff gff;

    public Gff getGff() {
        return gff;
    }

    public void setGff(Gff gff) {
        this.gff = gff;
    }

    public static class Gff {

        private String href;
        Set<Segment> segments = new HashSet<Segment>();

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public void setSegments(Set<Segment> segments) {
            this.segments = segments;
        }

        public Set<Segment> getSegments() {
            return segments;
        }
    }

    public static class Segment {

        private String id;
        private Integer start;
        private Integer stop;
        private Set<Feature> features = new HashSet<Feature>();

        public Set<Feature> getFeatures() {
            return features;
        }

        public void setFeatures(Set<Feature> features) {
            this.features = features;
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
    }

    public static class Feature {

        private String id;
        private Integer start;
        private Integer end;
        private String method;
        private String score;
        private String orientation;
        private Type type;
        private Target target;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public Target getTarget() {
            return target;
        }

        public void setTarget(Target target) {
            this.target = target;
        }

        public Integer getStart() {
            return start;
        }

        public void setStart(Integer start) {
            this.start = start;
        }

        public Integer getEnd() {
            return end;
        }

        public void setEnd(Integer end) {
            this.end = end;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getOrientation() {
            return orientation;
        }

        public void setOrientation(String orientation) {
            this.orientation = orientation;
        }
    }

    public static class Target {

        private String id;
        private Integer start;
        private Integer stop;

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
    }

    public static class Type {

        private String id;
        private String reference;
        private String superparts;
        private String subparts;
        private String category;
        private String text;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getSuperparts() {
            return superparts;
        }

        public void setSuperparts(String superparts) {
            this.superparts = superparts;
        }

        public String getSubparts() {
            return subparts;
        }

        public void setSubparts(String subparts) {
            this.subparts = subparts;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
