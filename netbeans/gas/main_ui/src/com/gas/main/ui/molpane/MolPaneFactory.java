/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import java.util.*;
import javax.swing.SwingWorker;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author dq
 */
@ServiceProvider(service = IMolPaneFactory.class)
public class MolPaneFactory implements IMolPaneFactory {

    private List<MolPane> molPanes = Collections.synchronizedList(new ArrayList<MolPane>());
    private int cacheSize = 4;
    private Object lock = new Object();

    public MolPaneFactory() {
        init();
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }
    
    private void init() {
        createSynchronous();
    }

    private void createAsynchronous() {
        synchronized (lock) {
            Worker worker = new Worker();
            worker.execute();
        }
    }

    private void createSynchronous() {
        while (molPanes.size() < cacheSize) {
            MolPane molPane = MolPane.createInstance();
            molPanes.add(molPane);            
        }
    }

    private class Worker extends SwingWorker {

        @Override
        protected Object doInBackground() throws Exception {
            while (molPanes.size() <= cacheSize) {
                MolPane molPane = MolPane.createInstance();
                molPanes.add(molPane);
            }
            return null;
        }
    }

    @Override
    public MolPane getMolPane() {
        MolPane ret = null;
        if (molPanes.isEmpty()) {
            MolPane molPane = MolPane.createInstance();
            molPanes.add(molPane);
        }
        ret = molPanes.remove(0);
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                createAsynchronous();
            }
        }, 4500);
        
        
        return ret;
    }
}
