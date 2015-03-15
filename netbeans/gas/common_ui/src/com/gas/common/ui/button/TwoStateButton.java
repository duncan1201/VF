/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.button;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;

/**
 *
 * @author dq
 */
public class TwoStateButton extends JButton {

    private Map<String, ActionListener> listeners = new HashMap<String, ActionListener>();
    private String state1;
    private String state2;

    public TwoStateButton(String state1, String state2) {
        super(state1);
        this.state1 = state1;
        this.state2 = state2;
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ActionListener l = listeners.get(getText());
                l.actionPerformed(null);
                if (getText().equalsIgnoreCase(getState1())) {
                    setText(getState2());
                } else {
                    setText(getState1());
                }
            }
        });
    }

    public String getState1() {
        return state1;
    }

    public String getState2() {
        return state2;
    }

    public void addActionListener(String text, ActionListener listener) {
        listeners.put(text, listener);
    }
}
