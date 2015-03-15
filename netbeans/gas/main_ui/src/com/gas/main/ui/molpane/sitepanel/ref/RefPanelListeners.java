/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.ref;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.FileHelper;
import com.gas.database.core.pubmed.api.IPubmedDBService;
import com.gas.domain.core.pubmed.PubmedArticle;
import com.gas.domain.core.pubmed.util.INCBIPubmedArticleSetParser;
import com.gas.domain.ui.util.api.IEFetchService;
import com.gas.main.ui.editor.pubmed.PubmedEditor;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URI;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;
import org.openide.util.Lookup;
import org.openide.windows.Mode;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
class RefPanelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String pName = evt.getPropertyName();
            Object v = evt.getNewValue();
            RefPanel src = (RefPanel) evt.getSource();
            if (pName.equals("pmid")) {
                String pmid = (String) v;
                if (pmid != null && !pmid.isEmpty()) {
                    src.getPmidPane().setVisible(true);
                    src.getPmidPane().setText("PMID: " + pmid);
                } else {
                    src.getPmidPane().setVisible(false);
                }
                src.getDownloadBtn().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                src.getDownloadBtn().addActionListener(new RefPanelListeners.DownloadBtnListener(new WeakReference<RefPanel>(src)));
            } else if (pName.equals("publisher")) {
                String publisher = (String) v;
                src.getLocationPane().setText(publisher);
                src.getLocationPane().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }
        }
    }

    static class LocationMouseListener extends MouseInputAdapter {

        private WeakReference<RefPanel> ref;

        LocationMouseListener(WeakReference<RefPanel> ref) {
            this.ref = ref;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            final String pmid = ref.get().getPmid();
            if (pmid == null || pmid.isEmpty()) {
                return;
            }
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(new Runnable() {
                @Override
                public void run() {
                    IEFetchService service = Lookup.getDefault().lookup(IEFetchService.class);
                    service.setDb("pubmed");
                    service.setIds(pmid);

                    List<String> rets = service.sendRequest(String.class);
                    String ret = null;
                    if (!rets.isEmpty()) {
                        ret = rets.get(0);
                    }
                    if (ret != null) {
                        INCBIPubmedArticleSetParser parser = Lookup.getDefault().lookup(INCBIPubmedArticleSetParser.class);
                        PubmedArticle article = parser.singleParse(ret);
                        if (article.getDoi() != null) {
                            URI uri = URI.create("http://dx.doi.org/" + article.getDoi());
                            CommonUtil.browse(uri);
                        }
                    }
                }
            });
        }
    }

    static class DownloadBtnListener implements ActionListener {

        private WeakReference<RefPanel> ref;

        DownloadBtnListener(WeakReference<RefPanel> ref) {
            this.ref = ref;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final String pmid = ref.get().getPmid();
            if (pmid == null || pmid.isEmpty()) {
                return;
            }
            boolean editorPresent = false;
            boolean dbPresent = false;
            Mode editorMode = WindowManager.getDefault().findMode("editor");
            TopComponent[] tcs = WindowManager.getDefault().getOpenedTopComponents(editorMode);
            for (int i = 0; i < tcs.length; i++) {
                String name = tcs[i].getName();
                if (tcs[i] instanceof PubmedEditor) {
                    if (name.equals(pmid)) {
                        editorPresent = true;
                        tcs[i].requestActive();
                        break;
                    }
                }
            }

            if (editorPresent) {
                return;
            }

            final PubmedEditor editor = new PubmedEditor();
            editor.setName(pmid);
            editor.open();
            editor.requestActive();

            IPubmedDBService pS = Lookup.getDefault().lookup(IPubmedDBService.class);
            dbPresent = pS.isPresent(pmid);

            if (dbPresent) {
                PubmedArticle article = pS.getFullByPmid(pmid);
                editor.setPubmedArticle(article);
            } else {
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        IEFetchService service = Lookup.getDefault().lookup(IEFetchService.class);
                        service.setDb("pubmed");
                        service.setIds(pmid);

                        List<String> articleStrs = service.sendRequest(String.class);
                        if (!articleStrs.isEmpty()) {
                            String ret = articleStrs.get(0);
                            INCBIPubmedArticleSetParser parser = Lookup.getDefault().lookup(INCBIPubmedArticleSetParser.class);
                            PubmedArticle article = parser.singleParse(ret);
                            editor.setPubmedArticle(article);
                            editor.setCanSave();
                        }else{
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    editor.close();
                                }
                            });
                            
                        }
                    }
                });
            }
        }
    }
}
