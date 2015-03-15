/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.demo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXTitledPanel;
import org.jdesktop.swingx.painter.Painter;

/**
 *
 * @author dunqiang
 */
public class BorderLayoutTest {
    public static void main(String[] args){
        JFrame frame = new JFrame("BorderLayoutTest");
        
        BorderLayout layout = new BorderLayout();
        JPanel panel = new JPanel(layout);

        JLabel label = new JLabel("CENTER");
        JXTitledPanel card = new JXTitledPanel("Title");
        card.setBorder(BorderFactory.createEmptyBorder());
        card.getContentContainer().setLayout(new BorderLayout());
        card.getContentContainer().add(label, BorderLayout.CENTER);
        label.setBorder(new MyBorder(Color.BLACK));
        panel.add(card, BorderLayout.CENTER);
        label = new JLabel("EAST");
        panel.add(label, BorderLayout.EAST);
        
        JXButton btn = new JXButton();
        btn.setOpaque(false);
        //btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setBorderPainted(false);
        Color color = panel.getBackground();
        System.out.println(color);
        System.out.println(System.out.getClass());
        btn.setBackgroundPainter(new Painter(){

            @Override
            public void paint(Graphics2D g, Object object, int width, int height) {
                
                g.setPaint(new Color(238, 238, 238));
                g.fillRect(0, 0, width, height);
            }
        });
         
        
        //btn.setBackground(panel.getBackground());
        btn.setText("WEST");
        panel.add(btn, BorderLayout.WEST);
        
        
        frame.setContentPane(panel);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    
    private static class MyBorder extends LineBorder{
        public MyBorder(Color color){
            super(color);
        }
        
            /**
     * Paints the border for the specified component with the 
     * specified position and size.
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
        @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        int i;

	/// PENDING(klobad) How/should do we support Roundtangles?
        g.setColor(lineColor);
        for(i = 0; i < thickness; i++)  {
	    if(!roundedCorners){
                //g.drawRect(x+i, y+i, width-i-i-1, height-i-i-1);
                g.drawLine(x+i, y+i, x+i, height-i-i-1);
                g.drawLine(x+i, y+i, width-i-i-1, y+i);
                g.drawLine(x+i, height-i-i-1, width-i-i-1, height-i-i-1);
               
            }
	    else
                g.drawRoundRect(x+i, y+i, width-i-i-1, height-i-i-1, thickness, thickness);
        }
        g.setColor(oldColor);
    }
    }
}
