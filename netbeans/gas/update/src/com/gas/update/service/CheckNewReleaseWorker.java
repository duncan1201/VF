/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.update.service;

import com.gas.update.service.api.SoftProductList;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import org.openide.util.Exceptions;

/**
 *
 * @author dq
 */
class CheckNewReleaseWorker extends SwingWorker<SoftProductList, Void> {

    boolean quiteOnNoUpdates;
    SoftProductService svc;
    
    CheckNewReleaseWorker(){
        this(true);
    }
    
    CheckNewReleaseWorker(boolean quiteOnNoUpdates){
        this.quiteOnNoUpdates = quiteOnNoUpdates;
    }
    
    @Override
    protected SoftProductList doInBackground() throws Exception {
        svc = new SoftProductService();
        SoftProductList ret = svc.getOSCompatibleProducts();
        return ret;
    }

    @Override
    protected void done() {    
        try {
            SoftProductList list = get();
            svc.displayResult(list, quiteOnNoUpdates);                    
        } catch (InterruptedException ex) {
            Exceptions.printStackTrace(ex);
        } catch (ExecutionException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
};
