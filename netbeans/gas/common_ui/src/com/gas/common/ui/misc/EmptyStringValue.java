/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

/**
 *
 * @author dq
 */
public class EmptyStringValue implements org.jdesktop.swingx.renderer.StringValue {

    @Override
    public String getString(Object value) {
        return "";
    }
}
