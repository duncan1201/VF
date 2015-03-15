/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import com.gas.domain.ui.IFolderPanel;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.filesystem.FolderHelper;
import com.gas.domain.ui.INCBIFolderPanelFactory;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class FolderPanelFactory {

    public static IFolderPanel createFolderPanel(Folder folder) {
        if (folder == null) {
            throw new IllegalArgumentException("Folder cannot be null");
        }
        IFolderPanel ret = null;

        boolean isMyDataFolder = FolderHelper.isMyDataFolder(folder);

        boolean isNCBIFolder = folder.isNCBIFolder();


        if (isMyDataFolder) {
            ret = createCardPanel(folder);
        } else if (isNCBIFolder) {
            ret = createNCBIPanel(folder);
        } else {
            throw new IllegalArgumentException(String.format("Unknown folder '%s'", folder.getName()));
        }

        return ret;
    }

    private static IFolderPanel createCardPanel(Folder folder) {
        MyDataFolderPanel ret = new MyDataFolderPanel(folder);
        return ret;
    }

    private static IFolderPanel createNCBIPanel(Folder folder) {
        IFolderPanel ret = null;
        INCBIFolderPanelFactory ncbiPanelFactory = Lookup.getDefault().lookup(INCBIFolderPanelFactory.class);
        ret = ncbiPanelFactory.create();
        ret.setFolder(folder);
        return ret;
    }
}
