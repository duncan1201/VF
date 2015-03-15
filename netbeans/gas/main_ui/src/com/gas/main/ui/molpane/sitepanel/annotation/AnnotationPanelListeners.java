/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.api.IExternalURIService;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Qualifier;
import com.gas.domain.ui.editor.as.IASEditor;
import com.gas.domain.ui.util.api.IDownloadService;
import com.gas.main.ui.molpane.MolPane;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.net.URI;
import javax.swing.JTree;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class AnnotationPanelListeners {

    private static IExternalURIService service = Lookup.getDefault().lookup(IExternalURIService.class);

    static class TreeSelListener implements TreeSelectionListener {

        private WeakReference<JTree> ref;
        
        TreeSelListener(WeakReference<JTree> ref) {
            this.ref = ref;
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            if(AnnotationPanel.updatingUI){
                return;
            }
            TreeSelectionModel model = (TreeSelectionModel) e.getSource();
            model.getSelectionPath();
            final TreePath treePath = e.getPath();
            FeatureTreeNode node = (FeatureTreeNode) treePath.getLastPathComponent();
            Object userObject = node.getUserObject();
            if (userObject instanceof Qualifier) {
                Qualifier q = (Qualifier) userObject;
                final String value = q.getValue();
                final int index = value.indexOf(':');
                String db = null;
                String id = null;
                if (index > -1) {
                    db = value.substring(0, index);
                    id = value.substring(index + 1);
                }
                if (q.isDbXref()) {
                    if (db != null && id != null) {
                        URI uri = service.getURI(db, id);
                        if (uri != null) {
                            CommonUtil.browse(uri);
                        }
                    }
                } else if (q.isProteinId()) {
                    IDownloadService dService = Lookup.getDefault().lookup(IDownloadService.class);
                    dService.downloadAndOpen("protein", value);
                }
            }else if(userObject instanceof Feture){
                final Feture feture = (Feture)userObject;
                Loc loc = feture.getLucation().toLoc();
                MolPane molPane = UIUtil.getParent(ref.get(), MolPane.class);
                molPane.center(loc);
                molPane.setSelectedFeture(feture);
            }            
        }
    }

    static class TreeMouseAdapter extends MouseInputAdapter {

        TreeMouseAdapter() {
        }

        @Override
        public void mouseMoved(MouseEvent e) {
            JTree tree = (JTree) e.getSource();
            int x = (int) e.getPoint().getX();
            int y = (int) e.getPoint().getY();
            TreePath path = tree.getPathForLocation(x, y);
            if (path == null) {
                tree.setCursor(Cursor.getDefaultCursor());
            } else {
                FeatureTreeNode node = (FeatureTreeNode) path.getLastPathComponent();
                Object userObject = node.getUserObject();
                if (userObject instanceof Qualifier) {
                    Qualifier qualifer = (Qualifier) userObject;
                    if (qualifer.isDbXref()) {
                        final String value = qualifer.getValue();
                        final int index = value.indexOf(':');
                        final String db = value.substring(0, index);
                        boolean supported = service.isBrowsingSupported(db);
                        if (supported) {
                            tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                        } else {
                            tree.setCursor(Cursor.getDefaultCursor());
                        }
                    } else if (qualifer.isProteinId()) {
                        tree.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    } else {
                        tree.setCursor(Cursor.getDefaultCursor());
                    }
                } else {
                    tree.setCursor(Cursor.getDefaultCursor());
                }

            }
        }
    }

    static class PtyChangeListener implements PropertyChangeListener {

        PtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AnnotationPanel panel = (AnnotationPanel) evt.getSource();
            final String pName = evt.getPropertyName();
            if (pName.equals("fetureMap")) {
                panel.getTree().clearSelection();/*clearing selection is necessary here to prevent exception*/
                DefaultTreeModel treeModel = (DefaultTreeModel) panel.getTree().getModel();
                FeatureTreeNode node = new FeatureTreeNode(panel.getFetureMap());                
                treeModel.setRoot(node);
            }
        }
    }
}
