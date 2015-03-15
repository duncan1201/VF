/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.layout.CardLayout2;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.ui.IFolderPanel;
import com.gas.domain.core.filesystem.Folder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author dunqiang
 */
public class CardHolderPanel extends JPanel {

    public CardHolderPanel() {
        super();
        setLayout(new CardLayout2());
        add(new InfoPanel(), InfoPanel.class.toString());
    }

    public void showInfoPanel() {
        CardLayout2 layout = (CardLayout2) getLayout();
        layout.show(this, InfoPanel.class.toString());
    }

    /**
     *
     */
    IFolderPanel createFolderPanel(Folder folder) {
        boolean contains = contains(folder.getAbsolutePath());
        if (contains) {
            throw new IllegalArgumentException(String.format("%s exists already", folder.getAbsolutePath()));
        }
        IFolderPanel folderPanel = FolderPanelFactory.createFolderPanel(folder);
        add((Component) folderPanel, folderPanel.getAbsolutePath());
        return folderPanel;
    }

    public void unselectRow(IFolderElement obj) {
        int size = getComponentCount();
        for (int i = 0; i < size; i++) {
            Component comp = getComponent(i);
            if (IFolderPanel.class.isAssignableFrom(comp.getClass())) {
                IFolderPanel folderPanel = (IFolderPanel) comp;
                boolean success = folderPanel.unselectRow(obj);
                if (success) {
                    break;
                }
            }
        }
    }
    
    public void setSelected(IFolderElement fe){
        int size = getComponentCount();
        for (int i = 0; i < size; i++) {
            Component comp = getComponent(i);
            if (IFolderPanel.class.isAssignableFrom(comp.getClass())) {
                IFolderPanel folderPanel = (IFolderPanel) comp;
                boolean success = folderPanel.setSelected(fe);
                if (success) {
                    break;
                }
            }
        }    
    }

    public void setCheckRow(IFolderElement obj, boolean checked) {
        int size = getComponentCount();
        for (int i = 0; i < size; i++) {
            Component comp = getComponent(i);
            if (IFolderPanel.class.isAssignableFrom(comp.getClass())) {
                IFolderPanel folderPanel = (IFolderPanel) comp;
                boolean success = folderPanel.checkRow(obj, checked);
                if (success) {
                    break;
                }
            }
        }
    }

    public void setCheckRow(String hId, boolean checked) {
        int size = getComponentCount();
        for (int i = 0; i < size; i++) {
            Component comp = getComponent(i);
            if (comp instanceof MyDataFolderPanel) {
                MyDataFolderPanel p = (MyDataFolderPanel) comp;
                boolean success = p.checkRow(hId, checked);
                if (success) {
                    break;
                }
            }
        }
    }

    public void unselectRow(String hId) {
        int size = getComponentCount();
        for (int i = 0; i < size; i++) {
            Component comp = getComponent(i);
            if (comp instanceof MyDataFolderPanel) {
                MyDataFolderPanel p = (MyDataFolderPanel) comp;
                boolean success = p.unselectRow(hId);
                if (success) {
                    break;
                }
            }
        }
    }

    public void updateRowByHibernateId(IFolderElement obj) {
        Component comp = getVisibleComponent();
        if (comp instanceof MyDataFolderPanel) {
            MyDataFolderPanel d = (MyDataFolderPanel) comp;
            d.getTable().replaceDatumByHibernateId(obj);
        }
    }

    protected IFolderPanel getFolderPanel(Folder folder) {
        IFolderPanel ret = null;
        int size = getComponentCount();
        for (int i = 0; i < size; i++) {
            Component comp = getComponent(i);
            if (comp instanceof IFolderPanel) {
                IFolderPanel panel = (IFolderPanel) comp;
                if (folder.getAbsolutePath().equals(panel.getAbsolutePath())) {
                    ret = panel;
                }
            }
        }
        return ret;
    }

    protected Component getVisibleComponent() {
        Component ret = null;
        int count = getComponentCount();
        for (int i = 0; i < count; i++) {
            Component comp = getComponent(i);
            if (comp.isVisible()) {
                ret = comp;
                break;
            }
        }
        return ret;
    }

    public boolean contains(String folderPath) {
        boolean ret = false;
        int count = getComponentCount();
        for (int i = 0; i < count; i++) {
            Component comp = getComponent(i);
            if (comp instanceof IFolderPanel) {
                IFolderPanel cardPanel = (IFolderPanel) comp;
                ret = cardPanel.getAbsolutePath().equals(folderPath);
                if (ret) {
                    break;
                }
            }
        }
        return ret;
    }

    public void removePanel(Folder folder) {
        String folderPath = folder.getAbsolutePath();
        int count = getComponentCount();
        for (int i = 0; i < count; i++) {
            Component comp = getComponent(i);
            if (comp instanceof IFolderPanel) {
                IFolderPanel cardPanel = (IFolderPanel) comp;
                if (cardPanel.getAbsolutePath().equalsIgnoreCase(folderPath)) {
                    remove(comp);
                    break;
                }
            }
        }
    }

    public void updateFolder(Folder folder) {
        IFolderPanel panel = getFolderPanel(folder);
        if(panel != null){
            panel.updateFolder(folder);
        }
    }

    public void showCard(Folder folder) {
        String folderPath = folder.getAbsolutePath();
        CardLayout2 layout = (CardLayout2) getLayout();

        boolean found = false;
        int count = getComponentCount();
        for (int i = 0; i < count; i++) {
            Component comp = getComponent(i);
            if (comp instanceof IFolderPanel) {
                IFolderPanel cardPanel = (IFolderPanel) comp;
                if (cardPanel.getAbsolutePath().equalsIgnoreCase(folderPath)) {
                    found = true;
                    boolean contains = layout.contains(this, folderPath);
                    if (!contains) {
                        this.remove((JComponent) cardPanel);
                        this.add((JComponent) cardPanel, folderPath);
                    }
                    layout.show(this, folderPath);
                    revalidate();
                    break;
                }
            }
        }

        if (!found) {
            IFolderPanel folderPanel = FolderPanelFactory.createFolderPanel(folder);
            add((Component) folderPanel, folderPanel.getAbsolutePath());
            showCard(folder);
        }
    }

    static class InfoPanel extends JPanel {

        InfoPanel() {
            setOpaque(true);
            setBackground(Color.WHITE);
            setLayout(new GridBagLayout());
            GridBagConstraints c;
            
            int gridy = 0;

            c = new GridBagConstraints();
            c.gridy  = gridy++;
            c.gridwidth = 2;
            JLabel label = createLabel();
            label.setText("Oops! No folder selected");            
            add(label, c);
            
            c = new GridBagConstraints();            
            c.gridy = gridy;
            label = createLabel();
            label.setText("Please select a folder from the ");            
            add(label, c);            
            
            c = new GridBagConstraints();
            c.gridy = gridy;
            label = createLabel();
            label.setText("Explorer Window");
            label.setIcon(ImageHelper.createImageIcon(ImageNames.DB_16));
            add(label, c);
        }
        
        private JLabel createLabel(){
            JLabel label = new JLabel();
            Font font = label.getFont();            
            label.setFont(font.deriveFont(font.getSize2D() * 1.3f));
            return label;
        }
    }
}