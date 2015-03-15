/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.annotation;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.dialog.DialogUtil;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.FetureKey;
import com.gas.domain.core.as.Pozition;
import com.gas.domain.core.as.api.IFetureKeyService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

/**

 @author dq
 */
class AnnPanelListeners {

    static class StrandBtnListener implements ActionListener {

        private AnnPanel panel;

        StrandBtnListener(AnnPanel panel) {
            this.panel = panel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final JRadioButton btn = (JRadioButton) e.getSource();
            final String cmd = btn.getActionCommand();
            final JList locList = panel.getLocList();
            boolean strand = true;
            if (cmd.equals("forward")) {
                strand = true;
            } else if (cmd.equals("reverse")) {
                strand = false;
            } else if (cmd.equals("undirected")) {
                strand = true;
            }

            int size = locList.getModel().getSize();
            DefaultListModel listModel = (DefaultListModel)locList.getModel();
            for (int i = 0; i < size; i++) {
                Pozition poz = (Pozition) listModel.getElementAt(i);
                poz.setStrand(strand);
                listModel.setElementAt(poz, i);
            }            
        }
    }

    static class DownBtnListener implements ActionListener {

        private WeakReference<AnnPanel> panelRef;

        DownBtnListener(WeakReference<AnnPanel> panelRef) {
            this.panelRef = panelRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final AnnPanel panel = this.panelRef.get();
            final JList locList = panel.getLocList();
            final DefaultListModel listModel = panel.getLocListModel();
            final int size = listModel.getSize();
            final int selectedIndex = locList.getSelectedIndex();
            if (selectedIndex == size - 1) {
                return;
            }
            Pozition cur = (Pozition) listModel.get(selectedIndex);
            Pozition next = (Pozition) listModel.get(selectedIndex + 1);
            listModel.setElementAt(cur, selectedIndex + 1);
            listModel.setElementAt(next, selectedIndex);
            locList.setSelectedIndex(selectedIndex + 1);
        }
    }

    static class UpBtnListener implements ActionListener {

        private WeakReference<AnnPanel> panelRef;

        UpBtnListener(WeakReference<AnnPanel> panelRef) {
            this.panelRef = panelRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final AnnPanel panel = this.panelRef.get();
            final JList locList = panel.getLocList();
            final DefaultListModel listModel = panel.getLocListModel();
            int selectedIndex = locList.getSelectedIndex();
            if (selectedIndex == 0) {
                return;
            }
            Pozition cur = (Pozition) listModel.get(selectedIndex);
            Pozition prev = (Pozition) listModel.get(selectedIndex - 1);
            listModel.setElementAt(cur, selectedIndex - 1);
            listModel.setElementAt(prev, selectedIndex);

            locList.setSelectedIndex(selectedIndex - 1);
        }
    }

    static class QualifierListListener implements ListSelectionListener {

        private WeakReference<AnnPanel> panelRef;

        QualifierListListener(WeakReference<AnnPanel> panelRef) {
            this.panelRef = panelRef;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            AnnPanel panel = panelRef.get();
            int selected = panel.getQualifierList().getSelectedIndex();
            panel.getDelQualifierBtn().setEnabled(selected > -1);
            panel.getEditQualifierBtn().setEnabled(selected > -1);
        }
    }

    static class LocListListener implements ListSelectionListener {

        private WeakReference<AnnPanel> panelRef;

        LocListListener(WeakReference<AnnPanel> panelRef) {
            this.panelRef = panelRef;
        }

        @Override
        public void valueChanged(ListSelectionEvent e) {
            AnnPanel panel = panelRef.get();
            int size = panel.getLocListModel().getSize();
            final int selection = panel.getLocList().getSelectedIndex();
            panel.getDelLocBtn().setEnabled(selection > -1);
            panel.getEditLocBtn().setEnabled(selection > -1);

            panel.getJoinBtn().setEnabled(size > 1);
            panel.getOrderBtn().setEnabled(size > 1);

            panel.getLocUpBtn().setEnabled(selection > 0 && panel.isCircular());
            panel.getLocDownBtn().setEnabled(selection < size - 1 && panel.isCircular());
        }
    }

    static class EditLocBtnListener implements ActionListener {

        private WeakReference<AnnPanel> panelRef;

