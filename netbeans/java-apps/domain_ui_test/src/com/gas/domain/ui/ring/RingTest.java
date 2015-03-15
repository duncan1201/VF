/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.ring;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class RingTest extends JFrame {


    public RingTest() {
        super("RingTest");
    }

    public void createAndShowGUI() {
        /* Set up frame */
        JFrame frame = new RingTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /* Set up scroll pane */
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JButton("NORTH"), BorderLayout.NORTH);
        panel.add(new JButton("WEST"), BorderLayout.WEST);
        panel.add(new JButton("EAST"), BorderLayout.EAST);
        Ring ring = new Ring();
        ring.setStartAngle(1);
        ring.setExtent(350);
        ring.setForward(true);
        ring.setRingWidth(15);
        panel.add(ring, BorderLayout.CENTER);


        /* pack and show frame */
        frame.add(panel);
        frame.pack();
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new RingTest().createAndShowGUI();
            }
        });
    }
}