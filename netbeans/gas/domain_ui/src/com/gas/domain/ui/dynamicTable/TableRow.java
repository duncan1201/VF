/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.dynamicTable;

/**
 *
 * @author dq
 */
public class TableRow {

    private boolean check;
    private Object obj;

    public TableRow(boolean check, Object obj) {
        this.check = check;
        this.obj = obj;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
