/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.light;

import com.gas.common.ui.core.Arc2DX;
import com.gas.common.ui.core.Arc2DX.Float;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author dq
 */
public class CircularBrickTest {

    public CircularBrickTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of paint method, of class CircularBrick.
     */
    @Test
    public void testPaint() {
        System.out.println("testPaint");
        Graphics2D g2d = null;
        Wrapper wrapper = new Wrapper();


//1. Create the frame.
        JFrame frame = new JFrame("CircularBrickTest");

//2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//3. Create components and put them in the frame.
//...create emptyLabel...
        frame.getContentPane().add(wrapper, BorderLayout.CENTER);
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event.getID() == MouseEvent.MOUSE_CLICKED) {
                    System.out.println("MouseEvent.MOUSE_CLICKED");
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK);
//4. Size the frame.
        frame.pack();

//5. Show it.
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(size.width - 250, size.height - 300);
        frame.setVisible(true);
        while (true) {
        }
    }

    private static class Wrapper extends JComponent {

        CircularBrick circularBrick = new CircularBrick();

        public Wrapper() {
            circularBrick.setText("X");
            Arc2DX.Float outerArc = new Arc2DX.Float();
            outerArc.setArcByCenter(130, 130, 60, 45, 45, Arc2D.OPEN);
            circularBrick.setOuterArc(outerArc);

            Arc2DX.Float innerArc = new Arc2DX.Float();
            innerArc.setArcByCenter(130, 130, 40, 90, -45, Arc2D.OPEN);
            circularBrick.setInnerArc(innerArc);
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            circularBrick.paint(g2d);
        }
    }
}
