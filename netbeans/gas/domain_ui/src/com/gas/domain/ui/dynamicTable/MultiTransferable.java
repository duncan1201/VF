/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.dynamicTable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.openide.util.Exceptions;
import org.openide.util.datatransfer.ExTransferable;
import org.openide.util.datatransfer.MultiTransferObject;

/**
 *
 * @author dq
 */
public class MultiTransferable extends ExTransferable.Multi {

    public MultiTransferable(Transferable[] tfs) {
        super(tfs);
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        boolean ret = super.isDataFlavorSupported(flavor);
        return ret;
    }

    public MultiTransferObject getMultiTransferObject() {
        MultiTransferObject ret = null;
        try {
            ret = (MultiTransferObject) getTransferData(getTransferDataFlavors()[0]);
        } catch (UnsupportedFlavorException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return ret;
    }
}
