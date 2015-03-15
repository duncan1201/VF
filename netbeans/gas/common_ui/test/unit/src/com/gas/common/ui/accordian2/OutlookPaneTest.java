/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.accordian2;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author dq
 */
public class OutlookPaneTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
        UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
    }

    @Test
    public void testMain() {
        JFrame frame = new JFrame("OutlookTabbedPaneTest");
        OutlookPane outlookBar = new OutlookPane();
        outlookBar.setSelectedColor(outlookBar.getColor());
        outlookBar.addBar("One", ImageHelper.createImageIcon(ImageNames.BR_UP_16), getDummyPanel("One", 1));
        outlookBar.addBar("Two", ImageHelper.createImageIcon(ImageNames.DB_PUBLICATION_16), getDummyPanel("Two", 10));
        outlookBar.addBar("Three", ImageHelper.createImageIcon(ImageNames.DB_PUBLICATION_16), getDummyPanel("Three", 100));
        outlookBar.addBar("Four", ImageHelper.createImageIcon(ImageNames.ARROW_DOWN_16), getDummyPanel("Four", 200));
        outlookBar.addBar("Five", ImageHelper.createImageIcon(ImageNames.BR_NEXT_16), getDummyPanel("Five", 300));

        outlookBar.show("Four");
        frame.getContentPane().add(outlookBar);

        frame.setSize(300, 600);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(d.width / 2 - 400, d.height / 2 - 300);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        while (true) {
        }
    }

    private MyPanel getDummyPanel(String str, int num) {
        MyPanel ret = new MyPanel();
        ret.setLayout(new BoxLayout(ret, BoxLayout.Y_AXIS));
        for (int i = 0; i < num; i++) {
            ret.add(new JLabel(str));
        }
        return ret;
    }
}

class MyPanel extends JPanel implements IOutlookPanel {

    @Override
    public void expanded() {
        System.out.println("expanded");
    }
}
