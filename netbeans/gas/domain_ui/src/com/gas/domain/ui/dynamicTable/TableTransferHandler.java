/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.dynamicTable;

import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.explorer.ExplorerTC;
import com.gas.domain.ui.banner.IImport2FolderService;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class TableTransferHandler extends TransferHandler {

    private static int action;

    public TableTransferHandler() {
    }

    @Override
    protected MultiTransferable createTransferable(JComponent c) {
        DynamicTable table = (DynamicTable) c;
        MultiTransferable tt = table.createMultiTransferable();
        return tt;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        boolean ret = super.canImport(support);
        return ret;
    }

    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        boolean ret = super.canImport(comp, transferFlavors);
        return ret;
    }

    @Override
    public void exportToClipboard(JComponent comp, Clipboard clip, int action) {
        super.exportToClipboard(comp, clip, action);
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY | MOVE;
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        TableTransferHandler.action = action;        
    }

    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        final Folder toFolder = ExplorerTC.getInstance().getSelectedFolder();
        if(toFolder == null || toFolder.isNCBIFolder() || toFolder.isRecycleBin()){
            return true;
        }
        IImport2FolderService import2FolderService = Lookup.getDefault().lookup(IImport2FolderService.class);
        boolean ret = import2FolderService.import2Folder(support, toFolder, TableTransferHandler.action == TransferHandler.MOVE);
        return ret;                
    }
}
