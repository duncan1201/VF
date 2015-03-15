/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.EInfo.api;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author dunqiang
 */
public class EInfoResult {

    private List<String> dbList = new ArrayList<String>();
    private String dbName;
    private String menuName;
    private String desc;
    private int count;
    private Date lastUpdate;
    
    private List<Field> fieldList = new ArrayList<Field>();
    private List<Link> linkList = new ArrayList<Link>();

    public List<Field> getFieldList() {
        return fieldList;
    }
    
    public List<Field> getDateField(){
        List<Field> ret = new ArrayList<Field>();
        for(Field f: fieldList){
            if(f.isDate()){
                ret.add(f);
            }
        }
        return ret;
    }

    public void setFieldList(List<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public List<Link> getLinkList() {
        return linkList;
    }

    public void setLinkList(List<Link> linkList) {
        this.linkList = linkList;
    }
    
    public List<String> getDbList() {
        return dbList;
    }

    public void setDbList(List<String> dbList) {
        this.dbList = dbList;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public static class Field {

        private String name;
        private String fullName;
        private String desc;
        private boolean date;
        private Integer termCount;
        private Boolean numerical;
        private Boolean singleToken;
        private Boolean hierarchy;
        private Boolean hidden;

        public Boolean isDate() {
            return date;
        }

        public void setDate(Boolean date) {
            this.date = date;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public Boolean isHidden() {
            return hidden;
        }

        public void setHidden(Boolean hidden) {
            this.hidden = hidden;
        }

        public Boolean isHierarchy() {
            return hierarchy;
        }

        public void setHierarchy(Boolean hierarchy) {
            this.hierarchy = hierarchy;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Boolean isNumerical() {
            return numerical;
        }

        public void setNumerical(Boolean numerical) {
            this.numerical = numerical;
        }

        public Boolean isSingleToken() {
            return singleToken;
        }

        public void setSingleToken(Boolean singleToken) {
            this.singleToken = singleToken;
        }

        public Integer getTermCount() {
            return termCount;
        }

        public void setTermCount(Integer termCount) {
            this.termCount = termCount;
        }
        
        @Override
        public String toString(){
            return getFullName();
        }
    }

    public static class Link {

        private String name;
        private String menu;
        private String desc;
        private String dbTo;

        public String getDbTo() {
            return dbTo;
        }

        public void setDbTo(String dbTo) {
            this.dbTo = dbTo;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getMenu() {
            return menu;
        }

        public void setMenu(String menu) {
            this.menu = menu;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
