/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.demo.scrollpane;

import java.awt.*;
import javax.swing.*;

/* Corner.java is used by ScrollDemo.java. */

public class Corner extends JComponent {
    protected void paintComponent(Graphics g) {
        // Fill me with dirty brown/orange.
        g.setColor(new Color(230, 163, 4));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}