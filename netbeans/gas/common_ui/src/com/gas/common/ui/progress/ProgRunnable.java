/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.common.ui.progress;

/**
 *
 * @author dq
 */
public interface ProgRunnable {

    /**
     * For time-consuming code that is not suitable to be executed in the event
     * dispatch code
     */
    public void run(ProgressHandle handle);

    /**
     * To be executed in the event dispatch thread
     */
    public void done(ProgressHandle handle);
}
