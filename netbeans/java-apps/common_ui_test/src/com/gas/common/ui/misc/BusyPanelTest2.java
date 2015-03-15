package com.gas.common.ui.misc;

import com.gas.common.ui.button.DropDownButton;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BusyPanelTest2 {

    public static void main(String[] args) {
        JFrame frame = new JFrame("BusyPanelTest1");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton btn = new JButton("Press me");

        final BusyPanel2 busyPanel2 = new BusyPanel2();
        panel.add(busyPanel2, BorderLayout.CENTER);
        panel.add(btn, BorderLayout.SOUTH);
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyPanel2.setBusy(!busyPanel2.isBusy());
            }
        });


        frame.setContentPane(panel);

        frame.pack();

        frame.setVisible(true);
    }
}