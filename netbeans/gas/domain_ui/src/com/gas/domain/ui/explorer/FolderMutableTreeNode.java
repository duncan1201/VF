/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.explorer;

import com.gas.common.ui.core.StringComparator;
import com.gas.common.ui.util.SetUtil;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.filesystem.FolderComparators;
import java.util.*;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 *
 * @author dq
 */
public class FolderMutableTreeNode extends DefaultMutableTreeNode {

    public FolderMutableTreeNode(Object obj) {
        this(obj, true);
    }

    public FolderMutableTreeNode(Object obj, boolean allowsChildren) {
        super(obj, allowsChildren);
        createChildren();
    }

    public boolean isNamePresent(String folderName) {
        List<String> names = getChildNames();
        return names.indexOf(folderName) > -1;
    }

    public Integer getInsertIndex(String folderName) {
        Integer ret = null;
        List<String> names = getChildNames();
        if (!names.contains(folderName)) {
            names.add(folderName);
            Collections.sort(names, new FolderComparators.MyDataFolderTreeNameComparator());
            ret = names.indexOf(folderName);
        }
        return ret;
    }

    public List<String> getChildNames() {
        List<String> ret = new ArrayList<String>();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            FolderMutableTreeNode childNode = getChildAt(i);
            ret.add(childNode.getUserObject().getName());
        }
        return ret;
    }

    public FolderMutableTreeNode findNode(String folderPath) {
        Folder _userObject = getUserObject();
        if (_userObject.getAbsolutePath().equals(folderPath)) {
            return this;
        } else {
            int count = getChildCount();
            if (count > 0) {
                FolderMutableTreeNode ret = null;
                for (int i = 0; i < count; i++) {
                    FolderMutableTreeNode child = getChildAt(i);
                    ret = child.findNode(folderPath);
                    if (ret != null) {
                        break;
                    }
                }
                return ret;
            } else {
                return null;
            }
        }
    }

    @Override
    public FolderMutableTreeNode getChildAt(int i) {
        return (FolderMutableTreeNode) super.getChildAt(i);
    }

    private void createChildren() {
        if (userObject != null && !(userObject instanceof Folder)) {
            return;
        }
        Folder folder = (Folder) userObject;

        Collection<Folder> folderSet = folder.getReadOnlyChildren();
        List<Folder> folderList = new ArrayList<Folder>(folderSet);
        Collections.sort(folderList, new FolderComparators.MyDataFolderTreeComparator());
        Iterator<Folder> itr = folderList.iterator();
        while (itr.hasNext()) {
            Folder child = itr.next();
            FolderMutableTreeNode folderNode = new FolderMutableTreeNode(child, true);
            add(folderNode);
        }
    }

    @Override
    public Folder getUserObject() {
        return (Folder) super.getUserObject();
    }
}
