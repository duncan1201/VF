/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.progress;

import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.SwingWorker;

/**
 *
 * @author dq
 */
public class ProgressHelper {

    public static void showProgressDialogAndRun(Frame parentFrame, final ProgRunnable operation,
            String titleDialog) {
        showProgressDialogAndRun(parentFrame, null, operation, titleDialog);
    }

    public static void showProgressDialogAndRun(Frame parentFrame, String prototypeMsg, final ProgRunnable operation,
            String titleDialog) {

        final JDialog dialog = new JDialog(parentFrame, titleDialog, true);
        dialog.setResizable(false);
        //dialog.setUndecorated(true);    
        ProgressBarPanel progressBarPanel = new ProgressBarPanel();
        if (prototypeMsg != null) {
            progressBarPanel.setPrototypeMessage(prototypeMsg);
        }
        dialog.getContentPane().add(progressBarPanel);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                dialog.setVisible(true);
            }
        });
        t.start();

        final ProgressHandle handle = new ProgressHandle();
        handle.dialog = dialog;
        handle.progressBarPanel = progressBarPanel;
        SwingWorker w = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                operation.run(handle);
                handle.finish();
                return null;
            }

            @Override
            public void done() {
                operation.done(handle);
            }
        };
        w.execute();

    }
}
