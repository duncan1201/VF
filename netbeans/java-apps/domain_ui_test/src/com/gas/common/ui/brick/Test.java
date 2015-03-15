/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.brick;

import com.gas.common.ui.image.ImageHelper;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Test {

    private static int maxW = 0;
    private static int maxH = 0;

    public static void main(String[] args) {


        final JFrame f = new JFrame();

        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);

        JButton btn = new JButton();
        //btn.setFocusPainted(false);
        //btn.setOpaque(false);        
        //btn.setBorder(null);
        btn.setContentAreaFilled(false);
        btn.setIconTextGap(0);
        btn.setPreferredSize(new Dimension(20,20));
        Icon icon = createImageIcon("D:\\tmp\\br_next-16.png");
        btn.setIcon(icon);
        btn.addMouseListener(getRolloverButtonListener());
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);

        panel.add(btn);

        JLabel label = new JLabel("Label");
        panel.add(label);

        f.setContentPane(panel);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.pack();
        f.setLocationRelativeTo(null);

        f.setVisible(true);


    }

    public static ImageIcon createImageIcon(String name) {
        ImageIcon ret = null;
        try {
            Image image = ImageIO.read(new File(name));
            ret = new ImageIcon(image, "");
        } catch (IOException ex) {
        }
        return ret;
    }
    
    static MouseAdapter rolloverButtonListener;

    private static MouseListener getRolloverButtonListener() {
        if (rolloverButtonListener == null) {
            rolloverButtonListener = new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    Object source = e.getSource();
                    if (source instanceof AbstractButton) {
                        AbstractButton button = (AbstractButton) source;
                        button.setContentAreaFilled(true);
                        button.setBorderPainted(true);
                    }
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    Object source = e.getSource();
                    if (source instanceof AbstractButton) {
                        AbstractButton button = (AbstractButton) source;
                        button.setContentAreaFilled(false);
                        button.setBorderPainted(false);
                    }
                }
            };
        }
        return rolloverButtonListener;
    }

    private static final JPanel createPanel(Color color, int w, int h) {

        JPanel p = new JPanel();
        p.setBackground(color);
        p.setPreferredSize(new Dimension(w, h));

        maxW = Math.max(w, maxW);
        maxH = Math.max(h, maxH);

        return p;

    }
}
