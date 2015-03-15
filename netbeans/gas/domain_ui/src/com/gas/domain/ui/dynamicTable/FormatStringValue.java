/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.dynamicTable;

/**
 *
 * @author dq
 */
public class FormatStringValue implements org.jdesktop.swingx.renderer.StringValue{

    private String formatStr;
    
    public FormatStringValue(String formatStr){
        this.formatStr = formatStr;
    }
    
    @Override
    public String getString(Object value) {
        return String.format(formatStr, value);
    }
    
}
