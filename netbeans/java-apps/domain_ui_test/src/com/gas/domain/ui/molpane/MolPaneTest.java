/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.molpane;


import com.gas.main.ui.molpane.MolPane;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class MolPaneTest extends JFrame {
    
    static int i = 0;

    public MolPaneTest(String appName) {
        // Everything is as usual here
        super(appName);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        final MolPane pane = MolPane.createInstance();
        pane.setPreferredSize(new Dimension(300, 300));
        pane.setBackground(Color.WHITE);
        
        
        final AnnotatedSeq NM_001206524 = AnnotatedSeqParser.singleParse(MolPane.class, "NM_001206524.gb", new FlexGenbankFormat());
        final AnnotatedSeq NM_008238 = AnnotatedSeqParser.singleParse(MolPane.class, "NM_008238.gb", new FlexGenbankFormat());
        final AnnotatedSeq NM_133503 = AnnotatedSeqParser.singleParse(MolPane.class, "NM_133503.gb", new FlexGenbankFormat());
        final AnnotatedSeq[] allSeq = {NM_001206524, NM_008238, NM_133503};
        
        pane.setAs(NM_001206524);

        panel.add(pane, BorderLayout.CENTER);
        JButton btn = new JButton("Switch");
        panel.add(btn, BorderLayout.SOUTH);
        btn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                
                    pane.setAs(allSeq[i % allSeq.length]);
                
                //pane.getRowHeaderView().revalidate();
                //pane.getRowHeaderView().repaint();
                
                pane.revalidate();
                pane.repaint();
                
                //pane.getRowHeader().revalidate();
                //pane.getRowHeader().repaint();
                i++;
            }
        });
        
        this.setContentPane(panel);
        
        this.pack();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

// The entry point of the application
    public static void main(String[] args) {
        MolPaneTest writer = new MolPaneTest("GraphPaneTest");
        writer.setVisible(true);
    }
}
