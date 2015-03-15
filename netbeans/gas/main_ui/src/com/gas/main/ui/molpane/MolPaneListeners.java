/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import com.gas.common.ui.core.LocList;
import com.gas.common.ui.misc.Loc;
import com.gas.common.ui.util.Pref;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.as.TranslationResult;
import com.gas.domain.core.gc.api.GCResult;
import com.gas.domain.core.ren.RMap;
import com.gas.domain.ui.editor.ISavableEditor;
import com.gas.main.ui.molpane.graphpane.GraphPane;
import com.gas.main.ui.molpane.graphpane.GraphPaneListeners;
import com.gas.main.ui.ringpane.RingGraphPanel;
import com.gas.main.ui.ringpane.RingPane;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.ref.WeakReference;
import java.util.Set;
import javax.swing.JScrollPane;

/**
 *
 * @author dq
 */
class MolPaneListeners {

    static class TextViewListener extends ComponentAdapter {

        boolean first = true;

        @Override
        public void componentShown(ComponentEvent e) {
            if (!first) {
                return;
            }
            JScrollPane src = (JScrollPane) e.getSource();
            src.getVerticalScrollBar().setValue(0);
            first = false;
        }
    }

    static class PrefListener implements PropertyChangeListener {

        private WeakReference<MolPane> molPaneRef;

        public PrefListener(MolPane molPane) {
            this.molPaneRef = new WeakReference<MolPane>(molPane);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (molPaneRef.get() == null || molPaneRef.get().getAs() == null) {
                return;
            }
            String name = evt.getPropertyName();
            final Object v = evt.getNewValue();

            if (name.equals("baseFontSize")) {
                molPaneRef.get().setBaseFontSize((Float) v);
            } else if (name.equals("annotationLabelSize")) {
                molPaneRef.get().setAnnotationLabelSize((Float) v);
            } else if (name.equals("rulerFontSize")) {
                molPaneRef.get().setRulerFontSize((Float) v);
            } else if (name.equals("editable")) {
                if (v != null) {
                    Boolean editable = Pref.CommonPtyPrefs.getInstance().getEditable();
                    molPaneRef.get().setEditable(editable);
                }
            }
        }
    }

    static class PtyChangeListener implements PropertyChangeListener {

        WeakReference<MolPane> molPaneRef;

