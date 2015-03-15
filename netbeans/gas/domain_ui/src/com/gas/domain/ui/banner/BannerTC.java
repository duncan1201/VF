/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.domain.ui.banner;

import com.gas.common.ui.border.EtchedBorder2;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.IFolderElement;
import com.gas.domain.core.IFolderElementList;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AnnotatedSeqList;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.IFolderPanel;
import com.gas.domain.ui.TCHeader;
import com.gas.domain.ui.dynamicTable.DynamicTable;
import com.gas.domain.ui.dynamicTable.DynamicTableModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import org.openide.windows.TopComponent;
import org.openide.awt.ActionID;

/**
 * Top component which displays something.
 */
@TopComponent.Description(preferredID = "BannerTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "banner", openAtStartup = true)
@ActionID(category = "Window", id = "com.gas.main.ui.banner.BannerTopComponent")
public final class BannerTC extends TopComponent {

    private static BannerTC self;
    public static final String ID = "BannerTopComponent";
    protected CardHolderPanel cardHolder;
    private TCHeader header;
    private ReentrantLock lockUpdateFolder = new ReentrantLock(true);

    public BannerTC() {
        initComponents();

        putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_SLIDING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_DRAGGING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);
    }

    public static BannerTC getInstance() {
        if (self == null) {
            self = UIUtil.findTopComponent(ID, BannerTC.class);
        }
        return self;
    }
    
    public List getSelectdObjects(){
        IFolderPanel panel = getFolderPanel();
        List ret = panel.getTable().getSelectedObjects();
        return ret;
    }
    
    public <T> List<T> getSelectedObjects(Class<T> retType){
        IFolderPanel panel = getFolderPanel();
        List ret = panel.getTable().getSelectedObjects(retType);
        return ret;
    }

    public <T> List<T> getCheckedObjects(Class<T> clazz) {
        IFolderPanel panel = getFolderPanel();
        List<T> ret = panel.getTable().getCheckedObjects(clazz);
        return ret;
    }

    public IFolderElementList getCheckedObjects() {
        IFolderPanel panel = getFolderPanel();
        IFolderElementList ret = panel.getTable().getCheckedObjects();
        return ret;
    }
    
    public List<AnnotatedSeq> getSelectedNucleotides(){
        List<AnnotatedSeq> ret = new ArrayList<AnnotatedSeq>();
        List<AnnotatedSeq> aaa = getSelectedObjects(AnnotatedSeq.class);
        for(AnnotatedSeq as: aaa){
            if(as.isNucleotide()){
                ret.add(as);
            }
        }
        return ret;
    }

    public AnnotatedSeqList getCheckedNucleotides() {
        IFolderPanel panel = getFolderPanel();
        List<AnnotatedSeq> asList = panel.getTable().getCheckedObjects(AnnotatedSeq.class);
        AnnotatedSeqList ret = new AnnotatedSeqList();
        for (AnnotatedSeq as : asList) {
            if (as.isNucleotide()) {
                ret.add(as);
            }
        }
        return ret;
    }

    public List<AnnotatedSeq> getCheckedProteins() {
        IFolderPanel panel = getFolderPanel();
        List<AnnotatedSeq> ret = panel.getTable().getCheckedProteins();
        return ret;
    }

    public void unselect(IFolderElement obj) {
        cardHolder.unselectRow(obj);
    }

    public void setCheckRowByHibernateId(String hId, boolean checked) {
        cardHolder.setCheckRow(hId, checked);
    }

    public void setCheckRow(IFolderElement obj, boolean checked) {
        cardHolder.setCheckRow(obj, checked);
    }
    
    public void setSelected(IFolderElement fe){
        cardHolder.setSelected(fe);
    }

    public void unselectByHibernateId(String hid) {
        cardHolder.unselectRow(hid);
    }

    public void setSelectedFolder(Folder folder) {
        this.cardHolder.showCard(folder);
        this.refreshHeader();
    }

    /**
     * Updates the UI based on the contents of the folder
     *
     * @param folder: the folder
     */
    public void updateFolder(Folder folder) {
        updateFolder(folder, false);
    }

    public void updataRowByHibernateId(IFolderElement obj) {
        this.cardHolder.updateRowByHibernateId(obj);
    }

    public void fireTableDataChanged(Folder folder) {
        DynamicTableModel tableModel = (DynamicTableModel) this.cardHolder.getFolderPanel(folder).getTable().getModel();
        tableModel.fireTableDataChanged();
    }

    public void updateFolder(Folder folder, boolean show) {
        lockUpdateFolder.lock();
        try {
            boolean contains = cardHolder.contains(folder.getAbsolutePath());
            if (!contains) {
                cardHolder.createFolderPanel(folder);
            } else {
                cardHolder.updateFolder(folder);
            }
            if (show) {
                setSelectedFolder(folder);
            }
        } finally {
            lockUpdateFolder.unlock();
        }
    }

    public boolean containsFolder(Folder folder) {
        return cardHolder.contains(folder.getAbsolutePath());
    }

    private void initComponents() {
        setName(ID);
        setBorder(new EtchedBorder2());
        setLayout(new BorderLayout());
        header = new TCHeader();
        add(header, BorderLayout.NORTH);
        cardHolder = new CardHolderPanel();
        add(cardHolder, BorderLayout.CENTER);
    }

    public void setIcon(ImageIcon icon) {
        header.setIcon(icon);
    }

    public void setHeaderText(String txt) {
        setHeaderText(txt, SwingConstants.LEFT);
    }
    
    public void setHeaderText(String txt, int alignment){
        header.setText(txt, alignment);
    }

    public void refreshHeader() {
        IFolderPanel folderPanel = getFolderPanel();
        Boolean busy = folderPanel.getBusy();
        if (Boolean.TRUE.equals(busy)) {
            setIcon(ImageHelper.createImageIcon(ImageNames.CIRCLE_BALL_16));
        } else {
            setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
        }
        setHeaderText(folderPanel.getStatusLine());
        
        DynamicTable table = folderPanel.getTable();
        int rowCount = table.getModel().getRowCount();
        int check = table.getCheckedObjects().size();
        if(check > 0){
            setHeaderText(String.format("%d of %d checked", check, rowCount), SwingConstants.RIGHT);
        }else{
            setHeaderText("", SwingConstants.RIGHT);
        }
    }

    public IFolderPanel getFolderPanel(Folder folder) {
        return cardHolder.getFolderPanel(folder);
    }

    public void deleteFolder(Folder folder) {
        cardHolder.removePanel(folder);
    }

    public void showInfoPanel() {
        cardHolder.showInfoPanel();
    }

    public IFolderPanel getFolderPanel() {
        IFolderPanel ret = null;
        Component comp = cardHolder.getVisibleComponent();
        if (comp instanceof IFolderPanel) {
            ret = (IFolderPanel) comp;
        }
        return ret;
    }
}