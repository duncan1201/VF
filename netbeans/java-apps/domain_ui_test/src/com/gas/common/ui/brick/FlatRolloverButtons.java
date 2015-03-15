/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.brick;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class FlatRolloverButtons extends JFrame {

    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                FlatRolloverButtons thisClass = new FlatRolloverButtons();
                thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                thisClass.setVisible(true);
            }
        });
    }
    private JButton jButton1 = null;
    private JButton jButton2 = null;
    private JButton jButton3 = null;
    private JButton jButtonExit = null;
    private JPanel jContentPane = null;
    private JPanel jPanelMain = null;
    private MouseListener rolloverButtonListener;

    public FlatRolloverButtons() {
        super();
        initialize();
    }

    private JButton getJButton1() {
        if (jButton1 == null) {
            jButton1 = new JButton();
            jButton1.setText("Button 1");
            initializeAsFlatRolloverButton(jButton1);
        }
        return jButton1;
    }

    private JButton getJButton2() {
        if (jButton2 == null) {
            jButton2 = new JButton();
            jButton2.setText("Button 2");
            initializeAsFlatRolloverButton(jButton2);
        }
        return jButton2;
    }

    private JButton getJButton3() {
        if (jButton3 == null) {
            jButton3 = new JButton();
            jButton3.setText("Button 3");
            initializeAsFlatRolloverButton(jButton3);
        }
        return jButton3;
    }

    private JButton getJButtonExit() {
        if (jButtonExit == null) {
            jButtonExit = new JButton();
            jButtonExit.setText("Exit");
            jButtonExit.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return jButtonExit;
    }

    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(new BorderLayout());
            jContentPane.add(getJButtonExit(), BorderLayout.SOUTH);
            jContentPane.add(getJPanelMain(), BorderLayout.CENTER);
        }
        return jContentPane;
    }

    private JPanel getJPanelMain() {
        if (jPanelMain == null) {
            jPanelMain = new JPanel();
            jPanelMain.setLayout(new FlowLayout());
            jPanelMain.add(getJButton1(), null);
            jPanelMain.add(getJButton2(), null);
            jPanelMain.add(getJButton3(), null);
        }
        return jPanelMain;
    }

    private MouseListener getRolloverButtonListener() {
        if (rolloverButtonListener == null) {
            rolloverButtonListener = new MouseAdapter() {

                public void mouseEntered(MouseEvent e) {
                    Object source = e.getSource();
                    if (source instanceof AbstractButton) {
                        AbstractButton button = (AbstractButton) source;
                        button.setContentAreaFilled(true);
                    }
                }

                public void mouseExited(MouseEvent e) {
                    Object source = e.getSource();
                    if (source instanceof AbstractButton) {
                        AbstractButton button = (AbstractButton) source;
                        button.setContentAreaFilled(false);
                    }
                }
            };
        }
        return rolloverButtonListener;
    }

    private void initialize() {
        this.setSize(300, 200);
        this.setContentPane(getJContentPane());
        this.setTitle("JFrame");
    }

    private void initializeAsFlatRolloverButton(AbstractButton b) {
        b.addMouseListener(getRolloverButtonListener());
        b.setContentAreaFilled(false);
        //b.setBorderPainted(false);
    }
}
