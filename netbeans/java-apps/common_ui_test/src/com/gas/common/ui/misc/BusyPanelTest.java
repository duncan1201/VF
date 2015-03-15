package com.gas.common.ui.misc;

import com.gas.common.ui.button.DropDownButton;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class BusyPanelTest {

    public static void main(String[] args) {
        JFrame frame = new JFrame("BusyPanelTest1");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        JPanel centerPanel = new JPanel(new CardLayout());
        panel.setLayout(new BorderLayout());

        JButton btn = new JButton("Press me");

        final BusyPanel busyPanel = new BusyPanel();
        centerPanel.add(busyPanel, "Test");
        centerPanel.add(new JButton(), "testa");
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(btn, BorderLayout.SOUTH);
        btn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyPanel.setBusy(!busyPanel.isBusy());
            }
        });


        frame.setContentPane(panel);

        frame.pack();

        frame.setVisible(true);
    }
}