/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.ring;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class RingPanelTest extends JFrame {
        public RingPanelTest() {
        super("RingPanelTest");
    }

    public void createAndShowGUI() {
        /* Set up frame */
        JFrame frame = new RingPanelTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /* Set up scroll pane */
        JPanel panel = new JPanel(new BorderLayout());
        //Ring ring = new Ring();
        //ring.setStartAngle(340);
        //ring.setExtent(80);
        //ring.setForward(true);
        //ring.setRingWidth(15);
        RingPanel ringPanel = new RingPanel();
        AnnotatedSeq as = AnnotatedSeqParser.singleParse(RingPanelTest.class, "XM_546602.gb", new FlexGenbankFormat());
        ringPanel.setAs(as);
        panel.add(ringPanel, BorderLayout.CENTER);


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
                new RingPanelTest().createAndShowGUI();
            }
        });
    }
}
