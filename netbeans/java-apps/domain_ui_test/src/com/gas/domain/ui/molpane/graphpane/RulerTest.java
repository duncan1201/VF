package com.gas.domain.ui.molpane.graphpane;

import com.gas.common.ui.ruler.Ruler;
import com.gas.common.ui.util.FontUtil;
import com.gas.common.core.misc.Loc;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class RulerTest extends JFrame {

// Swing components
    private Ruler ruler = new Ruler();
    private JColorChooser colorChooser;
    private JColorChooser highlightColorChooser;
// Constructor

    public RulerTest(String appName) {
        // Everything is as usual here
        super(appName);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        //panel.setLayout(new BorderLayout());
        panel.setLayout(null);
        panel.add(ruler, BorderLayout.CENTER);

        ruler.setLoc(new Loc(1, 200));
        //ruler.setBorder(BorderFactory.createLineBorder(Color.yellow, 2));
        //ruler.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        ruler.setTextFont(FontUtil.getDefaultMSFont().deriveFont(10.0f));
        int desiredHeight = ruler.getDesiredHeight();
        ruler.setBounds(0, 0, 600, desiredHeight);


        Font oldFont = panel.getFont();
        Font newFont = oldFont.deriveFont(12.0f);


        JLabel eastLabel = new JLabel();
        Font font = eastLabel.getFont();
        Font derivedFont = font.deriveFont(30.0f);
        panel.setPreferredSize(new Dimension(200, 200));
        this.setContentPane(panel);
        //this.pack();

        Insets insets = ruler.getInsets();
        Dimension size = ruler.getSize();
        System.out.println(insets);
        System.out.println(size);
        System.out.println(ruler.getBounds().getSize());
        System.out.println(ruler.getBounds().getLocation());
      
    }

// The entry point of the application
    public static void main(String[] args) {
        RulerTest writer = new RulerTest("SVG Writer");
        writer.setVisible(true);
    }
}
