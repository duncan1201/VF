/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.tooltip;

import com.gas.common.core.util.CommonUtil;
import com.gas.common.ui.util.ColorCnst;
import com.gas.common.ui.util.ColorUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;

import java.awt.Dialog.ModalityType;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.ToolTipManager;
import javax.swing.WindowConstants;
import org.jdesktop.swingx.JXHyperlink;

public class TooltipSample {

    static JFrame frame;
    static MyJButton button;
    
    public static void main(String args[]) {
        // Get current delay
        int dismissDelay = ToolTipManager.sharedInstance().getDismissDelay();

        // Keep the tool tip showing
        dismissDelay = Integer.MAX_VALUE;
        ToolTipManager.sharedInstance().setDismissDelay(dismissDelay);

        String title = "Tooltip Sample";
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = frame.getContentPane();
        JPanel panel = new JPanel();
        panel.setToolTipText("<HtMl>Tooltip<br>Message");
        container.add(panel, BorderLayout.CENTER);

        button = new MyJButton() ;
        
        button.setHtmlToolTip(false);
        button.setBounds(10, 10, 100, 50);
        //button.setToolTipHtml("<html><b>TESTS</b>again<br/>second line\t tab</html>");
        button.setRichToolTip(button.getRichToolTip());
        JXHyperlink link = new JXHyperlink();

        link.setURI(CommonUtil.getURI("http://www.google.com"));
        link.setText("Click here"); 
        frame.getContentPane().add(link, BorderLayout.CENTER);

        frame.setSize(300, 150) ;
        frame.setLocation(100, 100) ;
        frame.setVisible(true);

        
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    
    private static JDialog createJDialog(){
        JDialog ret = new JDialog(null, ModalityType.MODELESS);
        ret.setContentPane(createContents());
        ret.setUndecorated(true);
        ret.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        //ret.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        return ret;
    }
    
    private static JPanel createContents(){
        JPanel ret = new JPanel();
        ret.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        ret.setBackground(Color.red);
        ret.setLayout(new BorderLayout());       
        JTextArea field = new JTextArea("<b>WEST</b>");        
        field.setBackground(ColorCnst.TOOLTIP_BG);
        field.setEditable(false);
        ret.add(new JButton("ABC"), BorderLayout.WEST);
        ret.add(new JButton("EAST"), BorderLayout.EAST);
        ret.add(field, BorderLayout.CENTER);
        return ret;
    }
}
