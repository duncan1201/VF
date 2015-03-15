/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.contigs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.RowFilter;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author dq
 */
class VariantsPanelListeners {
    static class PtyChangeListener implements PropertyChangeListener {

        VariantsPanel variantsPanel;
        
        PtyChangeListener(VariantsPanel variantsPanel){
            this.variantsPanel = variantsPanel;
        }
        
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                String pName = evt.getPropertyName();
                if (pName.equals("variantMapMdl")) {
                    variantsPanel.getVariantsTable().setVariantMapMdl(variantsPanel.variantMapMdl);
                    variantsPanel.ambiguityCheckbox.setText(String.format("Ambiguity(%d)", variantsPanel.variantMapMdl.getAmbiguityCount()));
                    variantsPanel.ambiguityCheckbox.setSelected(true);
                    variantsPanel.transitionCheckbox.setText(String.format("Transition(%d)", variantsPanel.variantMapMdl.getTransitionCount()));
                    variantsPanel.transitionCheckbox.setSelected(true);
                    variantsPanel.transversionCheckbox.setText(String.format("Transversion(%d)", variantsPanel.variantMapMdl.getTransversionCount()));
                    variantsPanel.transversionCheckbox.setSelected(true);
                    variantsPanel.gapCheckbox.setText(String.format("Gap(%d)", variantsPanel.variantMapMdl.getGapCount()));
                    variantsPanel.gapCheckbox.setSelected(true);
                    variantsPanel.insertionCheckbox.setText(String.format("Insertion(%d)", variantsPanel.variantMapMdl.getInsertionCount()));
                    variantsPanel.insertionCheckbox.setSelected(true);
                } else if (pName.equals("includeGaps")) {
                    if (!variantsPanel.adjustingCheckbox) {
                        variantsPanel.gapCheckbox.setSelected(variantsPanel.includeGaps);
                    }
                } else if (pName.equals("includeAmbiguitis")) {
                    if (!variantsPanel.adjustingCheckbox) {
                        variantsPanel.ambiguityCheckbox.setSelected(variantsPanel.includeAmbiguitis);
                    }
                } else if (pName.equals("includeTransversions")) {
                    if (!variantsPanel.adjustingCheckbox) {
                        variantsPanel.transversionCheckbox.setSelected(variantsPanel.includeTransversions);
                    }
                } else if (pName.equals("includeTransitions")) {
                    if (!variantsPanel.adjustingCheckbox) {
                        variantsPanel.transitionCheckbox.setSelected(variantsPanel.includeTransitions);
                    }
                } else if (pName.equals("includeTransitions")) {
                    if (!variantsPanel.adjustingCheckbox) {
                        variantsPanel.insertionCheckbox.setSelected(variantsPanel.includeInsertions);
                    }
                }

                if (pName.equals("includeGaps")
                        || pName.equals("includeAmbiguitis")
                        || pName.equals("includeInsertions")
                        || pName.equals("includeTransversions")
                        || pName.equals("includeTransitions")) {
                    RowFilter rf = variantsPanel.createRowFilter();
                    TableRowSorter tableRowSorter = (TableRowSorter) variantsPanel.getVariantsTable().getRowSorter();
                    tableRowSorter.setRowFilter(rf);
                }
            }
    }
}