        EditLocBtnListener(WeakReference<AnnPanel> panelRef) {
            this.panelRef = panelRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = panelRef.get().getLocList().getSelectedIndex();
            Pozition poz = (Pozition) panelRef.get().getLocListModel().get(index);
            int length = panelRef.get().getMolPane().getAs().getLength();
            LocList curList = getLocList(panelRef.get().getLocListModel(), panelRef.get().getLocList(), false);
            NewLocPanel newLocPanel = new NewLocPanel(poz, length, panelRef.get().isCircular(), curList);
            final DialogDescriptor dialogDescriptor = new DialogDescriptor(newLocPanel, "Edit Location");
            newLocPanel.setDialogDescriptor(dialogDescriptor);

            newLocPanel.validateInput();

            Integer answer = (Integer) DialogDisplayer.getDefault().notify(dialogDescriptor);
            if (answer == JOptionPane.OK_OPTION) {
                poz = newLocPanel.getPozition();
                panelRef.get().getLocListModel().insertElementAt(poz, index);
                panelRef.get().getLocListModel().removeElementAt(index + 1);
                panelRef.get().getLocList().setSelectedIndex(index);
            }
        }
    }

    private static LocList getLocList(DefaultListModel listModel, JList locList, boolean includeSelected) {
        LocList ret = new LocList();
        final int size = listModel.size();

        int selectedIndex = locList.getSelectedIndex();
        for (int i = 0; i < size; i++) {
            if (!includeSelected && selectedIndex == i) {
                continue;
            }
            Pozition poz = (Pozition) listModel.get(i);
            Loc loc = new Loc(poz.getStart(), poz.getEnd());
            ret.add(loc);
        }
        return ret;
    }

    static class NewLocBtnListener implements ActionListener {

        private WeakReference<AnnPanel> panelRef;

        NewLocBtnListener(WeakReference<AnnPanel> panelRef) {
            this.panelRef = panelRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int totalPos = panelRef.get().getMolPane().getAs().getLength();
            final int length = panelRef.get().getMolPane().getAs().getLength();
            final JList locList = panelRef.get().getLocList();
            final DefaultListModel listModel = panelRef.get().getLocListModel();
            Pozition pozTmp = new Pozition();
            pozTmp.setStart(1);
            pozTmp.setEnd(totalPos);
            final LocList curLoc = getLocList(panelRef.get().getLocListModel(), panelRef.get().getLocList(), true);
            final NewLocPanel newLocPanel = new NewLocPanel(pozTmp, length, panelRef.get().isCircular(), curLoc);
            DialogDescriptor dd = new DialogDescriptor(newLocPanel, "New Location");
            newLocPanel.setDialogDescriptor(dd);

            newLocPanel.validateInput();

            Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);
            if (answer.intValue() == JOptionPane.OK_OPTION) {
                Pozition poz = newLocPanel.getPozition();
                if (panelRef.get().isCircular()) {
                    listModel.add(0, poz);
                    locList.setSelectedIndex(0);
                } else {
                    addLinearPozition(poz, listModel, locList);
                }
            }
            panelRef.get().validateInput();
        }

