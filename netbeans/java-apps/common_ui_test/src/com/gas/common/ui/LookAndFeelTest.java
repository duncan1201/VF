/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 *
 * @author dunqiang
 */
public class LookAndFeelTest {
    public static void main(String[] args){
        LookAndFeel laf = UIManager.getLookAndFeel();
        
        if(laf instanceof MetalLookAndFeel){
            FontUIResource uiRes = MetalLookAndFeel.getUserTextFont();
            int size = uiRes.getSize();
            System.out.println(size);
        }
    }
}
