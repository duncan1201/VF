/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.edit;

import org.openide.DialogDescriptor;

/**
 *
 * @author dq
 */
public interface IEditPrimerUI {
    void updateCodonCombo();
    void updateAminoAcidCombo();
    void updateRenCombo();
    void updateRenBasesCombo();
    void selectCustomPanel();
    void selectEnzymesPanel();
    void selectCodonsPanel();
    void attachClicked();
    void replaceClicked();
    // 5' to 3'
    String getExtension();
    // 5' to 3'
    String getPrimerSeq();
    void setDialogDescriptor(DialogDescriptor dialogDescriptor);
}
