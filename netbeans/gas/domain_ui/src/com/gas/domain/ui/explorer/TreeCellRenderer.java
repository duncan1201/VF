/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.explorer;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.filesystem.FolderHelper;
import com.gas.domain.core.filesystem.FolderNames;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *
 * @author dq
 */
public class TreeCellRenderer extends DefaultTreeCellRenderer {

    private final static String BOLD_TPL = "<html>%s(<b>%d</b>/%d)</html>";
    private final static String NORMAL_TPL = "%s(%d)";
    private final static String BOLD_TOOLTIP_TPL = "<html>%s(<b>%d<b/>/%d)<br/></html>";
    private final static String TOOLTIP_TPL = "<html>%s(%d)</html>";

    @Override
    public Component getTreeCellRendererComponent(
            JTree tree,
            Object value,
            boolean sel,
            boolean expanded,
            boolean leaf,
            int row,
            boolean hasFocus) {

        FolderMutableTreeNode node = (FolderMutableTreeNode) value;
        Folder folder = (Folder) node.getUserObject();
        boolean isRecycleBin = folder.isRecycleBin();
        boolean isNCBI = folder.isNCBIFolder();
        boolean isMyDataFolder = FolderHelper.isMyDataFolder(folder);

        super.getTreeCellRendererComponent(
                tree, value, sel,
                expanded, leaf, row,
                hasFocus);
        Icon icon = null;

        if (isRecycleBin) {
            icon = ImageHelper.createImageIcon(ImageNames.TRASH_BOX_16);
        } else if (isNCBI) {
            if (folder.getName().equals(FolderNames.NCBI_ROOT)) {
                icon = ImageHelper.createImageIcon(ImageNames.NCBI);
            } else if (folder.getName().equals(FolderNames.NCBI_NUCLEOTIDE)) {
                icon = ImageHelper.createImageIcon(ImageNames.DB_NUCLEOTIDE_16);
            } else if (folder.getName().equals(FolderNames.NCBI_PROTEIN)) {
                icon = ImageHelper.createImageIcon(ImageNames.DB_PROTEIN_16);
            } else if (folder.getName().equals(FolderNames.NCBI_PUBMED)) {
                icon = ImageHelper.createImageIcon(ImageNames.DB_PUBLICATION_16);
            } else if (folder.getName().equals(FolderNames.NCBI_STRUCTURE)) {
                icon = ImageHelper.createImageIcon(ImageNames.DB_STRUCTURE_16);
            } else if (folder.getName().equals(FolderNames.NCBI_GENOME)) {
                icon = ImageHelper.createImageIcon(ImageNames.DB_NUCLEOTIDE_16);
            } else {
                icon = ImageHelper.createImageIcon(ImageNames.FOLDER_16);
            }
        } else {
            icon = ImageHelper.createImageIcon(ImageNames.FOLDER_16);
        }
        setIcon(icon);

        int folderCount = folder.getCount();
        int unreadCount = folder.getUnreadCount();
        if (folderCount > 0 && unreadCount > 0) {
            setText(String.format(BOLD_TPL, folder.getName(), unreadCount, folderCount));
        } else if (folderCount > 0) {
            setText(String.format(NORMAL_TPL, folder.getName(), folderCount));
        } else {
            setText(folder.getName());
        }
        if (isMyDataFolder) {
            if (unreadCount == 0) {
                setToolTipText(String.format(TOOLTIP_TPL, folder.getName(), folderCount));
            } else {
                setToolTipText(String.format(BOLD_TOOLTIP_TPL, folder.getName(), unreadCount, folderCount));
            }
        } else if (isNCBI) {
            setToolTipText(folder.getName());
        }



        return this;
    }
}
