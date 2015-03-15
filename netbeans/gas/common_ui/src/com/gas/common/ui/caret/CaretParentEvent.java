/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.caret;

import java.util.EventObject;

/**
 *
 * @author dq
 */
public class CaretParentEvent extends EventObject {

    private Integer pos;
    private String data;

    public CaretParentEvent(Object src, Integer pos, String data) {
        super(src);
        this.pos = pos;
        this.data = data;
    }

    public CaretParentEvent(Object src) {
        this(src, null, null);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getPos() {
        return pos;
    }

    public void setPos(Integer pos) {
        this.pos = pos;
    }
}
