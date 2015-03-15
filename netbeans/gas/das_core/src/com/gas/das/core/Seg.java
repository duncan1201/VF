package com.gas.das.core;

public class Seg {

    private String name;
    private Integer start;
    private Integer end;

    public Seg(String name, Integer start, Integer end) {
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public Seg(String name) {
        this(name, null, null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String toString() {
        if (start == null && end != null) {
            throw new IllegalArgumentException("Both start and end must be set or neither be set");
        }
        if (start != null && end == null) {
            throw new IllegalArgumentException("Both start and end must be set or neither be set");
        }
        StringBuffer ret = new StringBuffer();
        ret.append(name);
        if (start != null) {
            ret.append(":");
            ret.append(start);
            ret.append(",");
        }
        if (end != null) {
            ret.append(end);
        }
        return ret.toString();
    }
}
