/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.geneticCode.api;

import java.util.Locale;

/**
 *
 * @author dq
 */
public class TableInfo {

    private String name;
    private String desc;
    private int tableNo;
    private transient StringBuilder details;

    public TableInfo() {
    }

    public TableInfo(String name, String desc, int tableNo) {
        this.name = name;
        this.desc = desc;
        this.tableNo = tableNo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public String getDetails() {
        if (details == null) {
            details = new StringBuilder();
            details.append(desc);
            details.append('(');
            details.append("transl_table:");
            details.append(tableNo);
            details.append(')');
        }
        return details.toString();
    }

    @Override
    public String toString() {
        return String.format("%s(%d)", beautify(name), tableNo);
    }

    private static String beautify(String a) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            String c = a.substring(i, i + 1);
            if (c.equals("_")) {
                ret.append(' ');
            } else if (i == 0) {
                ret.append(c.toUpperCase(Locale.ENGLISH));
            } else {
                ret.append(c.toLowerCase());
            }
        }
        return ret.toString();
    }
}
