/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.ui.editor.shared;

import com.gas.domain.core.tasm.Condig;
import com.gas.domain.core.tigr.TigrProject;
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
public class AssemblyPanelTest extends JFrame {

    private AssemblyPanel panel;

    public AssemblyPanelTest() {
        // Everything is as usual here        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());
        Condig condig = getCondig(1000, 2000);

        this.panel = new AssemblyPanel();
        this.panel.setCondig(condig);

        panel.add(this.panel, BorderLayout.CENTER);
        panel.add(getControlPanel(), BorderLayout.SOUTH);

        panel.setSize(400, 400);
        this.setContentPane(panel);
        this.pack();

        this.setBounds(250, 250, this.getWidth(), this.getHeight());
        this.panel.revalidate();
    }

    private JPanel getControlPanel() {
        JPanel ret = new JPanel();
        JButton btn = new JButton("Zoom in(deprecated)");
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                //panel.zoom();
                System.out.println();
            }
        });
        ret.add(btn);

        btn = new JButton("Zoom out");
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });
        ret.add(tBtn);

        tBtn = new JToggleButton("T");
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;
            }
        });
        ret.add(tBtn);

        tBtn = new JToggleButton("C");
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;
            }
        });
        ret.add(tBtn);

        tBtn = new JToggleButton("G");
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;
            }
        });
        ret.add(tBtn);

        tBtn = new JToggleButton("INSERT");
        tBtn.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) { 
                int state = e.getStateChange();
                boolean selected = state == ItemEvent.SELECTED;

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
        AssemblyPanelTest test = new AssemblyPanelTest();
        test.setVisible(true);
    }
}
