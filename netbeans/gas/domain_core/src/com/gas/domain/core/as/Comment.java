package com.gas.domain.core.as;

public class Comment implements Cloneable {

    public static final int RANK = 10;
    private Integer hibernateId; // for hibernate use only
    private String data;
    private int rank;

    @Override
    public Comment clone() {
        Comment ret = new Comment();
        ret.setData(getData());
        ret.setRank(getRank());
        return ret;
    }

    protected Integer getHibernateId() {
        return hibernateId;
    }

    protected void setHibernateId(int hibernateId) {
        this.hibernateId = hibernateId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
