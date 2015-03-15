/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui;

import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.dynamicTable.DynamicTable;

/**
 *
 * @author dq
 */
public interface IFolderPanel {

    Folder getFolder();

    String getAbsolutePath();

    void setFolder(Folder folder);

    DynamicTable getTable();

    String getStatusLine();

    void setStatusLine(String statusLine);

    Boolean getBusy();

    void setBusy(Boolean busy);

    boolean isNCBIFolder();

    void updateFolder(Folder folder);
    
    boolean unselectRow(IFolderElement obj);
    
    boolean checkRow(IFolderElement obj, boolean checked);
    
    boolean setSelected(IFolderElement fe);
}
