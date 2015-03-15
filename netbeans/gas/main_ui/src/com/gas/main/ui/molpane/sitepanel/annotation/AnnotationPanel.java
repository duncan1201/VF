/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Feture;
import com.gas.main.ui.molpane.sitepanel.SidePanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author dq
 */
public class AnnotationPanel extends JPanel {

    protected Map<String, List<Feture>> fetureMap;
        
    private WeakReference<JTree> treeRef;
    
    public static boolean updatingUI = false;
    
    public AnnotationPanel() {
        super(new BorderLayout());
        setToolTipText("Annotations");
        TitledPanel titledPanel = new TitledPanel("Annotations");      
        titledPanel.getContentPane().setLayout(new BorderLayout());
                        
        JTree tree = new JTree();
        treeRef = new WeakReference<JTree>(tree);
        tree.setEditable(true);
        tree.setRootVisible(false);
        tree.setCellRenderer(new TreeRenderer());
        tree.setCellEditor(new TreeEditor(tree));
        JScrollPane scrollPane = new JScrollPane(tree);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(Color.WHITE);
        
        titledPanel.getContentPane().add(scrollPane, BorderLayout.CENTER);
        add(titledPanel, BorderLayout.CENTER);
        hookupListeners();
    }
    
    private void hookupListeners(){
        getTree().addMouseMotionListener(new AnnotationPanelListeners.TreeMouseAdapter());
        addPropertyChangeListener(new AnnotationPanelListeners.PtyChangeListener());
        TreeSelectionModel selModel = getTree().getSelectionModel(); 
        selModel.addTreeSelectionListener(new AnnotationPanelListeners.TreeSelListener(treeRef));
    }
    
    public void refresh(){
        SidePanel sidePanel = UIUtil.getParent(this, SidePanel.class);
        AnnotatedSeq as = sidePanel.getAs();
        Map<String, List<Feture>> fetureMap = AsHelper.getSortedFetureMap(as);
        setFetureMap(fetureMap);
    }
    
    public void setFetureMap(Map<String, List<Feture>> fetureMap){
        Map<String, List<Feture>> old = this.fetureMap;
        this.fetureMap = fetureMap;
        firePropertyChange("fetureMap", old, this.fetureMap);
    }

    public Map<String, List<Feture>> getFetureMap() {
        return fetureMap;
    }
    
    public void setSelected(Feture feture){
        updatingUI = true;
        int rowCount = treeRef.get().getRowCount();
        for(int i = 0; i < rowCount; i++){
            TreePath path = treeRef.get().getPathForRow(i);
            FeatureTreeNode treeNode = (FeatureTreeNode)path.getLastPathComponent();
            Object userObject = treeNode.getUserObject();
            if(userObject == feture){
                Feture f = (Feture)userObject;
                treeRef.get().setSelectionPath(path);
                break;
            }
        }
        updatingUI = false;
    }
    
    public String getChildName() {
        return "Annotation";
    }
    
    public ImageIcon getImageIcon() {
        return ImageHelper.createImageIcon(ImageNames.ARROW_RIGHT_GREEN);
    }  
    
    public JTree getTree(){
        return treeRef.get();
    }
}
