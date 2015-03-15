/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.ui.kpanel;

import com.gas.tigr.ui.kpanel.KromatogramPanel;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.util.KromatogramParser;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author dq
 */
public class KromatogramPanelTest extends JFrame {

    KromatogramPanel kPanel;
    public KromatogramPanelTest() {
        // Everything is as usual here        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        String txt = "";
        for (int i = 0; i < 25; i++) {
            txt += i % 10;
        }

        panel.setLayout(new BorderLayout());
        Kromatogram k = getKromatogram();

        kPanel = new KromatogramPanel(k);
        kPanel.setKromatogram(k);

        
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
                kPanel.zoomIn();
                System.out.println();
            }
        }); 
        ret.add(btn);
        
        JToggleButton tBtn = new JToggleButton("A");
        tBtn.setSelected(kPanel.isVisibleA());
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;                
                kPanel.setVisibleA(selected);                
            }
        });         
        ret.add(tBtn);        
        
        tBtn = new JToggleButton("T");
        tBtn.setSelected(kPanel.isVisibleT());
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;                
                kPanel.setVisibleT(selected);                
            }
        });         
        ret.add(tBtn);
        
        tBtn = new JToggleButton("C");
        tBtn.setSelected(kPanel.isVisibleC());
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;                
                kPanel.setVisibleC(selected);                
            }
        });         
        ret.add(tBtn);        

        tBtn = new JToggleButton("G");
        tBtn.setSelected(kPanel.isVisibleG());
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;                
                kPanel.setVisibleG(selected);                
            }
        });         
        ret.add(tBtn);     
        
        tBtn = new JToggleButton("Offset");
        tBtn.setSelected(kPanel.isOffsetVisible());
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;                
                kPanel.setOffsetVisible(selected);          
            }
        });         
        ret.add(tBtn);          
        
        return ret;
    }
    
    

    private Kromatogram getKromatogram() {
        Kromatogram ret = null;
        File file = null ;
        file = new File("D:\\tmp\\abi\\F\\HS354_C02_7360_F_0.b.ab1");
        //file = new File("D:\\tmp\\geneious\\Fragment_1.ab1");
        ret = KromatogramParser.parse(file);
        return ret;
    }

    public static void main(String[] args) {
        KromatogramPanelTest test = new KromatogramPanelTest();
        test.setVisible(true);
    }
}
