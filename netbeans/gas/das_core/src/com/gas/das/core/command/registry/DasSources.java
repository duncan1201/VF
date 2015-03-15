package com.gas.das.core.command.registry;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DasSources {

    private Integer hibernateId; // for hibernate only
    private Date updatedDate;
    private Set<Source> sources = new HashSet<Source>();

    public DasSources() {
        updatedDate = new Date();
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Set<Source> getSources() {
        return sources;
    }

    public void setSources(Set<Source> sources) {
        this.sources = sources;
    }

    // for hibernate only
    public Integer getHibernateId() {
        return hibernateId;
    }

    // for hibernate only
    public void setHibernateId(Integer id) {
        this.hibernateId = id;
    }

    public static class Source {

        private Integer hibernateId;
        private String uri;
        private String title;
        private String docHref;
        private String desc;
        private Set<Version> versions = new HashSet<Version>();

        public Integer getHibernateId() {
            return hibernateId;
        }

        public void setHibernateId(Integer hibernateId) {
            this.hibernateId = hibernateId;
        }

        public Set<Version> getVersions() {
            return versions;
        }

        public void setVersions(Set<Version> versions) {
            this.versions = versions;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDocHref() {
            return docHref;
        }

        public void setDocHref(String docHref) {
            this.docHref = docHref;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class Version {

        private String uri;
        private Map<String, String> properties = new HashMap<String, String>();
        private List<Capability> capabilities = new ArrayList<Capability>();
        private List<Coordinates> coordinates = new ArrayList<Coordinates>();

        public Map<String, String> getProperties() {
            return properties;
        }

        public void setProperties(Map<String, String> properties) {
            this.properties = properties;
        }

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public List<Capability> getCapabilities() {
            return capabilities;
        }

        public void setCapabilities(List<Capability> capabilities) {
            this.capabilities = capabilities;
        }

        public List<Coordinates> getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(List<Coordinates> coordinates) {
            this.coordinates = coordinates;
        }
    }

    public static class Coordinates {

        private String uri;
        private String source;
        private String authority;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getAuthority() {
            return authority;
        }

        public void setAuthority(String authority) {
            this.authority = authority;
        }
    }

    public static class Capability {

        private String type;
        private String queryURI;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getQueryURI() {
            return queryURI;
        }

        public void setQueryURI(String queryURI) {
            this.queryURI = queryURI;
        }
    }
}
