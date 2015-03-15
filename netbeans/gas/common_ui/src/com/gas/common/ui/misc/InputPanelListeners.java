/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.lang.ref.WeakReference;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author dq
 */
class InputPanelListeners {

    static class FieldListener implements DocumentListener {

        WeakReference<InputPanel> newSettingPanelRef;

        FieldListener(InputPanel newSettingPanel) {
            newSettingPanelRef = new WeakReference<InputPanel>(newSettingPanel);
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            validateInput();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            validateInput();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            validateInput();
        }

        void validateInput() {
            newSettingPanelRef.get().validateInput();
        }
    }
}
