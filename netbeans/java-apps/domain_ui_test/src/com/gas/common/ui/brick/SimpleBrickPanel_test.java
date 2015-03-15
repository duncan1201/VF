/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.brick;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import com.gas.domain.ui.molpane.txtpane.BrickPane_translation_test;
import javax.swing.JFrame;

/**
 *
 * @author dq
 */
public class SimpleBrickPanel_test extends JFrame {
        public SimpleBrickPanel_test(String appName) {
        // Everything is as usual here
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        AnnotatedSeq seq = AnnotatedSeqParser.singleParse(BrickPane_translation_test.class, "sequence.gb", new FlexGenbankFormat());
        StringBuilder ret = new StringBuilder();
        for(int i = 0; i < 100; i++){
            ret.append('A');
        }
        
        SimpleBrickPanel pane = new SimpleBrickPanel();
        pane.setData(ret.toString(), true);
        //pane.setPreferredSize(new Dimension(500, 400));
             
        pane.setDoubleLine(true);
        
        this.setSize(550, 450);
        this.setContentPane(pane);
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
        
// The entry point of the application
    public static void main(String[] args) {
        SimpleBrickPanel_test writer = new SimpleBrickPanel_test(" Writer");
        writer.setVisible(true);
    }        
}
