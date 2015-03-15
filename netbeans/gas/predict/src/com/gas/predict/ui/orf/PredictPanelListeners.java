/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.predict.ui.orf;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.orf.api.IORFService;
import com.gas.domain.core.orf.api.ORFParam;
import com.gas.domain.core.orf.api.ORFResult;
import com.gas.domain.ui.editor.IMolPane;
import com.gas.domain.ui.editor.as.IASEditor;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.JPanel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
class PredictPanelListeners {

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            PredictPanel src = (PredictPanel) evt.getSource();
            final String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equals("page")) {
                JPanel contentPane = src.titledPanel.getContentPane();
                CardLayout layout = (CardLayout) contentPane.getLayout();
                layout.show(contentPane, v.toString());

                PredictPanel.PAGE page = (PredictPanel.PAGE) v;
                if (page == PredictPanel.PAGE.RESULT) {
                    src.prevBtnRef.get().goPublic();

                    src.rightBtn.setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
                    src.rightBtn.setEnabled(false);
                } else if (page == PredictPanel.PAGE.SEARCH) {
                    src.prevBtnRef.get().goPrivate();

                    src.rightBtn.setEnabled(true);
                    src.rightBtn.setIcon(ImageHelper.createImageIcon(ImageNames.PLAY_16));
                }
            } else if (name.equals("orfResult")) {                
                src.refresh();
            }
        }
    }

    static class PrevBtnListener implements ActionListener {

        private WeakReference<PredictPanel> predictPanelRef;

        public PrevBtnListener(WeakReference<PredictPanel> predictPanelRef) {
            this.predictPanelRef = predictPanelRef;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            predictPanelRef.get().setPage(PredictPanel.PAGE.SEARCH);
        }
    }

    static class GoBtnListener implements ActionListener {

        private IORFService orfService = Lookup.getDefault().lookup(IORFService.class);
        private WeakReference<ORFPanel> orfPanelRef;
        private WeakReference<IMolPane> molPaneRef;
        private WeakReference<PredictPanel> predictPanelRef;

        public GoBtnListener(ORFPanel orfPanel) {
            this.orfPanelRef = new WeakReference<ORFPanel>(orfPanel);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<String> errorMsg = orfPanelRef.get().validateInput();
            if (!errorMsg.isEmpty()) {
                DialogDescriptor.Message m = new DialogDescriptor.Message(errorMsg.get(0), DialogDescriptor.INFORMATION_MESSAGE);
                m.setTitle("Cannot perform ORF");

                DialogDisplayer.getDefault().notify(m);
                return;
            }
            orfPanelRef.get().populateFromUI();
            ORFParam orfParam = orfPanelRef.get().orfResult.getOrfParams();
            IMolPane molPane = getMolPane();            
            
            orfParam.setSequence(molPane.getSequence());

            ORFResult orfResult = orfService.findORFResult(orfParam);

            getMolPane().getAs().setOrfResult(orfResult);

            getMolPane().refresh();
            getPredictPanel().getORFResultPanel().setORF(orfResult.getOrfSet().getOrfs());
            getPredictPanel().getORFResultPanel().sort();
            getPredictPanel().setPage(PredictPanel.PAGE.RESULT);

            //getPredictPanel().getORFPanel().save();

            IASEditor editor = UIUtil.getParent(orfPanelRef.get(), IASEditor.class);
            if (editor != null) {
                editor.setCanSave();
            }
        }

        private IMolPane getMolPane() {
            if (molPaneRef == null || molPaneRef.get() == null) {
                IMolPane molPane = UIUtil.getParent(orfPanelRef.get(), IMolPane.class);
                molPaneRef = new WeakReference<IMolPane>(molPane);
            }
            return molPaneRef.get();
        }

        private PredictPanel getPredictPanel() {
            if (predictPanelRef == null || predictPanelRef.get() == null) {
                PredictPanel p = UIUtil.getParent(orfPanelRef.get(), PredictPanel.class);
                predictPanelRef = new WeakReference<PredictPanel>(p);
            }
            return predictPanelRef.get();
        }
    }
}
