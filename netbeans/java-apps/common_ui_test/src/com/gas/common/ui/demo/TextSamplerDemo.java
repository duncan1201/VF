/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.demo;

/*
 * TextSamplerDemo.java requires the following files:
 *   TextSamplerDemoHelp.html (which references images/dukeWaveRed.gif)
 *   images/Pig.gif
 *   images/sound.gif
 */

import com.gas.common.ui.test2.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.text.*;

import java.awt.*;              //for layout managers and more
import java.awt.event.*;        //for action events

import java.net.URL;
import java.io.IOException;

public class TextSamplerDemo extends JPanel
                             implements ActionListener {
    private String newline = "\n";
    protected static final String textFieldString = "JTextField";
    protected static final String passwordFieldString = "JPasswordField";
    protected static final String ftfString = "JFormattedTextField";
    protected static final String buttonString = "JButton";

    protected JLabel actionLabel;
    JTextPane textPane;
    
    public TextSamplerDemo() {
        setLayout(new BorderLayout());



        //Create an editor pane.
        JEditorPane editorPane = createEditorPane();
        JScrollPane editorScrollPane = new JScrollPane(editorPane);
        editorScrollPane.setVerticalScrollBarPolicy(
                        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        editorScrollPane.setPreferredSize(new Dimension(250, 145));
        editorScrollPane.setMinimumSize(new Dimension(10, 10));

        //Create a text pane.
        textPane = createTextPane();
        JScrollPane paneScrollPane = new JScrollPane(textPane);
        paneScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        paneScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        paneScrollPane.setPreferredSize(new Dimension(250, 155));
        paneScrollPane.setMinimumSize(new Dimension(10, 10));



        //Put everything together.
 

        add(paneScrollPane, BorderLayout.CENTER);
    }

    private void addLabelTextRows(JLabel[] labels,
                                  JTextField[] textFields,
                                  GridBagLayout gridbag,
                                  Container container) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        int numLabels = labels.length;

        for (int i = 0; i < numLabels; i++) {
            c.gridwidth = GridBagConstraints.RELATIVE; //next-to-last
            c.fill = GridBagConstraints.NONE;      //reset to default
            c.weightx = 0.0;                       //reset to default
            container.add(labels[i], c);

            c.gridwidth = GridBagConstraints.REMAINDER;     //end row
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1.0;
            container.add(textFields[i], c);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String prefix = "You typed \"";
        if (textFieldString.equals(e.getActionCommand())) {
            JTextField source = (JTextField)e.getSource();
            actionLabel.setText(prefix + source.getText() + "\"");
        } else if (passwordFieldString.equals(e.getActionCommand())) {
            JPasswordField source = (JPasswordField)e.getSource();
            actionLabel.setText(prefix + new String(source.getPassword())
                                + "\"");
        } else if (buttonString.equals(e.getActionCommand())) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    private JEditorPane createEditorPane() {
        JEditorPane editorPane = new JEditorPane();
        editorPane.setEditable(false);
        java.net.URL helpURL = TextSamplerDemo.class.getResource(
                                        "TextSamplerDemoHelp.html");
        if (helpURL != null) {
            try {
                editorPane.setPage(helpURL);
            } catch (IOException e) {
                System.err.println("Attempted to read a bad URL: " + helpURL);
            }
        } else {
            System.err.println("Couldn't find file: TextSampleDemoHelp.html");
        }

        return editorPane;
    }

    private JTextPane createTextPane() {
        String[] initString =
                { "This is an editable JTextPane, ",            //regular
                  "another ",                                   //italic
                  "styled ",                                    //bold
                  "text ",                                      //small
                  "component, ",                                //large
                  "which supports embedded components..." + newline,//regular
                  " " ,                                //button
                  "...and embedded icons..." + newline,         //regular
                  " ",                                          //icon
                  newline + "JTextPane is a subclass of JEditorPane that " +
                    "uses a StyledEditorKit and StyledDocument, and provides " +
                    "cover methods for interacting with those objects."
                 };

        String[] initStyles =
                { "regular", "italic", "bold", "small", "large",
                  "regular", "button", "regular", "icon",
                  "regular"
                };

        JTextPane textPane = new JTextPane();
        StyledDocument doc = textPane.getStyledDocument();
        addStylesToDocument(doc);

        try {
            for (int i=0; i < initString.length; i++) {
                doc.insertString(doc.getLength(), initString[i],
                                 doc.getStyle(initStyles[i]));
            }
        } catch (BadLocationException ble) {
            System.err.println("Couldn't insert initial text into text pane.");
        }

        return textPane;
    }

    protected void addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);

        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");

        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);

        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);

        //s = doc.addStyle("icon", regular);
        s = doc.addStyle("icon", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon pigIcon = createImageIcon("images/Pig.gif",
                                            "a cute pig");
        if (pigIcon != null) {
            StyleConstants.setIcon(s, pigIcon);
        }

        s = doc.addStyle("button", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon soundIcon = createImageIcon("images/sound.gif",
                                              "sound icon");
        JButton button = new JButton();
        button.setText("BP");
        if (soundIcon != null) {
            button.setIcon(soundIcon);
        } 
        button.setCursor(Cursor.getDefaultCursor());
        button.setMargin(new Insets(0,0,0,0));
        button.setActionCommand(buttonString);
        //button.addActionListener(this);
        button.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                StyledDocument doc = (StyledDocument)textPane.getDocument();
                Style style = doc.getStyle("button");
                JButton c = (JButton)StyleConstants.getComponent(style);
                System.out.println(c.getText());
                if(c.getText().equals("BP")){
                    c.setText("OK");
                }else{
                    c.setText("BP");
                }
            }
        });
        
        button.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                StyledDocument doc = textPane.getStyledDocument();
                try {
                    doc.insertString(doc.getLength(), "  ",
                                     doc.getStyle("icon"));
                } catch (BadLocationException ex) {
                    Logger.getLogger(TextSamplerDemo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        StyleConstants.setComponent(s, button);
    }

    /** Returns an ImageIcon, or null if the path was invalid. */
    protected static ImageIcon createImageIcon(String path,
                                               String description) {
        java.net.URL imgURL = TextSamplerDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, description);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("TextSamplerDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add content to the window.
        frame.add(new TextSamplerDemo());

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                 //Turn off metal's use of bold fonts
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		createAndShowGUI();
            }
        });
    }
}