package com.gas.domain.ui.molpane.graphpane;

import com.gas.domain.core.as.Lucation;
import com.gas.domain.ui.shape.Arrow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ArrowTest extends JFrame {

// Swing components
    protected Arrow arrow = new Arrow();
    private JColorChooser colorChooser;
    private JColorChooser highlightColorChooser;
// Constructor

    public ArrowTest(String appName) {
        // Everything is as usual here
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        
        String txt = "";
        for(int i = 0; i < 25; i++){
            txt += i % 10;
        }
        
        panel.setLayout(new BorderLayout());
        panel.add(getControlPanel(), BorderLayout.NORTH);
        Font oldFont = panel.getFont();
        Font newFont = oldFont.deriveFont(12.0f);
        arrow.setTextFont(newFont);
        arrow.setDisplayText(txt);
        arrow.setBounds(0, 0, 100, 80);
        arrow.setLuc(new Lucation(1, 100));
        arrow.setSize(150, 40);
        
        JPanel arrowPanel = new JPanel();
        arrowPanel.setLayout(new FlowLayout());
        arrowPanel.add(arrow, BorderLayout.CENTER);
        arrowPanel.add(arrow);
        
        panel.add(arrowPanel, BorderLayout.CENTER);
        JLabel eastLabel = new JLabel();
        Font font = eastLabel.getFont();
        Font derivedFont = font.deriveFont(30.0f);
        eastLabel.setFont(derivedFont);
        eastLabel.setText("EAST");
        panel.add(eastLabel, BorderLayout.EAST);
        panel.add(new JLabel("WEST"), BorderLayout.WEST);
        panel.add(new JLabel("SOUTHSOUTHSOUTHSOUTHSOUTHSOUTHSOUTHSOUTHSOUTHSOUTHSOUTHSOUTH"), BorderLayout.SOUTH);
        panel.setSize(400, 400);
        this.setContentPane(panel);
        this.pack();
        /*
        this.setBounds(250, 250, this.getWidth(),
                this.getHeight());
         
         */
        
    }
    
    private JPanel getControlPanel() {
        final JPanel ret = new JPanel();
        ret.setLayout(new GridLayout(7, 2));
        final String FORWARD = "Forward";
        final String REVERSE = "Reverse";
        final String NONE = "NONE";
        
        ret.add(new JLabel("Direction:"));
        String[] petStrings = {FORWARD, REVERSE, NONE};
        
        final JComboBox directionComboBox = new JComboBox(petStrings);
        directionComboBox.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                String direction = (String) directionComboBox.getSelectedItem();
                if (direction.equals(FORWARD)) {
                    arrow.setForward(true);
                } else if (direction.equals(REVERSE)) {
                    arrow.setForward(false);
                } else {
                    arrow.setForward(null);
                }
                arrow.repaint();
            }
        });
        ret.add(directionComboBox);
        
        ret.add(new JLabel("Bar Width % of height:"));
        SpinnerModel spinnerModel =
                new SpinnerNumberModel(0.9, //initial value
                0.1, //min
                1, //max
                0.1);                //step
        final JSpinner spinner = new JSpinner(spinnerModel);
        spinner.addChangeListener(new ChangeListener() {
            
            @Override
            public void stateChanged(ChangeEvent e) {
                double value = (Double) spinner.getModel().getValue();
                System.out.println(value);
                arrow.setBarHeightRatio((float) value);
                arrow.repaint();
            }
        });
        ret.add(spinner);
        
        final JButton colorButton = new JButton();
        colorButton.setText("Color Chooser");
        colorButton.addActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean pSet = colorButton.isPreferredSizeSet();
                boolean mSet = colorButton.isMinimumSizeSet();
                boolean maxSet = colorButton.isMaximumSizeSet();

                System.out.println(pSet);
                System.out.println(mSet);
                System.out.println(maxSet);
                
                Dimension size = colorButton.getSize();
                System.out.println("size="+size);
                
                colorChooser = new JColorChooser();
                JDialog dialog = JColorChooser.createDialog(ret, "Title", true, colorChooser, new ColorOkListener(), new CancelListener());
                dialog.setVisible(true);
            }
        });
        ret.add(colorButton);
        
        
        ret.add(new JLabel("Vertical Margins:"));
        spinnerModel =
                new SpinnerNumberModel(0.05, //initial value
                0, //min
                0.3, //max
                0.01);  
        final JSpinner verticalMarginsSpinner = new JSpinner(spinnerModel);
        verticalMarginsSpinner.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                double value = (Double)verticalMarginsSpinner.getValue();
                arrow.setVerticalPaddingRatio((float)value);
                arrow.repaint();
            }
        });
        ret.add(verticalMarginsSpinner);
        
        
        JButton highlightColorButton = new JButton();
        highlightColorButton.setText("Highlight Color:");
        highlightColorButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                highlightColorChooser = new JColorChooser();
                JDialog dialog = JColorChooser.createDialog(ret, "Title", true, highlightColorChooser, new HighlightColorOkListener(), new CancelListener());
                dialog.setVisible(true);
                
            }
        });
        ret.add(highlightColorButton);
        return ret;
    }
    
    private class HighlightColorOkListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            Color color = highlightColorChooser.getColor();
            arrow.setHighlightColor(color);
            arrow.repaint();
        }
    }
    
    private class ColorOkListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            Color color = colorChooser.getColor();
            arrow.setSeedColor(color);
            arrow.repaint();
        }
    }
    
    private class CancelListener implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
        }
    }

// The entry point of the application
    public static void main(String[] args) {
        ArrowTest writer = new ArrowTest("SVG Writer");
        writer.setVisible(true);
    }
}
