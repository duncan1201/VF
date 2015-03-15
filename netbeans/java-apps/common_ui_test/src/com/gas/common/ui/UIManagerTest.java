/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui;

import java.awt.Font;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author dunqiang
 */
public class UIManagerTest {

    public static void main(String[] args) throws Exception {
        String laf = System.getProperty("swing.defaultlaf");
        //System.out.println("swing.defaultlaf="+laf);     
                
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        
        String className = UIManager.getSystemLookAndFeelClassName();
        //System.out.println("SystemLookAndFeelClassName="+className);
        className = UIManager.getCrossPlatformLookAndFeelClassName() ;
        System.out.println("CrossPlatformLookAndFeelClassName="+className);
        className = UIManager.getLookAndFeel().getClass().toString();
        System.out.println("current look and feel="+className);
        
        UIDefaults uiDefaults = UIManager.getDefaults();

        
        Font font = uiDefaults.getFont("TabbedPane.font");
        System.out.println("font="+font);
        System.out.println("font.getFontName()="+font.getFontName());
        System.out.println();
        
        Set<Map.Entry<Object, Object>> entrySet = uiDefaults.entrySet();
        int size = entrySet.size();

        Iterator<Entry<Object, Object>> entrySetItr = entrySet.iterator();

        int i = 0;
        while (entrySetItr.hasNext()) {
            Entry<Object, Object> entry = entrySetItr.next();
            Object key = entry.getKey();
            Object value = entry.getValue();
            if(key instanceof String){
                String keyStr = (String)key;
                if(keyStr.startsWith("Tree")){
                   System.out.println(i + " " + keyStr + "");
                    //font = uiDefaults.getFont(key);
                    if(font != null){
                        //System.out.println("    " + uiDefaults.get(key));
                    }}
            }
            //System.out.println("    " + value);

            i++;
        }
    }
}
