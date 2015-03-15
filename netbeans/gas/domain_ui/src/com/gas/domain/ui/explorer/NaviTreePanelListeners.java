/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.explorer;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.filesystem.FolderHelper;
import com.gas.domain.ui.banner.BannerTC;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

/**
 *
 * @author dq
 */
class NaviTreePanelListeners {

    static class TreeSelListener implements TreeSelectionListener {

        private WeakReference<NaviTreePanel> ref;

        TreeSelListener(NaviTreePanel naviTreePanel) {
            ref = new WeakReference<NaviTreePanel>(naviTreePanel);
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath treePath = e.getNewLeadSelectionPath();
            if (treePath == null) {
                return;
            }

            FolderMutableTreeNode node = (FolderMutableTreeNode) treePath.getLastPathComponent();
            Folder folder = node.getUserObject();
            boolean isNCBIRoot = FolderHelper.isNCBIRoot(folder);
            if (!isNCBIRoot) {
                ref.get().selectFolder(folder);
            }else{
                Folder folderBanner = BannerTC.getInstance().getFolderPanel().getFolder();
                ref.get().selectFolder(folderBanner);
            }
        }
    }

    static class MouseApt extends MouseInputAdapter {

        private NaviTreePanel naviTreePanel;

        MouseApt(NaviTreePanel naviTreePanel) {
            this.naviTreePanel = naviTreePanel;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            boolean isPopupTrigger = e.isPopupTrigger();
            if (isPopupTrigger) {
                triggerPopup(e);
            }
        }

        private void triggerPopup(MouseEvent e) {
            Point p = e.getLocationOnScreen();
            SwingUtilities.convertPointFromScreen(p, naviTreePanel);
            JTree tree = naviTreePanel.getTree();
            Folder selectedFolder = naviTreePanel.getSelectedFolder();
            if (selectedFolder == null) {
                return;
            }

            boolean isMyDataRoot = FolderHelper.isMyDataRoot(selectedFolder);
            boolean isNCBI = selectedFolder.isNCBIFolder();
            if (isNCBI) {
                return;
            }
            boolean recyleBin = selectedFolder.isRecycleBin();

            NaviTreePopup treePopup = new NaviTreePopup();

            treePopup.setNaviTreePanel(naviTreePanel);
            treePopup.updateEnablement(isMyDataRoot, recyleBin);
            
            UIUtil.showPopupMenu(treePopup, naviTreePanel, p.x, p.y);
        }
    }
}
