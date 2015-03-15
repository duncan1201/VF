/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import com.gas.domain.core.filesystem.Folder;
import javax.swing.TransferHandler;

/**
 *
 * @author dq
 */
public interface IImport2FolderService {
    boolean import2Folder(TransferHandler.TransferSupport support, final Folder toFolder, final boolean move);
}
