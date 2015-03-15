package com.gas.domain.core.as;

public class Dbref implements Cloneable {

    private Integer hibernateId;
    private String db;
    private String entry;

    public Dbref() {
    }// for hibernate use only

    public Dbref(String db, String entry) {
        this.db = db;
        this.entry = entry;
    }

    @Override
    public Dbref clone() {
        Dbref ret = new Dbref();
        ret.setDb(getDb());
        ret.setEntry(getEntry());
        return ret;
    }

    public Integer getHibernateId() {
        return hibernateId;
    }

    public void setHibernateId(Integer hibernateId) {
        this.hibernateId = hibernateId;
    }

    public String getDb() {
        return db;
    }

    public void setDb(String db) {
        this.db = db;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public boolean equals(Object o) {
        if (o instanceof Dbref) {
            Dbref another = (Dbref) o;
            return another.getDb().equals(getDb()) && another.getEntry().equals(getEntry());
        } else {
            return false;
        }
    }

    public String toString() {
        return getDb() + ":" + getEntry();
    }

    public int hashCode() {
        return toString().hashCode();
    }
}
