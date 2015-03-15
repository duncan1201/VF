/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.histogram;

import com.gas.domain.core.tasm.Condig;
import com.gas.tigr.ui.kpanel.*;
import com.gas.tigr.ui.kpanel.KromatogramPanel;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.TigrProject;
import com.gas.domain.core.tigr.util.KromatogramParser;
import com.gas.domain.core.tigr.util.TigrProjectIO;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Iterator;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author dq
 */
public class HistogramlTest extends JFrame {

    Histogram kPanel;
    public HistogramlTest() {
        // Everything is as usual here        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        String txt = "";
        for (int i = 0; i < 25; i++) {
            txt += i % 10;
        }

        panel.setLayout(new BorderLayout());
        Condig condig = getCondig(1000, 2000);

        kPanel = new Histogram();
        kPanel.setData(condig.getQualities());

        
        panel.add(kPanel, BorderLayout.CENTER);
        panel.add(getControlPanel(), BorderLayout.SOUTH);

        panel.setSize(400, 400);
        this.setContentPane(panel);
        this.pack();
        
        this.setBounds(250, 250, this.getWidth(),
        this.getHeight());                 
    }
    
    private JPanel getControlPanel(){
        JPanel ret = new JPanel();
        JButton btn = new JButton("Zoom in");        
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //kPanel.zoomIn();
                System.out.println();
            }
        }); 
        ret.add(btn);
        
        JToggleButton tBtn = new JToggleButton("A");
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;                
                //kPanel.setVisibleA(selected);                
            }
        });         
        ret.add(tBtn);        
        
        tBtn = new JToggleButton("T");
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;                
                //kPanel.setVisibleT(selected);                
            }
        });         
        ret.add(tBtn);
        
        tBtn = new JToggleButton("C");
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;                
                //kPanel.setVisibleC(selected);                
            }
        });         
        ret.add(tBtn);        

        tBtn = new JToggleButton("G");
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;                
                //kPanel.setVisibleG(selected);                
            }
        });         
        ret.add(tBtn);                
        
        return ret;
    }
    
    private Condig getCondig(int min, int max){
        Condig ret = null;
        TigrProject p = TigrProjectIO.read(new File("D:\\tmp\\tigr\\tigr_project.ser"));
        Iterator<Condig> itr = p.getUnmodifiableCondigs().iterator();
        while(itr.hasNext()){
            ret = itr.next();
            int length = ret.getLsequence().length();
            if(length > min && length < max){
                break;
            }
        }
        return ret;
    }

    public static void main(String[] args) {
        HistogramlTest test = new HistogramlTest();
        test.setVisible(true);
    }
}
