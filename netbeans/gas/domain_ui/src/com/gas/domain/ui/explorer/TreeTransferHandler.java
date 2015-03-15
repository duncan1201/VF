/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.explorer;

import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.banner.IImport2FolderService;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class TreeTransferHandler extends TransferHandler {

    private ExplorerTC explorerTC;
    private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

    public TreeTransferHandler() {
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        boolean ret = true;
        if (!support.isDrop()) {
            ret = false;
            return ret;
        }

        JTree.DropLocation dropLocation =
                (JTree.DropLocation) support.getDropLocation();
        TreePath path = dropLocation.getPath();
        return path != null;
    }
    
    @Override
    public boolean importData(TransferHandler.TransferSupport support) {
        if (explorerTC == null) {
            explorerTC = UIUtil.findTopComponent(ExplorerTC.ID, ExplorerTC.class);
        }

        boolean ret = true;

        JTree.DropLocation dropLocation =
                (JTree.DropLocation) support.getDropLocation();
        TreePath path = dropLocation.getPath();
        FolderMutableTreeNode node = (FolderMutableTreeNode) path.getLastPathComponent();
        Integer folderId = ((Folder) node.getUserObject()).getHibernateId();
        final Folder toFolder = folderService.loadWithDataAndChildren(folderId);     
        if(toFolder.isNCBIFolder()){
            String msg = "Cannot move data to NCBI folders";
            DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
            m.setTitle("Cannot move data");
            DialogDisplayer.getDefault().notify(m);
            return ret;
        }
        
        IImport2FolderService import2FolderService = Lookup.getDefault().lookup(IImport2FolderService.class);
        ret = import2FolderService.import2Folder(support, toFolder, true);
        return ret;
    }
}
