/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui;

import com.sun.java.swing.plaf.windows.WindowsLookAndFeel;
import java.awt.Font;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 *
 * @author dunqiang
 */
public class DefaultFontTest {
    public static void main(String[] args) throws Exception{
        LookAndFeelInfo[] lafInfos = UIManager.getInstalledLookAndFeels();
        for(LookAndFeelInfo lafInfo: lafInfos){
            System.out.println(lafInfo);
            
        }
        
        System.out.println();
        String className = UIManager.getSystemLookAndFeelClassName();
        LookAndFeel systemLaf = (LookAndFeel)Class.forName(className).newInstance();
        //systemLaf.getDefaults()
        
        WindowsLookAndFeel aaa = new WindowsLookAndFeel();
        UIDefaults defaults = systemLaf.getDefaults();
        Font font = defaults.getFont("TabbedPane.font");
        System.out.println("font="+font);
        System.out.println("font.getFontName()="+font.getFontName());
        System.out.println();
    }
}
