/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.explorer;

import com.gas.common.ui.border.EtchedBorder2;
import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.core.filesystem.FolderHelper;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.openide.awt.ActionID;

/**
 * Top component which displays something.
 */
@TopComponent.Description(preferredID = "ExplorerTopComponent",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "slimExplorer", openAtStartup = true)
@ActionID(category = "Window", id = "com.gas.main.ui.explorer.ExplorerTopComponent")
public final class ExplorerTC extends TopComponent {

    private static ExplorerTC self;
    public static final String ID = "ExplorerTopComponent";
    private NaviTreePanel naviTreePanel;
    private Header header;

    public ExplorerTC() {
        initComponents();

        setBorder(new EtchedBorder2());
        setIcon(ImageHelper.createImage(ImageNames.DB_SEARCH_16));
        setName("Explorer Window");
        setToolTipText(NbBundle.getMessage(ExplorerTC.class, "HINT_ExplorerTopComponent"));
        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_SLIDING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);

    }

    public static ExplorerTC getInstance() {
        if (self == null) {
            self = UIUtil.findTopComponent(ID, ExplorerTC.class);
        }
        return self;
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        header = new Header();
        add(header, BorderLayout.NORTH);
        naviTreePanel = new NaviTreePanel();
        add(naviTreePanel, BorderLayout.CENTER);
    }

    public Folder getRootFolder() {
        return naviTreePanel.getRootFolder();
    }

    public void updateFolder(Folder folder) {
        naviTreePanel.updateFolder(folder);
    }
    
    public void addNewFolder(Folder folder){
        naviTreePanel.addNewFolder(folder);
    }

    public Folder getSelectedFolder() {
        return naviTreePanel.getSelectedFolder();
    }

    public Folder getFolder(String path) {
        return naviTreePanel.getTreeNode(path).getUserObject();
    }

    public FolderMutableTreeNode getSelectedTreeNode() {
        return naviTreePanel.getSelectedNode();
    }
    
    public void setSelectedFolder(Integer hId){
        if(hId == null){
            return;
        }
        Folder root = getRootFolder();
        Folder descendant = FolderHelper.getDescendantByHibernateId(root, hId);
        if(descendant != null){
            setSelectedFolder(descendant);
        }
    }

    public void setSelectedFolder(Folder f) {
        naviTreePanel.selectFolder(f);
    }

    public void deleteFolder(Folder f) {
        naviTreePanel.removeNode(f.getAbsolutePath());
    }

    @Override
    public void setName(String name) {
        header.setText(name);
    }

    @Override
    public void setIcon(Image image) {
        ImageIcon icon = ImageHelper.createImageIcon(image);
        header.setIcon(icon);
    }

    @Override
    public void componentOpened() {
        naviTreePanel.selectLastOpenFolder();
    }

    static class Header extends JPanel {

        JLabel label;

        Header() {
            setOpaque(true);
            setBackground(new Color(223, 234, 246));
            GridBagLayout layout = new GridBagLayout();
            setLayout(layout);
            GridBagConstraints c;

            c = new GridBagConstraints();
            label = new JLabel();
            add(label, c);

            c = new GridBagConstraints();
            c.gridy = 0;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            Component comp = Box.createRigidArea(new Dimension(1, 1));
            add(comp, c);
        }

        public void setRightDecorator(Component comp) {
            GridBagConstraints c = new GridBagConstraints();
            c.gridy = 0;
            add(comp, c);
        }

        public void setIcon(Icon icon) {
            label.setIcon(icon);
        }

        public void setText(String text) {
            label.setText(text);
        }
    }
}
