/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.seqlogo.ui;

import java.awt.Graphics2D;
import java.util.ArrayList;

/**
 *
 * @author dq
 */
public class StyledTextList extends ArrayList<StyledText>{
    
    public void paint(Graphics2D g){        
        for(int i = 0; i < size(); i++){
            StyledText s = get(i);
            s.paint(g);
        }
    }
    
    public int calculateTextLength(){
        int ret = 0;
        for(int i = 0; i < size(); i++){
            StyledText s = get(i);
            ret += s.getTextLength();
        }
        return ret;
    }
    
}
