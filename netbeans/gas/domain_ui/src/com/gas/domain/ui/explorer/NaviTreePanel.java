/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.explorer;

import com.gas.database.core.filesystem.service.api.IFolderService;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.filesystem.FolderHelper;
import com.gas.domain.core.filesystem.FolderNames;
import com.gas.domain.ui.banner.BannerTC;
import com.gas.common.ui.util.Pref;
import com.gas.domain.core.filesystem.FolderList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.LayoutManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class NaviTreePanel extends JPanel {

    private JTree tree;

    public NaviTreePanel() {
        super();
        setBackground(Color.WHITE);
        LayoutManager layout = new BorderLayout();
        setLayout(layout);

        tree = new JTree();        
        ToolTipManager.sharedInstance().registerComponent(tree);
        tree.setTransferHandler(new TreeTransferHandler());
        tree.setDropMode(DropMode.ON);
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setModel(createMyDataTreeModel());
        tree.setCellRenderer(new TreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        //expand();

        setupListeners();

    }

    private void expand() {
        int rowCount = tree.getRowCount();
        List<TreePath> toBeExpanded = new ArrayList<TreePath>();
        for (int row = 0; row < rowCount; row++) {
            TreePath path = tree.getPathForRow(row);
            toBeExpanded.add(path);
        }

        for (TreePath path : toBeExpanded) {
            tree.expandPath(path);
        }

        TreePath sampleDataTreePath = null;

        rowCount = tree.getRowCount();
        toBeExpanded.clear();
        for (int row = 0; row < rowCount; row++) {
            TreePath path = tree.getPathForRow(row);
            FolderMutableTreeNode node = (FolderMutableTreeNode) path.getLastPathComponent();
            Folder userObject = (Folder) node.getUserObject();
            if (userObject.getName().equals(FolderNames.SAMPLE_DATA)) {
                sampleDataTreePath = path;
                break;
            }
        }

        if (sampleDataTreePath != null) {
            tree.expandPath(sampleDataTreePath);
        }
    }
    
    private String getSelectedPathNoRoot(){
        String path = getSelectedPath();
        int i = path.indexOf(FolderNames.ROOT);
        return path.substring(i + FolderNames.ROOT.length());
    }
    
    /**
     * @return the path in the form of "\[name]\[name]\[name]"
     */
    private String getSelectedPath(){
        StringBuilder ret = new StringBuilder();
        TreePath treePath = tree.getSelectionPath();
        Object[] paths = treePath.getPath();
        for(Object o: paths){
            FolderMutableTreeNode n = (FolderMutableTreeNode)o;
            Folder folder = n.getUserObject();
            ret.append(Folder.separator);
            ret.append(folder.getName());            
        }
        return ret.toString();
    }

    public FolderMutableTreeNode getSelectedNode() {
        FolderMutableTreeNode ret = null;
        TreePath treePath = tree.getSelectionPath();
        if (treePath != null) {
            ret = (FolderMutableTreeNode) treePath.getLastPathComponent();
        }
        return ret;
    }

    public FolderMutableTreeNode getTreeNode(String folderPath) {
        FolderMutableTreeNode ret = null;
        DefaultTreeModel dtm = (DefaultTreeModel) tree.getModel();
        FolderMutableTreeNode fmtn = (FolderMutableTreeNode) dtm.getRoot();
        ret = fmtn.findNode(folderPath);
        return ret;
    }

    protected void removeNode(String folderPath) {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        FolderMutableTreeNode root = (FolderMutableTreeNode) model.getRoot();
        FolderMutableTreeNode node = getNode(root, folderPath);
        if (node != null) {
            model.removeNodeFromParent(node);
        }
    }

    protected void addNewFolder(Folder folder) {
        FolderMutableTreeNode parentNode = getSelectedNode();
        FolderMutableTreeNode childNode = new FolderMutableTreeNode(folder);
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();

        Integer index = parentNode.getInsertIndex(folder.getName());
        treeModel.insertNodeInto(childNode, parentNode,
                index);

        //Make sure the user can see the new node.
        tree.scrollPathToVisible(new TreePath(childNode.getPath()));
    }

    public void updateFolder(Folder folder) {
        String folderPath = folder.getAbsolutePath();
        FolderMutableTreeNode ret = null;
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        FolderMutableTreeNode treeNode = (FolderMutableTreeNode) treeModel.getRoot();
        ret = treeNode.findNode(folderPath);
        final Folder oldFolder = ret.getUserObject();
        final Set<String> oldNames = oldFolder.getChildrenNames();
        ret.setUserObject(folder);
        final Set<String> newNames = folder.getChildrenNames();
        if (ret != null) {
            if (!oldNames.equals(newNames)) {
                treeModel.nodeStructureChanged(ret);
            } else {
                treeModel.nodeChanged(ret);
            }
        }
    }

    public JTree getTree() {
        return tree;
    }

    public Folder getSelectedFolder() {
        String selectedPath = getSelectedPathNoRoot();
        Folder rootFolder = getRootFolder();
        Folder ret = rootFolder.getFolder(selectedPath);              
        return ret;
    }

    public void selectLastOpenFolder() {
        String folderStr = Pref.LastOpenFolderPref.getInstance().getLastOpenFolder();
        Folder folder = null;
        if (!folderStr.isEmpty()) {
            Folder rootFolder = getRootFolder();
            folder = rootFolder.getFolder(folderStr);
            if(folder == null){
                folder = getDefaultFolder();
            }
        } else {
            folder = getDefaultFolder();
        }
        // expand the "My Data" node
        Folder rootFolder = getRootFolder();
        Folder myDataRoot = rootFolder.getFolder(FolderHelper.getMyDataRoot().getAbsolutePath());
        if (myDataRoot.getChildCount() > 0) {
            FolderList leaves = FolderHelper.getLeaves(myDataRoot);
            if (!leaves.isEmpty()) {
                final Folder depest = leaves.getDepest();
                FolderMutableTreeNode treeNode = getTreeNode(depest.getAbsolutePath());                
                tree.scrollPathToVisible(new TreePath(treeNode.getPath()));
            }else{
                FolderMutableTreeNode treeNode = getTreeNode(myDataRoot.getAbsolutePath());
                tree.scrollPathToVisible(new TreePath(treeNode.getPath()));
            }
        }

        // expand the selected folder
        FolderMutableTreeNode treeNode = getTreeNode(folder.getAbsolutePath());
        tree.scrollPathToVisible(new TreePath(treeNode.getPath()));
        selectFolder(folder);

        // expand the NBCI folder
        String ncbiPath = FolderHelper.getNCBIRoot().getChild(FolderNames.NCBI_NUCLEOTIDE).getAbsolutePath();
        treeNode = getTreeNode(ncbiPath);
        tree.scrollPathToVisible(new TreePath(treeNode.getPath()));

    }

    public void selectFolder(Folder folder) {
        String folderStr = folder.getAbsolutePath();
        int rowCount = tree.getRowCount();
        TreePath treePath = null;
        for (int row = 0; row < rowCount; row++) {
            TreePath path = tree.getPathForRow(row);
            FolderMutableTreeNode node = (FolderMutableTreeNode) path.getLastPathComponent();
            Folder userObject = (Folder) node.getUserObject();
            if (userObject.getAbsolutePath().equals(folderStr)) {
                treePath = path;
                break;
            }
        }

        if (treePath != null) {
            tree.getSelectionModel().addSelectionPath(treePath);
        }
        BannerTC.getInstance().setSelectedFolder(folder);
        Pref.LastOpenFolderPref.getInstance().setLastOpenFolder(folder.getAbsolutePath());
    }

    private Folder getDefaultFolder() {
        Folder ret = null;
        Folder myDataFolder = getRootFolder().getChild(FolderNames.MY_DATA);
        List<Folder> leafFolder = FolderHelper.getLeaves(myDataFolder);
        Iterator<Folder> itr = leafFolder.iterator();
        while (itr.hasNext()) {
            ret = itr.next();
            if (!ret.getName().equals(FolderNames.RECYCLE_BIN)) {
                break;
            }
        }
        return ret;
    }

    private void setupListeners() {
        tree.addTreeSelectionListener(new NaviTreePanelListeners.TreeSelListener(this));

        tree.addMouseListener(new NaviTreePanelListeners.MouseApt(this));
    }

    private DefaultTreeModel createMyDataTreeModel() {
        IFolderService s = Lookup.getDefault().lookup(IFolderService.class);
        Folder folderTree = s.getFolderTree();
        FolderMutableTreeNode node = new FolderMutableTreeNode(folderTree);
        DefaultTreeModel ret = new DefaultTreeModel(node);
        return ret;
    }

    protected Folder getRootFolder() {
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        FolderMutableTreeNode node = (FolderMutableTreeNode) model.getRoot();
        Folder ret = getFolderRecursively(node);
        return ret;
    }

    private Folder getFolderRecursively(FolderMutableTreeNode node) {
        Folder ret = (Folder) node.getUserObject();
        for (int i = 0; i < node.getChildCount(); i++) {
            FolderMutableTreeNode childNode = node.getChildAt(i);
            Folder childFolder = getFolderRecursively(childNode);
            ret.addOrReplaceFolder(childFolder);
        }
        return ret;
    }

    private FolderMutableTreeNode getNode(FolderMutableTreeNode node, String folderPath) {
        FolderMutableTreeNode ret = null;
        Folder folder = node.getUserObject();
        if (folder.getAbsolutePath().equals(folderPath)) {
            return node;
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                FolderMutableTreeNode nodeChild = getNode(node.getChildAt(i), folderPath);
                if (nodeChild != null) {
                    ret = nodeChild;
                    break;
                }
            }
        }
        return ret;
    }
}
