/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.jcomp;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;

/**
 *
 * @author dunqiang
 */
public class CollapsibleBorderPanelTest {

    public static void StringTitle_test() {
        JFrame frame = new JFrame("StringTitle_test");

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints c = null;

        CollapsibleBorderPanel cp = new CollapsibleBorderPanel("Title");
        cp.getContentPane().add(new JLabel("Test Content"), BorderLayout.CENTER);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        panel.add(cp, c);


        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(new JLabel("Second Label"), c);

        frame.setContentPane(panel);

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    
        public static void RadioButton_test() {
        JFrame frame = new JFrame("RadioButton_test");

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints c = null;

        CollapsibleBorderPanel cp = new CollapsibleBorderPanel(new JRadioButton("Radio"));
        cp.getContentPane().add(new JLabel("Test Content"), BorderLayout.CENTER);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        panel.add(cp, c);


        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(new JLabel("Second Label"), c);

        frame.setContentPane(panel);

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void Default_constructor_test() {
        JFrame frame = new JFrame("Default_constructor_test");

        JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        panel.setLayout(layout);
        GridBagConstraints c = null;

        CollapsibleBorderPanel cp = new CollapsibleBorderPanel();
        cp.setTitle("Default_constructor_test");        
        cp.getContentPane().add(new JLabel("Test Content"), BorderLayout.CENTER);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        panel.add(cp, c);


        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        panel.add(new JLabel("Second Label"), c);

        frame.setContentPane(panel);

        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        StringTitle_test();
        RadioButton_test();
        Default_constructor_test();
    }
}
