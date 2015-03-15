/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.core.nexus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dq
 */
public class BlkStmt {
    private String cmd;
    private String args;

    public BlkStmt(){
    }
    
    public BlkStmt(String cmd){
        this.cmd = cmd;
    }
    
    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }
    
    @Override
    public String toString(){
        StringBuilder ret = new StringBuilder();
        ret.append(cmd);
        ret.append(' ');
        if(args != null){
            ret.append(args);
        }
        ret.append(';');
        return ret.toString();
    }
}
