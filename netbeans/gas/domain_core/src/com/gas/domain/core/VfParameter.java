/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core;

/**
 *
 * @author adm_LiaoDu
 */
public class VfParameter {
    private String hibernateId; // for hibernate use only
    private String name;
    private String value;

    protected String getHibernateId() {
        return hibernateId;
    }

    protected void setHibernateId(String hibernateId) {
        this.hibernateId = hibernateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }    
}
