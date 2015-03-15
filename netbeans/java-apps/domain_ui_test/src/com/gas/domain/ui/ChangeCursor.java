/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui;

import java.awt.*;
import java.awt.event.*;

public class ChangeCursor{
  public static void main(String[] args) {
  Frame f = new Frame("Change cursor");
  Panel panel = new Panel();
  Button comp1 = new Button("Ok");
  Button comp2 = new Button("Cancel");
  panel.add(comp1);
  panel.add(comp2);
  f.add(panel,BorderLayout.CENTER);
  f.setSize(200,200);
  f.setVisible(true);
  Cursor cur = comp1.getCursor();
  Cursor cur1 = comp2.getCursor();
  comp1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
  comp2.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
  f.addWindowListener(new WindowAdapter(){
  public void windowClosing(WindowEvent we){
  System.exit(0);
  }
  });

  }
}