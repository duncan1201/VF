/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import javax.swing.JComponent;
import javax.swing.JLabel;
import sun.swing.SwingUtilities2;

/**
 *
 * @author dunqiang
 */
public class ToolkitTest {

    public static Clipboard getClipboard(JComponent c) {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Toolkit tk2 = c.getToolkit();
        return c.getToolkit().getSystemClipboard();


    }

    public static void main(String[] args) {
        JLabel j = new JLabel();
        getClipboard(j);
    }
}