        protected PtyChangeListener(MolPane molPane) {
            molPaneRef = new WeakReference<MolPane>(molPane);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            AnnotatedSeq as = molPaneRef.get().getAs();
            if (as == null) {
                return;
            }
            String name = evt.getPropertyName();
            Object src = evt.getSource();
            Object v = evt.getNewValue();
            if (name.equals("translationResults")) {

                if (as != null && as.isCircular()) {
                    molPaneRef.get().getRingPane().setTranslationResults((Set<TranslationResult>) v);
                } else if (as != null && !as.isCircular()) {
                    molPaneRef.get().getGraphPane().setTranslationResults((Set<TranslationResult>) v);
                }
            } else if (name.equals("rmap")) {
                RMap rmap = (RMap) v;

                as.setRmap(rmap);
                if (as != null && as.isCircular()) {
                    molPaneRef.get().getRingPane().getRingGraphPanel().setRmap(rmap);
                } else if (as != null && !as.isCircular()) {
                    molPaneRef.get().getGraphPane().getGraphPanel().setRmap(rmap);
                }

                ISavableEditor savableEditor = UIUtil.getParent(molPaneRef.get(), ISavableEditor.class);
                if (savableEditor != null) {
                    savableEditor.setCanSave();
                }
            } else if (name.equals("doubleStranded")) {
                Boolean doubleStranded = molPaneRef.get().getDoubleStranded();

                if (as != null && as.isCircular()) {
                    molPaneRef.get().getRingPane().getRingGraphPanel().setDoubleStranded(doubleStranded);
                }
                if (as != null && !as.isCircular()) {
                    molPaneRef.get().getGraphPane().getGraphPanel().setDoubleStranded(doubleStranded);
                }
            } else if (name.equals("minimapShown")) {
                Boolean minimapShown = molPaneRef.get().getMinimapShown();

                if (as != null && as.isCircular()) {
                    molPaneRef.get().getRingPane().setMinimapShown(minimapShown);
                }
                if (as != null && !as.isCircular()) {
                    molPaneRef.get().getGraphPane().setMinimapShown(minimapShown);
                }
            } else if (name.equals("baseNumberShown")) {
                Boolean shown = molPaneRef.get().getBaseNumberShown();

                if (as != null && as.isCircular()) {
                    molPaneRef.get().getRingPane().setBaseNumberShown(shown);
                } else if (as != null && !as.isCircular()) {
                    molPaneRef.get().getGraphPane().setBaseNumberShown(shown);
                }
            } else if (name.equals("rulerFontSize")) {
                Float size = (Float) v;
                if (as != null) {
                    molPaneRef.get().getSidePanel().getGeneralPanel().getFontPanel().setRulerFontSize(size.intValue());
                    if (!as.isCircular()) {
                        molPaneRef.get().getGraphPane().setRulerFontSize(size);
                    } else {
                        molPaneRef.get().getRingPane().getRingGraphPanel().setRulerFontSize(size);
                    }
                }
            } else if (name.equals("baseFontSize")) {
                Float size = (Float) v;
                if (as != null) {
                    molPaneRef.get().getSidePanel().getGeneralPanel().getFontPanel().setBaseFontSize(size.intValue());
                    if (!as.isCircular()) {
                        molPaneRef.get().getGraphPane().setBaseSize(size);
                    } else {
                        molPaneRef.get().getRingPane().getRingGraphPanel().setBaseFontSize(size);
                    }
                }
            } else if (name.equals("annotationLabelSize")) {
                Float size = (Float) v;
                if (as != null) {
                    molPaneRef.get().getSidePanel().getGeneralPanel().getFontPanel().setAnnotationLabelSize(size.intValue());
                    if (!as.isCircular()) {
                        molPaneRef.get().getGraphPane().setAnnotationLabelSize(size);
                    } else {
                        molPaneRef.get().getRingPane().setAnnotationLabelSize(size);
                    }
                }
            } else if (name.equals("editable")) {
                Boolean editable = (Boolean) v;

                if (as != null && as.isCircular()) {
                    molPaneRef.get().getRingPane().getRingGraphPanel().setEditingAllowed(editable);
                } else if (as != null && !as.isCircular()) {
                    molPaneRef.get().getGraphPane().getGraphPanel().setEditingAllowed(editable);
                }
            } else if (name.equals("gcResult")) {
                if (as != null && as.isCircular()) {
                    molPaneRef.get().getRingPane().getRingGraphPanel().setGCResult((GCResult) v);
                } else if (as != null && !as.isCircular()) {
                    molPaneRef.get().getGraphPane().getGraphPanel().setGCResult((GCResult) v);
                }
            }
        }
    }

    static class AsPrefListener implements PropertyChangeListener {

        private WeakReference<MolPane> molPaneRef;

        public AsPrefListener(MolPane molPane) {
            this.molPaneRef = new WeakReference<MolPane>(molPane);
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            AnnotatedSeq as = molPaneRef.get().getAs();
            if (name.equals("doubleStranded")) {
                Boolean doubleStranded = (Boolean) v;
                molPaneRef.get().setDoubleStranded(doubleStranded);
            } else if (name.equals("minimapShown")) {
                molPaneRef.get().setMinimapShown((Boolean) v);
            } else if (name.equals("baseNumberShown")) {
                molPaneRef.get().setBaseNumberShown((Boolean) v);
            } else if (name.equals("zoom")) {
                Float zoom = (Float) v;
                if (as != null && !as.isCircular()) {
                    GraphPane graphPane = molPaneRef.get().getGraphPane();
                    GraphPaneListeners.resetGraphPanelPreferredSize(graphPane, zoom);
                    LocList selections = graphPane.getSelections();
                    if (!selections.isEmpty()) {
                        graphPane.center(selections.first());
                    }
                } else if (as != null && as.isCircular()) {
                    RingPane ringPane = molPaneRef.get().getRingPane();
                    RingGraphPanel ringPanel = ringPane.getRingGraphPanel();
                    if (ringPanel == null) {
                        return;
                    }
                    Dimension size = ringPanel.getFullSize();
                    if (size != null) {
                        Dimension newSize = new Dimension(Math.round(size.width * zoom), Math.round(size.height * zoom));
                        ringPanel.setPreferredSize(newSize);
                        ringPanel.revalidate();
                    }
                    Loc selectedLoc = ringPanel.getSelectedLoc();
                    if (selectedLoc != null) {
                        molPaneRef.get().getRingPane().getScrollBar().setValue(selectedLoc.center());
                    }
                }
            } else {
                if (as != null && as.isCircular()) {
                    molPaneRef.get().getRingPane().setTrackVisible(name, (Boolean) v);
                } else if (as != null && !as.isCircular()) {
                    molPaneRef.get().getGraphPane().setTrackVisible(name, (Boolean) v);
                }
            }
        }
    }
}
