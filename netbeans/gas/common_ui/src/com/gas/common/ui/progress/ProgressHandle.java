/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.progress;

import javax.swing.JDialog;

/**
 *
 * @author dq
 */
public final class ProgressHandle {

    JDialog dialog;
    ProgressBarPanel progressBarPanel;

    void finish() {
        dialog.setVisible(false);
        dialog.dispose();
    }

    /**
     * @param workunit max 100
     */
    public final void progress(int workunit) {
        if (!progressBarPanel.progressBar.isIndeterminate()) {
            progressBarPanel.progressBar.setValue(Math.min(progressBarPanel.progressBar.getMaximum(), workunit));
        }
    }

    public final void progress(String message) {
        progressBarPanel.msgMain.setText(message);
    }

    public final void setIndeterminate(boolean indeterminate) {
        progressBarPanel.progressBar.setIndeterminate(indeterminate);
    }
}
