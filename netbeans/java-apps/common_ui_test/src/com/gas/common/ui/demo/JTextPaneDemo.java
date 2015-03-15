/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.demo;

/**
 *
 * @author dunqiang
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class JTextPaneDemo extends JFrame {
  static SimpleAttributeSet ITALIC_GRAY = new SimpleAttributeSet();

  static SimpleAttributeSet BOLD_BLACK = new SimpleAttributeSet();

  static SimpleAttributeSet BLACK = new SimpleAttributeSet();

  JTextPane textPane = new JTextPane();

  // Best to reuse attribute sets as much as possible.
  static {
    StyleConstants.setForeground(ITALIC_GRAY, Color.gray);
    StyleConstants.setItalic(ITALIC_GRAY, false);
    StyleConstants.setFontFamily(ITALIC_GRAY, "Helvetica");
    StyleConstants.setFontSize(ITALIC_GRAY, 14);

    StyleConstants.setForeground(BOLD_BLACK, Color.black);
    StyleConstants.setBold(BOLD_BLACK, true);
    StyleConstants.setFontFamily(BOLD_BLACK, "Helvetica");
    StyleConstants.setFontSize(BOLD_BLACK, 14);
    StyleConstants.setAlignment(BOLD_BLACK, StyleConstants.ALIGN_CENTER);

    StyleConstants.setForeground(BLACK, Color.black);
    StyleConstants.setFontFamily(BLACK, "Helvetica");
    StyleConstants.setFontSize(BLACK, 14);
  }

  public JTextPaneDemo() {
    super("JTextPane Demo");

    JScrollPane scrollPane = new JScrollPane(textPane);
    getContentPane().add(scrollPane, BorderLayout.CENTER);
    insertText("PIG:", BOLD_BLACK);
    setEndSelection();
    textPane.insertIcon(new ImageIcon("Pig.gif"));
    insertText("\nWebsite for: www.java2s.com \n\n", BOLD_BLACK);

    setEndSelection();
    insertText("                                    ", BLACK);
    setEndSelection();
    insertText("\nJava"+ "Source\n\n",
        ITALIC_GRAY);

    insertText(" and Support. ", BLACK);

    setEndSelection();
    JButton btn = new JButton("A");
    btn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                textPane.insertComponent(new JButton("NEW"));
            }
        });
    
    textPane.insertComponent(btn);
    setEndSelection();
    btn = new JButton("B");
    btn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                textPane.replaceSelection("");
            }
        });
    textPane.insertComponent(btn);

    
    btn = new JButton("C");
    btn.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String text = textPane.getText();
                System.out.println(text);
            }
        });
    
    setEndSelection();
    textPane.insertComponent(btn);
    
    setSize(500, 450);
    setVisible(true);
    
    textPane.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e) {
                DefaultCaret caret = (DefaultCaret)textPane.getCaret();
                Dimension size = caret.getSize();
                Point loc = caret.getLocation();
                Color color = textPane.getCaretColor();
                System.out.println("color="+color);
                System.out.println("Caret size="+size);
                System.out.println("Loc="+loc);
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
  }

  protected void insertText(String text, AttributeSet set) {
    try {
      textPane.getDocument().insertString(
          textPane.getDocument().getLength(), text, set);
    } catch (BadLocationException e) {
      e.printStackTrace();
    }
  }

  // Needed for inserting icons in the right places
  protected void setEndSelection() {
    textPane.setSelectionStart(textPane.getDocument().getLength());
    textPane.setSelectionEnd(textPane.getDocument().getLength());
  }

  public static void main(String argv[]) {
    new JTextPaneDemo();
  }
}