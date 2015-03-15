/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3.edit;

import static com.gas.main.ui.molpane.sitepanel.primer3.edit.EditPrimerPanel.CMD_REN;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.ref.WeakReference;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author dq
 */
public class EditPrimerPanelListeners {
    
    
    
    public static class CustomBaseFieldListener implements DocumentListener {

        private WeakReference<IEditPrimerUI> ref;
        
        public CustomBaseFieldListener(IEditPrimerUI panel){
            ref = new WeakReference<IEditPrimerUI>(panel);
        }
        
        @Override
        public void insertUpdate(DocumentEvent e) {           
            ref.get().selectCustomPanel();           
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            ref.get().selectCustomPanel();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            ref.get().selectCustomPanel();
        }
    }
    
    public static class ComboAndBtnListeners implements ActionListener {
        
        private WeakReference<IEditPrimerUI> ref;
        public ComboAndBtnListeners(IEditPrimerUI panel){
            ref = new WeakReference<IEditPrimerUI>(panel);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if(cmd.equals(EditPrimerPanel.CMD_AMINO_ACID)){
                ref.get().updateCodonCombo();
                ref.get().selectCodonsPanel();
            }else if(cmd.equals(EditPrimerPanel.CMD_GENETIC_CODE)){
                ref.get().updateAminoAcidCombo();
                ref.get().updateCodonCombo();
                ref.get().selectCodonsPanel();
            }else if(cmd.equals(EditPrimerPanel.CMD_CODON)){
                ref.get().selectCodonsPanel();
            }else if(cmd.equals(EditPrimerPanel.CMD_RENLIST)){
                ref.get().updateRenCombo();
                ref.get().selectEnzymesPanel();
            }else if(cmd.equals(EditPrimerPanel.CMD_REN)){
                ref.get().updateRenBasesCombo();
                ref.get().selectEnzymesPanel();
            }else if(cmd.equals(EditPrimerPanel.CMD_REN_BASES)){                
                ref.get().selectEnzymesPanel();
            }else if(cmd.equals(EditPrimerPanel.CMD_ATTACH)){
                ref.get().attachClicked();
            }else if(cmd.equals(EditPrimerPanel.CMD_REPLACE)){
                ref.get().replaceClicked();
            }
        }
    }
}
