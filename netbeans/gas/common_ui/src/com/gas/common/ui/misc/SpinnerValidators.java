/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.misc;

import java.lang.ref.WeakReference;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
public class SpinnerValidators {

    public static class Linker implements ChangeListener {

        WeakReference<JSpinner> lowerRef;
        WeakReference<JSpinner> upperRef;

        public Linker(JSpinner lower, JSpinner upper) {
            lowerRef = new WeakReference<JSpinner>(lower);
            upperRef = new WeakReference<JSpinner>(upper);

            lowerRef.get().addChangeListener(this);
            upperRef.get().addChangeListener(this);
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            Object src = e.getSource();
            final SpinnerNumberModel modelLower = (SpinnerNumberModel) lowerRef.get().getModel();
            final SpinnerNumberModel modelUpper = (SpinnerNumberModel) upperRef.get().getModel();

            if (src == lowerRef.get()) {
                Object value = modelLower.getValue();
                if (value instanceof Integer) {
                    modelUpper.setMinimum((Integer) value);
                } else if (value instanceof Float) {
                    modelUpper.setMinimum((Float) value);
                } else if (value instanceof Double) {
                    modelUpper.setMinimum((Double) value);
                }
            } else if (src == upperRef.get()) {
                Object value = modelUpper.getValue();
                if (value instanceof Integer) {
                    modelLower.setMaximum((Integer) value);
                } else if (value instanceof Float) {
                    modelLower.setMaximum((Float) value);
                } else if (value instanceof Double) {
                    modelLower.setMaximum((Double) value);
                }
            }
        }
    }
}
