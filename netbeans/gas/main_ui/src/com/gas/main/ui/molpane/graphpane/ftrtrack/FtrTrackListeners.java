/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.graphpane.ftrtrack;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 *
 * @author dq
 */
public class FtrTrackListeners {

    private static Logger log = Logger.getLogger(FtrTrackListeners.class.getName());
    
    static class CompListener implements ComponentListener {

        Timer timer;

        @Override
        public void componentResized(ComponentEvent e) {
            abc(e);
        }

        @Override
        public void componentMoved(ComponentEvent e) {
        }

        @Override
        public void componentShown(ComponentEvent e) {
        }

        @Override
        public void componentHidden(ComponentEvent e) {
        }

        private void abc(ComponentEvent e) {
            if(timer == null){
                createTimer(e);
            }

            timer.restart();
        }

        private synchronized void createTimer(final ComponentEvent ce) {
            timer = new Timer(80, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FtrTrack ft = (FtrTrack) ce.getSource();
                    log.finer(ft.getTrackName() + ":actionPerformed");                    
                    ft.revalidate();
                }
            });
            timer.setRepeats(false);
        }
    }
}