        private void addLinearPozition(Pozition poz, DefaultListModel listModel, JList locList) {
            final int size = listModel.getSize();
            boolean success = false;
            for (int i = 0; i < size; i++) {
                Pozition p = (Pozition) listModel.get(i);
                if (poz.getStart() < p.getStart()) {
                    listModel.add(i, poz);
                    locList.setSelectedIndex(i);
                    success = true;
                    break;
                }
            }
            if (!success) {
                listModel.add(size, poz);
                locList.setSelectedIndex(size);
            }
        }
    }

    static class FetureNameListener implements DocumentListener {

        private WeakReference<AnnPanel> newAnnPanelRef;

        FetureNameListener(WeakReference<AnnPanel> newAnnPanelRef) {
            this.newAnnPanelRef = newAnnPanelRef;
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            updateFetureName(e);
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            updateFetureName(e);
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            updateFetureName(e);
        }

        private void updateFetureName(DocumentEvent e) {
            String text = getText(e);
            newAnnPanelRef.get().setFetureName(text);
            newAnnPanelRef.get().validateInput();
        }

        private String getText(DocumentEvent e) {
            Document doc = e.getDocument();
            String text = null;
            try {
                text = doc.getText(0, doc.getLength());
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
            return text;
        }
    }

    static class FetureKeyListener implements ActionListener {

        private WeakReference<AnnPanel> panelRef;

        FetureKeyListener(WeakReference<AnnPanel> panelRef) {
            this.panelRef = panelRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            FetureKey fetureKey = (FetureKey) panelRef.get().getFetureKeyComboBox().getSelectedItem();
            panelRef.get().setFetureKeyType(fetureKey.getName());
        }
    }

    static class AddFetureTypeBtnListener implements ActionListener {

        private WeakReference<AnnPanel> newAnnPanelRef;

        AddFetureTypeBtnListener(WeakReference<AnnPanel> newAnnPanelRef) {
            this.newAnnPanelRef = newAnnPanelRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            NewFetureKeyPanel newFetureKeyPanel = new NewFetureKeyPanel();
            int answer = DialogUtil.notify(newFetureKeyPanel, "New Feature Key", JOptionPane.OK_CANCEL_OPTION);

            if (answer == JOptionPane.OK_OPTION) {
                IFetureKeyService s = Lookup.getDefault().lookup(IFetureKeyService.class);

                FetureKey fk = newFetureKeyPanel.getFetureKey();
                s.create(fk);
                newAnnPanelRef.get().comboBoxModel = null;
                newAnnPanelRef.get().getComboBoxModel();
                newAnnPanelRef.get().getFetureKeyComboBox().setSelectedItem(fk);

            }
        }
    }

    static class EditQualifierListener implements ActionListener {

        private WeakReference<AnnPanel> panelRef;

        EditQualifierListener(WeakReference<AnnPanel> panelRef) {
            this.panelRef = panelRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = panelRef.get().getQualifierList().getSelectedIndex();
            String qualifier = (String) panelRef.get().getQualifierListModel().get(index);
            DefaultComboBoxModel fk = panelRef.get().getComboBoxModel();
            String type = fk.getSelectedItem().toString();
            NewQualPanel newQualifierPanel = new NewQualPanel(type, qualifier);
            DialogDescriptor dd = new DialogDescriptor(newQualifierPanel, "Edit Qualifier");
            newQualifierPanel.setDialogDescriptor(dd);
            newQualifierPanel.validateInput();
            
            Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);
            if (answer == JOptionPane.OK_OPTION) {
                final String qName = newQualifierPanel.getqName();
                final String line = newQualifierPanel.getQNameLine();
                int size = panelRef.get().getQualifierListModel().getSize();
                for (int i = 0; i < size; i++) {
                    String qua = (String) panelRef.get().getQualifierListModel().getElementAt(i);
                    if (qua.startsWith(qName + "=")) {
                        panelRef.get().getQualifierListModel().removeElementAt(i);
                        panelRef.get().getQualifierListModel().insertElementAt(line, i);
                    }
                }
                panelRef.get().getQualifierList().setSelectedIndex(0);
            }
        }
    }

    static class NewQualifierListener implements ActionListener {

        private WeakReference<AnnPanel> newAnnPanelRef;

        NewQualifierListener(WeakReference<AnnPanel> newAnnPanelRef) {
            this.newAnnPanelRef = newAnnPanelRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            DefaultComboBoxModel featureKey = newAnnPanelRef.get().getComboBoxModel();
            String type = featureKey.getSelectedItem().toString();
            NewQualPanel newQualifierPanel = new NewQualPanel(type, null);
            DialogDescriptor dd = new DialogDescriptor(newQualifierPanel, "New Qualifier");
            newQualifierPanel.setDialogDescriptor(dd);
            newQualifierPanel.validateInput();
            Integer answer = (Integer) DialogDisplayer.getDefault().notify(dd);
            if (answer == JOptionPane.OK_OPTION) {
                String qName = newQualifierPanel.getqName();
                String value = newQualifierPanel.getValue();
                newAnnPanelRef.get().getQualifierListModel().add(0, String.format("%1$s=%2$s", qName, value));
                newAnnPanelRef.get().getQualifierList().setSelectedIndex(0);

            }
        }
    }

    static class DelQualifierListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            AnnPanel newAnnPanel = UIUtil.getParent(src, AnnPanel.class);
            if (newAnnPanel == null) {
                return;
            }
            int index = newAnnPanel.getQualifierList().getSelectedIndex();
            if (index > -1) {
                newAnnPanel.getQualifierListModel().remove(index);
                int size = newAnnPanel.getQualifierListModel().getSize();

                if (size > 0) {
                    if (index == newAnnPanel.getQualifierListModel().getSize()) {
                        //removed item in last position
                        index--;
                    }
                    newAnnPanel.getQualifierList().setSelectedIndex(index);
                    newAnnPanel.getQualifierList().ensureIndexIsVisible(index);
                }
            }
        }
    }

    static class DelLocListener implements ActionListener {

        private WeakReference<AnnPanel> newAnnPanelRef;

        DelLocListener(WeakReference<AnnPanel> newAnnPanelRef) {
            this.newAnnPanelRef = newAnnPanelRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = newAnnPanelRef.get().getLocList().getSelectedIndex();
            if (index > -1) {
                newAnnPanelRef.get().getLocListModel().remove(index);
                int size = newAnnPanelRef.get().getLocListModel().getSize();

                if (size > 0) {
                    if (index == newAnnPanelRef.get().getLocListModel().getSize()) {
                        //removed item in last position
                        index--;
                    }
                    newAnnPanelRef.get().getLocList().setSelectedIndex(index);
                    newAnnPanelRef.get().getLocList().ensureIndexIsVisible(index);
                }
            }
            newAnnPanelRef.get().validateInput();
        }
    }
}
