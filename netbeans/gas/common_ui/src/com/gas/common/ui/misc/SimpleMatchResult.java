/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.util.regex.MatchResult;

/**
 *
 * @author dq
 */
public class SimpleMatchResult implements MatchResult {

    private int start;
    private int end;
    
    public SimpleMatchResult(int start, int end){
        this.start = start;
        this.end = end;
    }
    
    @Override
    public int start() {
        return start;
    }

    @Override
    public int start(int group) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int end() {
        return end;
    }

    @Override
    public int end(int group) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String group() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String group(int group) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int groupCount() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
