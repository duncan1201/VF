/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Utilities;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
public class DialogUtil {

    public static JDialog createDialog(final IVDialog comp, String title, int optionType) {
        return createDialog(comp, title, true, optionType);
    }

    public static int notify(final IVDialog comp, String title) {
        return notify(comp, title, JOptionPane.OK_CANCEL_OPTION);
    }

    public static int notify(final IVDialog comp, String title, int optionType) {
        int ret = 0;
        JDialog dialog = createDialog(comp, title, optionType);
        Rectangle r = Utilities.getUsableScreenBounds();
        int maxW = (r.width * 9) / 10;
        int maxH = (r.height * 9) / 10;
        Dimension d = dialog.getPreferredSize();
        d.width = Math.min(d.width, maxW);
        d.height = Math.min(d.height, maxH);
        dialog.setBounds(Utilities.findCenterBounds(d));        
        dialog.setVisible(true);

        ret = comp.getClosedButton();
        return ret;
    }

    public static void showAutoCloseMessagePane(String title, final String msg) {
        final WindowManager windowManager = WindowManager.getDefault();
        final JFrame frame = (JFrame) windowManager.getMainWindow();
        final AutoCloseDialog dialog = new AutoCloseDialog(frame, msg);
        final Timer timer = new Timer(3000, new ActionListener() {            
            @Override
            public void actionPerformed(ActionEvent e) {                
                ((Timer) e.getSource()).stop();
                dialog.setVisible(false);                
            }
        });
        timer.start();
        dialog.pack();
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    public static JDialog createDialog(final IVDialog comp, String title, boolean modal, int optionType) {
        final WindowManager windowManager = WindowManager.getDefault();
        final JFrame frame = (JFrame) windowManager.getMainWindow();
        final JDialog ret = new JDialog(frame, title, modal);
        ret.getContentPane().add((Component) comp, BorderLayout.CENTER);
        final ButtonPanel buttonPanel = createButtonPanel(optionType);
        ret.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        ret.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);


        ret.pack();
        return ret;
    }

    private static ButtonPanel createButtonPanel(int optionType) {
        ButtonPanel ret = new ButtonPanel(optionType, SwingConstants.RIGHT);
        return ret;
    }

    public static void error(List<String> msgs) {
        message(msgs, DialogDescriptor.ERROR_MESSAGE);
    }

    public static void message(List<String> msgs) {
        message(msgs, DialogDescriptor.INFORMATION_MESSAGE);
    }

    public static void message(List<String> msgs, int msgType) {
        StringBuilder ret = new StringBuilder();
        Iterator<String> itr = msgs.iterator();
        while (itr.hasNext()) {
            String str = itr.next();
            ret.append(str);
            ret.append("\n");
        }
        DialogDescriptor.Message me = new DialogDescriptor.Message(ret.toString(), msgType);
        DialogDisplayer.getDefault().notify(me);
    }
}
