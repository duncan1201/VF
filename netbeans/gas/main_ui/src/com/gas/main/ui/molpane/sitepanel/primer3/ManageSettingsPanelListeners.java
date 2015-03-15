/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.misc.InputPanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.primer.service.api.IUserInputService;
import com.gas.domain.core.primer3.IUserInputFactory;
import com.gas.domain.core.primer3.UserInput;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
class ManageSettingsPanelListeners {
    
    private static IUserInputService service = Lookup.getDefault().lookup(IUserInputService.class);
    
    static class TableListener implements ListSelectionListener{

        private WeakReference<ManageSettingsPanel> panelRef;
        
        TableListener(ManageSettingsPanel panel){
            panelRef = new WeakReference<ManageSettingsPanel>(panel);
        }
        
        @Override
        public void valueChanged(ListSelectionEvent e) {           
            panelRef.get().getDelBtn().setEnabled(panelRef.get().getRowCount() > 1);
            panelRef.get().getExportBtn().setEnabled(true);
        }
    }
    
    static class NewListener implements ActionListener{

        private WeakReference<ManageSettingsPanel> panelRef;
        
        NewListener(ManageSettingsPanel panel){
            panelRef = new WeakReference<ManageSettingsPanel>(panel);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            IUserInputFactory userInputFactory = Lookup.getDefault().lookup(IUserInputFactory.class);
            UserInput userInput = userInputFactory.getP3WEB_V_3_0_0();
            String name = panelRef.get().getNewName("New Setting");
            userInput.setName(name);
            userInput.setFavorite(false);
            service.create(userInput);
            
            panelRef.get().addUserInput(userInput);
        }
    }
    
    static class DelListener implements ActionListener {

        private WeakReference<ManageSettingsPanel> panelRef;
        
        DelListener(ManageSettingsPanel panel){
            panelRef = new WeakReference<ManageSettingsPanel>(panel);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            UserInput userInput = panelRef.get().getSelectedUserInput();
            int indexSelection = panelRef.get().getSelectionIndex();
            DialogDescriptor.Confirmation cf = new DialogDescriptor.Confirmation(String.format("Are you sure you want to PERMANENTLY delete setting \"%s\"?", userInput.getName()), "Delete Settings");
            Object answerDel = DialogDisplayer.getDefault().notify(cf);
            if(!answerDel.equals(DialogDescriptor.OK_OPTION)){
                return;
            }            
            service.delete(userInput);
            panelRef.get().deleteSelectdUserInput();
            panelRef.get().setSelection(indexSelection);
            if(userInput.isFavorite()){
                panelRef.get().setFavoriteSetting();
            }            
        }
    }

    static class ImportListener implements ActionListener{

        private WeakReference<ManageSettingsPanel> panelRef;
        
        ImportListener(ManageSettingsPanel panel){
            panelRef = new WeakReference<ManageSettingsPanel>(panel);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser chooser = new JFileChooser();
            int answer = UIUtil.showDialog(chooser, WindowManager.getDefault().getMainWindow());
            if(JFileChooser.APPROVE_OPTION == answer){
                InputPanel inputPanel = new InputPanel("New setting name:");
                DialogDescriptor dd = new DialogDescriptor(inputPanel, "New setting name");
                inputPanel.setDialogDescriptor(dd);
                
                inputPanel.validateInput();
                
                Object answerName = DialogDisplayer.getDefault().notify(dd);
                if(!answerName.equals(DialogDescriptor.OK_OPTION)){
                    return;
                }
                String name = inputPanel.getInputText();
                
                File file = chooser.getSelectedFile();
                UserInput userInput = service.importFromFile(file);
                userInput.setName(name);
                userInput.setUpdatedDate(new Date());
                service.create(userInput);
                panelRef.get().addUserInput(userInput);
            }
        }
    }
    
    static class ExportListener implements ActionListener{

        private WeakReference<ManageSettingsPanel> panelRef;
        
        ExportListener(ManageSettingsPanel panel){
            panelRef = new WeakReference<ManageSettingsPanel>(panel);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            UserInput userInput = panelRef.get().getSelectedUserInput();
            if(userInput == null){
                return;
            }
            
            JFileChooser chooser = new JFileChooser();
            File selectedFile = new File(chooser.getCurrentDirectory(), userInput.getName() + ".txt");
            chooser.setSelectedFile(selectedFile);
            int answerSave = chooser.showSaveDialog(WindowManager.getDefault().getMainWindow());
            if(JFileChooser.APPROVE_OPTION == answerSave){
                File file = chooser.getSelectedFile();
                if(file.exists()){
                    DialogDescriptor.Confirmation cf = new DialogDescriptor.Confirmation(String.format("File \'%s\' already exists, overwrite it?", file.getName()));
                    Object answerOverwrite = DialogDisplayer.getDefault().notify(cf);
                    if(!answerOverwrite.equals(DialogDescriptor.OK_OPTION)){
                        return;
                    }
                }
                UserInput full = service.getFullByHibernateId(userInput.getHibernateId());
                service.export(full, file);
            }
        }
    }
}
