/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.ui.ckpanel;

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
public class CKromatogramPanel_trim_end_Test extends JFrame {

    private CKromatogramPanel kPanel;

    public CKromatogramPanel_trim_end_Test() {
        super(CKromatogramPanel_trim_end_Test.class.getSimpleName());        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());
        Kromatogram k = getKromatogram();

        kPanel = new CKromatogramPanel();

        CKromatogramPanel.Read read = new CKromatogramPanel.Read();
        read.setKromatogram(k);
        read.setBases(k.getBases());
        
        //read.getBases().set(5, 'X');
        //read.getKromatogram().getOffsets().set(5, null);
        //read.getKromatogram().getQualityValues().set(5, null);
        
        
        kPanel.setRead(read);

        panel.add(kPanel, BorderLayout.CENTER);
        kPanel.getSimpleBrickPanel().setCaretEnablement(true);
        panel.add(getControlPanel(), BorderLayout.SOUTH);

        panel.setSize(400, 400);
        this.setContentPane(panel);
        this.pack();

        this.setBounds(250, 250, this.getWidth(), this.getHeight());
    }

    private JPanel getControlPanel() {
        JPanel ret = new JPanel();
        JButton btn = new JButton("Zoom in");
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                kPanel.zoom(1.2f);
                System.out.println();
            }
        });
        ret.add(btn);

        btn = new JButton("Zoom out");
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                kPanel.zoom(0.9f);
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

        tBtn = new JToggleButton("INSERT");
        tBtn.setSelected(kPanel.isInsertMode());
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) { 
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;
                kPanel.setInsertMode(selected);
            }
        });
        ret.add(tBtn);    
        
        tBtn = new JToggleButton("Graph Visible");
        tBtn.setSelected(kPanel.isGraphVisible());
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) { 
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;
                kPanel.setGraphVisible(selected);
            }
        });
        ret.add(tBtn);         
        
        return ret;
    }

    private Kromatogram getKromatogram() {
        Kromatogram ret = null;
        File file = null;
        file = new File("D:\\tmp\\abi\\F\\HS354_C02_7360_F_0.b.ab1");
        ret = KromatogramParser.parse(file);
        ret.trimEnd(5);
        return ret;
    }

    public static void main(String[] args) {
        CKromatogramPanel_trim_end_Test test = new CKromatogramPanel_trim_end_Test();
        test.setVisible(true);
    }
}
