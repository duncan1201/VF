/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.enzyme.core.ui;

import com.gas.common.ui.button.PrevBtn;
import com.gas.common.ui.core.LocList;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.ren.RENSet;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.domain.ui.ren.api.IRENAnalysisPanel;
import com.gas.domain.core.ren.IRMapService;
import com.gas.enzyme.core.ui.enzymesused.EnzymesUsedPanel;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class RENAnalPanelListeners {

    static class FindBtnListener implements ActionListener {

        private IRENAnalysisPanel renAnalysisPanel;
        private IMolPane molPane;

        public FindBtnListener(IRENAnalysisPanel enzymesUsedPanel) {
            this.renAnalysisPanel = enzymesUsedPanel;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            boolean anywhere = renAnalysisPanel.isAnywhere();
            boolean mustCut = renAnalysisPanel.isMustCut();

            boolean mustNotCut = renAnalysisPanel.isMustNotCut();
            Integer minOcc = renAnalysisPanel.getMinOccurrence();
            Integer maxOcc = renAnalysisPanel.getMaxOccurrence();
            Integer mustCutFrom = renAnalysisPanel.getMustCutFrom();
            Integer mustCutTo = renAnalysisPanel.getMustCutTo();
            Integer mustNotCutTo = renAnalysisPanel.getMustNotCutTo();
            Integer mustNotCutFrom = renAnalysisPanel.getMustNotCutFrom();

            String renListName = renAnalysisPanel.getSelectedRENList().getName();
            RENSet selectedRENs = renAnalysisPanel.getSelectedRENs();
            if (molPane == null) {
                molPane = UIUtil.getParent((Component)renAnalysisPanel, IMolPane.class);
            }

            String seq = molPane.getSequence();

            LocList selection = new LocList();
            Boolean allow ;
            if (anywhere) {
                selection.add(new Loc(mustCutFrom, mustCutTo));
                allow = null;
            } else if (mustCut) {
                selection.add(new Loc(mustCutFrom, mustCutTo));
                allow = true;
            } else if (mustNotCut) {
                selection.add(new Loc(mustNotCutFrom, mustNotCutTo));
                allow = false;
            } else {
                throw new IllegalArgumentException("something wrong!");
            }


            IRMapService service = Lookup.getDefault().lookup(IRMapService.class);
            RMap rmap = service.findRM(seq, molPane.isCircular(), renListName, selectedRENs, minOcc, maxOcc, selection, allow);
            if(rmap.getEntries().isEmpty()){
                final String title = "Restriction Analysis";
                final String template = "<html>No suitable restriction sites found:" + 
                        "<ul>" + 
                            "<li>%s restriction site(s) found</li>" +
                            "<li>%s restriction site(s) NOT within specified range</li>" + 
                            "<li>%s restriction site(s) occur TOO many times</li>" + 
                            "<li>%s restriction site(s) occur TOO few times</li>" + 
                        "</ul></html>";
                
                String msg = String.format(template, 
                        rmap.getAnalysisDetails().getTotalCandidates(), 
                        rmap.getAnalysisDetails().getOutOfRangeCount(),
                        rmap.getAnalysisDetails().getTooManyTimesCount(),
                        rmap.getAnalysisDetails().getTooFewTimesCount()
                );               
                DialogDescriptor.Message m = new DialogDescriptor.Message(msg, DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle(title);
                DialogDisplayer.getDefault().notify(m);
            }else{
                renAnalysisPanel.setRmap(rmap);
                renAnalysisPanel.enableLeftDecoration(true);
            }

            molPane.setRmap(rmap);
        }
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            RENAnalPanel src = (RENAnalPanel) evt.getSource();
            if (name.equals("rmap")) {               
                src.refresh();
            } else if (name.equals("totalLength")) {
                src.getRenAnalysisView().getCutSitesPanel().setTotalLength((Integer) v);
            } else if (name.equals("page")) {
                RENAnalPanel.PAGE page = (RENAnalPanel.PAGE) v;
                src.renAnalysisView.showPanel(page);

                if (page == RENAnalPanel.PAGE.SEARCH) {
                    src.prevBtnRef.get().goPrivate();

                    src.rightDecBtn.setIcon(ImageHelper.createImageIcon(ImageNames.PLAY_16));
                    src.rightDecBtn.setEnabled(true);

                } else if (page == RENAnalPanel.PAGE.RESULT) {
                    src.prevBtnRef.get().goPublic();
                    src.rightDecBtn.setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
                    src.rightDecBtn.setEnabled(false);
                }
            }
        }
    }

    static class PrevBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            PrevBtn btn = (PrevBtn) e.getSource();
            RENAnalPanel panel = UIUtil.getParent(btn, RENAnalPanel.class);
            panel.setPage(RENAnalPanel.PAGE.SEARCH);
        }
    }
}
