/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.common.ui.core.BooleanList;
import com.gas.common.ui.core.FloatList;
import com.gas.common.ui.util.FontUtil;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class RingTes extends JFrame {

    public RingTes() {
        super("RingTest");
    }

    public void createAndShowGUI() {
        /* Set up frame */
        JFrame frame = new RingTes();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /* Set up scroll pane */
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JTextArea(5, 5), BorderLayout.NORTH);
        panel.add(new JButton("WEST"), BorderLayout.WEST);
        JButton east = new JButton("Add text");

        panel.add(east, BorderLayout.EAST);

        Font msFont = FontUtil.getDefaultMSFont();

        final Ring ring = new Ring();
        ring.setFont(msFont);
        ring.setStartAngles(new FloatList(350f));
        ring.setExtents(new FloatList(100f));
        ring.setForwards(new BooleanList(true));
        ring.setRingThickness(20);
        panel.add(ring, BorderLayout.CENTER);


        /* pack and show frame */
        frame.add(panel);
        frame.pack();
        frame.setSize(500, 500);
        frame.setVisible(true);

        east.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new RingTes().createAndShowGUI();
            }
        });
    }
}