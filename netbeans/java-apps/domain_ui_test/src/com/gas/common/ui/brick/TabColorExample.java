/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.brick;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;

/**
 * @version 1.1 06/02/99
 */
public class TabColorExample extends JPanel {
  public TabColorExample() {
    setLayout(new BorderLayout());

    UIManager.put("TabbedPane.selected", Color.green);

    JTabbedPane tabbedPane = new JTabbedPane();
    String tabs[] = { "One", "Two", "Three", "Four" };
    Color[] colors = { null, Color.red, null, null };
    for (int i = 0; i < tabs.length; i++) {
      tabbedPane.addTab(tabs[i], createPane(tabs[i]));
      tabbedPane.setBackgroundAt(i, colors[i]);
    }
    tabbedPane.setSelectedIndex(0);
    add(tabbedPane, BorderLayout.CENTER);
  }

  JPanel createPane(String s) {
    JPanel p = new JPanel();
    p.add(new JLabel(s));
    return p;
  }

  public static void main(String[] args) {
    JFrame frame = new JFrame("Tab color Example");
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.getContentPane().add(new TabColorExample());
    frame.setSize(200, 100);
    frame.setVisible(true);
  }
}