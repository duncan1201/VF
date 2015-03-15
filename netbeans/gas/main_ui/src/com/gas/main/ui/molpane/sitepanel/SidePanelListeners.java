/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel;

import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.AsHelper;
import com.gas.domain.core.as.Feture;
import com.gas.domain.core.as.Reference;
import com.gas.domain.core.orf.api.ORFResult;
import com.gas.domain.core.primer3.P3Output;
import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author dq
 */
class SidePanelListeners {

    static class TabbedPaneChangeLtr implements ChangeListener {

        private Dimension oSize;

        TabbedPaneChangeLtr(Dimension oSize) {
            this.oSize = oSize;
        }

        @Override
        public void stateChanged(ChangeEvent e) {
            JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
            tabbedPane.setPreferredSize(oSize);
        }
    }

    static class PtyChangeListener implements PropertyChangeListener {

        PtyChangeListener() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            SidePanel sidePanel = (SidePanel) evt.getSource();
            String name = evt.getPropertyName();
            if (name.equals("as")) {
                AnnotatedSeq as = sidePanel.getAs();
                boolean isAA = AsHelper.isAminoAcid(as);
                Map<String, List<Feture>> fetureMap = AsHelper.getSortedFetureMap(as);

                sidePanel.getAnnotationPanel().setFetureMap(fetureMap);

                Set<Reference> refs = as.getSortedReference(new Reference.PubYearComparator(false));
                sidePanel.referencesPanelRef.get().setReference(refs);

                sidePanel.generalPanel.setDoubleForm(as.getAsPref().isDoubleStranded());
                sidePanel.generalPanel.setBaseNumber(as.getAsPref().isBaseNumberShown());
                sidePanel.generalPanel.setMinimap(as.getAsPref().isMinimapShown());

                if (!as.getTranslationResults().isEmpty()) {
                    sidePanel.generalPanel.setTranslationResults(as.getTranslationResults());
                }

                sidePanel.setForProtein(isAA);
                if (!as.isOligo() && as.getRmap() != null) {
                    sidePanel.renAnalysisPanel.setRmap(as.getRmap());
                    sidePanel.renAnalysisPanel.enableLeftDecoration(true);
                }
                if (!as.isOligo() && as.getOrfResult() != null) {
                    sidePanel.predictPanelRef.get().refresh();
                }
                if (!as.isOligo() && as.getP3output() == null) {
                    P3Output p3output = new P3Output();
                    as.setP3output(p3output);
                }
                sidePanel.primer3Panel.setP3output(as.getP3output());
                if (!isAA) {
                    sidePanel.renAnalysisPanel.setTotalLength(as.getLength());
                }
                if (as.getOrfResult() == null) {
                    as.setOrfResult(new ORFResult());
                }
                if (sidePanel.predictPanelRef != null && sidePanel.predictPanelRef.get() != null) {
                    sidePanel.predictPanelRef.get().refresh();
                }
                if (as.getGcResult() != null) {
                    sidePanel.analysisPanelRef.get().setGCResult(as.getGcResult());
                }
                if (as.isOligo()) {
                    removeUIIfOligo(sidePanel);
                }
            } else if (name.equals("forProtein")) {
                Boolean forProtein = sidePanel.getForProtein();

                JTabbedPane tabbedPane = sidePanel.getTabbedPane();
                if (forProtein) {

                    int index = tabbedPane.indexOfComponent(sidePanel.getRenAnalysisPanel());
                    if (index > -1) {
                        tabbedPane.remove(index);
                    }
                    index = tabbedPane.indexOfComponent(sidePanel.getPrimer3Panel());
                    if (index > -1) {
                        tabbedPane.remove(index);
                    }

                    index = tabbedPane.indexOfComponent((JPanel) sidePanel.getPredictPanel());
                    if (index > -1) {
                        tabbedPane.remove(index);
                    }

                    index = tabbedPane.indexOfComponent((JPanel) sidePanel.getAnalysisPanel());
                    if (index > -1) {
                        tabbedPane.remove(index);
                    }

                }

                sidePanel.getGeneralPanel().setForProtein(forProtein);

            }
        }

        private void removeUIIfOligo(SidePanel sidePanel) {
            JTabbedPane tabbedPane = sidePanel.getTabbedPane();
            int index;

            index = tabbedPane.indexOfComponent(sidePanel.getReferencesPanel());
            if (index > -1) {
                tabbedPane.remove(index);
            }

            index = tabbedPane.indexOfComponent(sidePanel.getRenAnalysisPanel());
            if (index > -1) {
                tabbedPane.remove(index);
            }

            index = tabbedPane.indexOfComponent((JPanel) sidePanel.getPredictPanel());
            if (index > -1) {
                tabbedPane.remove(index);
            }

            index = tabbedPane.indexOfComponent(sidePanel.getPrimer3Panel());
            if (index > -1) {
                tabbedPane.remove(index);
            }
        }
    }
}
