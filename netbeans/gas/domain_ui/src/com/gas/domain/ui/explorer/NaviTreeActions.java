/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.explorer;

import com.gas.common.ui.core.StringList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.InputPanel;
import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.filesystem.FolderNames;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.domain.ui.banner.TableActions;
import com.gas.domain.ui.dynamicTable.DynamicTable;
import com.gas.common.ui.util.Pref;
import com.gas.common.ui.util.StrUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.domain.core.IFolderElementList;
import com.gas.domain.core.ren.RENList;
import com.gas.database.core.api.IDomainUtil;
import com.gas.domain.ui.IFolderPanel;
import com.gas.domain.ui.banner.IRestoreService;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
public class NaviTreeActions {

    static IAnnotatedSeqService asService = Lookup.getDefault().lookup(IAnnotatedSeqService.class);

    protected static class NewAction extends AbstractAction {

        private NaviTreePanel naviTreePanel;
        private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);

        public NewAction() {
            super("New...", ImageHelper.createImageIcon(ImageNames.FOLDER_ADD_16));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JTree tree = naviTreePanel.getTree();
            TreePath treePath = tree.getSelectionPath();
            if (treePath == null) {
                return;
            }
            FolderMutableTreeNode selectedNode = naviTreePanel.getSelectedNode();

            String folderName;
            InputPanel inputPanel = new InputPanel("New Folder Name:");
            inputPanel.setForbiddenChars(Folder.forbiddenChars);
            DialogDescriptor dd = new DialogDescriptor(inputPanel, "New Folder");
            final List<String> existingNames = selectedNode.getChildNames();
            inputPanel.setDialogDescriptor(dd);
            inputPanel.setExistingNames(existingNames.toArray(new String[existingNames.size()]));
            inputPanel.validateInput();

            Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);
            if (answer.equals(DialogDescriptor.OK_OPTION)) {
                folderName = inputPanel.getInputText();
                Folder folder = new Folder(folderName);
                FolderMutableTreeNode parentNode = naviTreePanel.getSelectedNode();
                Folder parentFolder = (Folder) parentNode.getUserObject();
                parentFolder = folderService.loadWithChildren(parentFolder.getHibernateId());
                parentFolder.addFolder(folder);
                folderService.merge(parentFolder);
                Integer hId = folder.getHibernateId();
                Folder updatedFolder = folderService.loadWithDataAndParentAndChildren(hId);
                Folder updatedParentFolder = folderService.loadWithDataAndParentAndChildren(parentFolder.getHibernateId());
                naviTreePanel.updateFolder(updatedParentFolder);
                naviTreePanel.addNewFolder(updatedFolder);

                BannerTC.getInstance().updateFolder(updatedFolder, false);

            }
        }

        public NaviTreePanel getNaviTreePanel() {
            return naviTreePanel;
        }

        public void setNaviTreePanel(NaviTreePanel naviTreePanel) {
            this.naviTreePanel = naviTreePanel;
        }
    }

    protected static class RenameAction extends AbstractAction {

        private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        private NaviTreePanel naviTreePanel;

        public RenameAction() {
            super("Rename...");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            TreePath treePath = naviTreePanel.getTree().getSelectionPath();
            if (treePath == null) {
                return;
            }
            

            FolderMutableTreeNode treeNode = (FolderMutableTreeNode) treePath.getLastPathComponent();
            Folder folder = (Folder) treeNode.getUserObject();
            FolderMutableTreeNode selectedNode = naviTreePanel.getSelectedNode();
            FolderMutableTreeNode parentNode = (FolderMutableTreeNode)selectedNode.getParent();
            List<String> existingNames ;
            if(parentNode != null){
                existingNames = parentNode.getChildNames();
                while(existingNames.indexOf(folder.getName()) > -1){
                    existingNames.remove(folder.getName());
                }
            }else{
                existingNames = new ArrayList<String>();
            }

            InputPanel inputPanel = new InputPanel("New Folder Name:");
            inputPanel.setExistingNames(existingNames);
            inputPanel.setInitInputText(folder.getName());
            inputPanel.setForbiddenChars(Folder.forbiddenChars);
            DialogDescriptor dd = new DialogDescriptor(inputPanel, "Rename Folder");
            inputPanel.setDialogDescriptor(dd);
            inputPanel.validateInput();
            Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);
            if (answer.equals(DialogDescriptor.OK_OPTION)) {

                String newName = inputPanel.getInputText();
                folderService.renameFolder(folder, newName);

                DefaultTreeModel treeModel = (DefaultTreeModel) naviTreePanel.getTree().getModel();
                treeModel.nodeChanged(treeNode);
                Pref.LastOpenFolderPref.getInstance().setLastOpenFolder(folder.getAbsolutePath());
            }
        }

        public void setNaviTreePanel(NaviTreePanel naviTreePanel){
            this.naviTreePanel = naviTreePanel;            
        }
    }

    protected static class RestoreAllAction extends AbstractAction {

        public RestoreAllAction() {
            super("Restore all items");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Folder rootFolder = ExplorerTC.getInstance().getRootFolder();

            Folder recycleBin = rootFolder.getChild(FolderNames.MY_DATA).getChild(FolderNames.RECYCLE_BIN);
            IRestoreService restoreService = Lookup.getDefault().lookup(IRestoreService.class);
            restoreService.restore(recycleBin.getElements());
        }
    }

    protected static class EmptyRecycleBinAction extends AbstractAction {

        private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        private JTree tree;
        private ExplorerTC explorerTC;
        private static final String TITLE = "Empty Recycle Bin";

        public EmptyRecycleBinAction() {
            this(null);
        }

        public EmptyRecycleBinAction(JTree tree) {
            super(TITLE, ImageHelper.createImageIcon(ImageNames.TRASH_BOX_16));
            this.tree = tree;
        }

        public void setTree(JTree tree) {
            this.tree = tree;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (explorerTC == null) {
                explorerTC = UIUtil.findTopComponent(ExplorerTC.ID, ExplorerTC.class);
            }
            DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
            TreePath treePath = tree.getSelectionPath();
            if (treePath == null) {
                return;
            }
            FolderMutableTreeNode treeNode = (FolderMutableTreeNode) treePath.getLastPathComponent();
            MutableTreeNode parent = (MutableTreeNode) (treeNode.getParent());
            if (parent != null) {
                Folder folder = (Folder) treeNode.getUserObject();
                String msg = "Are you sure you want to empty the recycle bin?";
                DialogDescriptor.Confirmation cf = new DialogDescriptor.Confirmation(msg, TITLE, DialogDescriptor.WARNING_MESSAGE, DialogDescriptor.YES_NO_OPTION);
                Integer answer = (Integer) DialogDisplayer.getDefault().notify(cf);
                if (answer.equals(DialogDescriptor.OK_OPTION)) {
                    folder.clearContents();
                    folderService.merge(folder);
                    folder = folderService.loadWithDataAndParentAndChildren(folder.getHibernateId());
                    explorerTC.updateFolder(folder);
                    BannerTC.getInstance().updateFolder(folder, true);
                }
            }
        }
    }

    protected static class DelAction extends AbstractAction {

        private IFolderService folderService = Lookup.getDefault().lookup(IFolderService.class);
        private JTree tree;
        private IDomainUtil domainUtil = Lookup.getDefault().lookup(IDomainUtil.class);

        public DelAction() {
            this(null);
        }

        public DelAction(JTree tree) {
            super("Permanently delete ", ImageHelper.createImageIcon(ImageNames.FOLDER_DELETE_16));
            this.tree = tree;
        }

        public void setTree(JTree tree) {
            this.tree = tree;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
            TreePath treePath = tree.getSelectionPath();
            if (treePath == null) {
                return;
            }
            final FolderMutableTreeNode treeNode = (FolderMutableTreeNode) treePath.getLastPathComponent();
            final FolderMutableTreeNode parentNode = (FolderMutableTreeNode) (treeNode.getParent());
            if (parentNode != null) {
                final Folder parentFolder = parentNode.getUserObject();
                final Folder folder = (Folder) treeNode.getUserObject();

                final String msg = String.format("<html>Are you sure you want to PERMANENTLY delete the following folder and its contents?%s</html>", StrUtil.toHtmlList(folder.getName()));
                String title = String.format("Permanently delete folder \"%s\"", folder.getName());
                DialogDescriptor.Confirmation cf = new DialogDescriptor.Confirmation(msg, title, DialogDescriptor.WARNING_MESSAGE, DialogDescriptor.YES_NO_OPTION);
                Integer answer = (Integer) DialogDisplayer.getDefault().notify(cf);
                if (answer.equals(DialogDescriptor.OK_OPTION)) {
                    Frame frame = WindowManager.getDefault().getMainWindow();
                    ProgressHelper.showProgressDialogAndRun(frame, new ProgRunnable() {
                        
                        boolean success = false;
                        
                        @Override
                        public void run(ProgressHandle handle) {
                            handle.setIndeterminate(true);
                            handle.progress("Preparing to delete...");
                            IFolderElementList fes = folder.getElements();
                            int beforeSize = fes.size();
                            domainUtil.checkDesendants(fes);
                            int afterSize = fes.size();
                            if (beforeSize != afterSize) {
                                return;
                            }
                            handle.progress("Deleting folder...");
                            treeModel.removeNodeFromParent(treeNode);
                            domainUtil.closeEditors(folder.getElements());
                            folderService.delete(folder);
                            success = true;
                        }

                        @Override
                        public void done(ProgressHandle handle) {
                            if(!success){
                                return;
                            }
                            BannerTC.getInstance().deleteFolder(folder);
                            IFolderPanel folderPanel = BannerTC.getInstance().getFolderPanel();

                            //BannerTC.getInstance().showInfoPanel();

                            Folder parentFolderUpdated = folderService.loadWithDataAndParentAndChildren(parentFolder.getHibernateId());
                            ExplorerTC.getInstance().updateFolder(parentFolderUpdated);
                            if (folderPanel != null) {
                                Folder selectedFolder = folderPanel.getFolder();
                                ExplorerTC.getInstance().setSelectedFolder(selectedFolder.getHibernateId());
                            }
                            Pref.LastOpenFolderPref.getInstance().setLastOpenFolder("");
                        }
                    }, "Delete Folder");
                }
            }
        }
    }

    protected static class PasteAction extends AbstractAction {

        private TableActions.PasteAction pasteAction;
        private WeakReference<DynamicTable> tableRef;

        public PasteAction() {
            super("Paste", ImageHelper.createImageIcon(ImageNames.PASTE_16));
            Folder folder = ExplorerTC.getInstance().getSelectedFolder();
            DynamicTable table = BannerTC.getInstance().getFolderPanel(folder).getTable();
            tableRef = new WeakReference<DynamicTable>(table);
            pasteAction = new TableActions.PasteAction();
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK);
            KeyEvent ke = UIUtil.toKeyEvent(tableRef.get(), keyStroke);
            SwingUtilities.notifyAction(pasteAction, keyStroke, ke, tableRef.get(), keyStroke.getModifiers());
        }
    }
}
