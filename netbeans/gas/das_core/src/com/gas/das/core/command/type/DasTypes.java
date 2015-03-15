package com.gas.das.core.command.type;

import java.util.HashSet;
import java.util.Set;

public class DasTypes {

    private DasTypes.Gff gff = new DasTypes.Gff();

    public DasTypes.Gff getGff() {
        return gff;
    }

    public void setGff(DasTypes.Gff gff) {
        this.gff = gff;
    }

    public static class Gff {

        private String href;
        private String version;
        private Set<DasTypes.Segment> segments = new HashSet<DasTypes.Segment>();

        public String getHref() {
            return href;
        }

        public void setHref(String href) {
            this.href = href;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Set<DasTypes.Segment> getSegments() {
            return segments;
        }

        public void setSegments(Set<DasTypes.Segment> segments) {
            this.segments = segments;
        }
    }

    public static class Segment {

        private String version;
        private Set<DasTypes.Type> types = new HashSet<DasTypes.Type>();

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public Set<DasTypes.Type> getTypes() {
            return types;
        }

        public void setTypes(Set<DasTypes.Type> types) {
            this.types = types;
        }
    }

    public static class Type {

        private String id;
        private String category;
        private String method;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }
    }
}
