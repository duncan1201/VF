/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.entrez.core.ui;

import com.gas.common.ui.misc.CNST;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.filesystem.Folder;
import com.gas.domain.ui.explorer.ExplorerTC;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import javax.swing.JComponent;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

/**
 *
 * @author dq
 */
class SearchCtrlPanelListeners {

    static class TextFieldListener implements ActionListener {

        SearchCtrlPanel panel;

        TextFieldListener(SearchCtrlPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            panel.searchButton.doClick();
        }      

    }

    static class SearchActionListener implements ActionListener {

        SearchCtrlPanel panel;

        SearchActionListener(SearchCtrlPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final String term = panel.getTerm();
            if (term.isEmpty()) {
                String msg = String.format(CNST.MSG_FORMAT, "Search parameters are empty", "Please enter search parameters");
                DialogDescriptor.Message me = new DialogDescriptor.Message(msg);
                me.setTitle("Cannot perform search");
                DialogDisplayer.getDefault().notify(me);
                return;
            }            
            panel.totalCount = 0;
            panel.downloadedCount = 0;
            panel.cancelled = false;
            Folder folder = ExplorerTC.getInstance().getSelectedFolder();
            if (folder != null) {
                folder.clearContents();
                ExplorerTC.getInstance().updateFolder(folder);
            }

            //DialogDescriptor.Message me = new DialogDescriptor.Message(term);
            //DialogDisplayer.getDefault().notify(me);
            panel.clearTable();
            panel.doSearch(term);
        }
    }

    static class CancelActionListener implements ActionListener {

        WeakReference<SearchCtrlPanel> ref;
        WeakReference<SearchPanel> searchPanelRef;

        CancelActionListener(SearchCtrlPanel ref) {
            this.ref = new WeakReference<SearchCtrlPanel>(ref);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (searchPanelRef == null) {
                SearchPanel searchPanel = UIUtil.getParent(ref.get(), SearchPanel.class);
                searchPanelRef = new WeakReference<SearchPanel>(searchPanel);
            }

            ref.get().cancelled = true;
            ref.get().updateUICancelled(ref.get().totalCount, ref.get().downloadedCount);
        }
    }
}
