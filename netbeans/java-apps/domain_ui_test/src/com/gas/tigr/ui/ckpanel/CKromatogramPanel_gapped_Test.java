/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.ui.ckpanel;

import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tasm.Rid;
import com.gas.domain.core.tasm.TasmParser;
import com.gas.domain.core.tigr.Kromatogram;
import com.gas.domain.core.tigr.util.KromatogramHelper;
import com.gas.domain.core.tigr.util.KromatogramParser;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

/**
 *
 * @author dq
 */
public class CKromatogramPanel_gapped_Test extends JFrame {

    private CKromatogramPanel kPanel;

    public CKromatogramPanel_gapped_Test() {
        // Everything is as usual here        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());
        String fName = "HS354_C02_7360_F_0.b.ab1";

        Kromatogram k = getKromatogram(fName);
        Rid rid = getRid(fName, k);
        Kromatogram alteredK = KromatogramHelper.alterKromatogram(rid, k);
        kPanel = new CKromatogramPanel();

        CKromatogramPanel.Read read = new CKromatogramPanel.Read();
        read.setKromatogram(alteredK);
        read.setBases(rid.getLsequence());
        
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

    private Kromatogram getKromatogram(String fileName) {
        Kromatogram ret = null;
        File file = null;
        file = new File("D:\\tmp\\abi\\F\\" + fileName);
        ret = KromatogramParser.parse(file);        
        return ret;
    }
    
    private Rid getRid(String ridName, Kromatogram k){
        Rid ret = null;
        boolean found = false;
        File fromFile = new File("D:\\tmp\\tigr\\myoutput.txt");
        List<Condig> condigs = TasmParser.parse(fromFile);
        for(Condig condig: condigs){
            Iterator<Rid> itr = condig.getRids().iterator();
            while(itr.hasNext() && !found){
                Rid rid = itr.next();
                String seqName = rid.getSeqName();
                if(seqName.equals(ridName)){
                    ret = rid;
                    found = true;                    
                }                
            }
            if(found){
                break;
            }
        }
        
        
        return ret;
    }

    public static void main(String[] args) {
        CKromatogramPanel_gapped_Test test = new CKromatogramPanel_gapped_Test();
        test.setVisible(true);
    }
}
