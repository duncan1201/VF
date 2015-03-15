/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.ringpane;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.util.AnnotatedSeqParser;
import com.gas.domain.core.flexrs.FlexGenbankFormat;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class RingPaneTest extends JFrame {

    public RingPaneTest() {
        super("RingPaneTest");
    }

    public void createAndShowGUI() {
        /* Set up frame */
        JFrame frame = new RingPaneTest();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        /* Set up scroll pane */
        JPanel panel = new JPanel(new BorderLayout());

        RingPane ringPane = new RingPane();
        ringPane.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        AnnotatedSeq as = AnnotatedSeqParser.singleParse(RingPaneTest.class, "M77789.gb", new FlexGenbankFormat());
        ringPane.setAs(as);
        panel.add(new JLabel("NORTH"), BorderLayout.NORTH);
        panel.add(ringPane, BorderLayout.CENTER);


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
                new RingPaneTest().createAndShowGUI();
            }
        });
    }
}
