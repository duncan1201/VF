/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.dialog.DialogUtil;
import com.gas.common.ui.misc.InputPanel;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.primer.service.api.IUserInputService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.primer3.UserInput;
import com.gas.domain.core.primer3.UserInputList;
import com.gas.main.ui.molpane.MolPane;
import static com.gas.main.ui.molpane.sitepanel.primer3.Primer3PanelPopup.SAVE_CUR_SETTINGS_TO_MOL;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Date;
import javax.swing.JFileChooser;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class Primer3PanelPopupListeners {

    static class SaveListener implements ActionListener {

        private IAnnotatedSeqService serviceAs = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
        private IUserInputService userInputSvc = Lookup.getDefault().lookup(IUserInputService.class);
        private WeakReference<Primer3Panel> ref;

        SaveListener(Primer3Panel primer3Panel) {
            ref = new WeakReference<Primer3Panel>(primer3Panel);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String actionCommand = e.getActionCommand();
            if (actionCommand.equals(Primer3PanelPopup.CMD.SAVE_AS_NEW_TEMPLATE_CMD.name())) {
                UserInputList userInputList = new UserInputList(userInputSvc.getAll(false));

                InputPanel inputPanel = new InputPanel("Name:", "Description:");
                DialogDescriptor dd = new DialogDescriptor(inputPanel, Primer3PanelPopup.SAVE_CUR_SETTINGS_AS_A_NEW_TEMPLATE);
                inputPanel.setDialogDescriptor(dd);
                inputPanel.setExistingNames(userInputList.getNames());
                inputPanel.validateInput();
                Object answer = DialogDisplayer.getDefault().notify(dd);
                if (!answer.equals(DialogDescriptor.OK_OPTION)) {
                    return;
                }
                String name = inputPanel.getInputText();
                ref.get().updateUserInputFromUI();
                UserInput clonedUserInput = ref.get().getP3output().getUserInput().clone();
                clonedUserInput.setName(name);
                clonedUserInput.setDescription(inputPanel.getDescText());
                clonedUserInput.setUpdatedDate(new Date());
                userInputSvc.create(clonedUserInput);
                DialogUtil.showAutoCloseMessagePane(Primer3PanelPopup.SAVE_CUR_SETTINGS_AS_A_NEW_TEMPLATE, "The settings have been successfully saved");
            } else if (actionCommand.equals(Primer3PanelPopup.CMD.SAVE_TO_A_TEMPLATE_CMD.name())) {
                SelectTemplatePanel selectTemplatePanel = new SelectTemplatePanel();
                DialogDescriptor dd = new DialogDescriptor(selectTemplatePanel, Primer3PanelPopup.LOAD_SETTINGS_FROM_A_TEMPLATE);
                selectTemplatePanel.setDialogDescriptor(dd);
                selectTemplatePanel.validateInput();
                Object answer = DialogDisplayer.getDefault().notify(dd);
                if (answer.equals(DialogDescriptor.OK_OPTION)) {
                    UserInput userInput = selectTemplatePanel.getSelectedUserInput();

                    ref.get().updateUserInputFromUI();
                    UserInput userInputFromUI = ref.get().getP3output().getUserInput();
                    userInputSvc.mergeData(userInput, userInputFromUI);
                    DialogUtil.showAutoCloseMessagePane(Primer3PanelPopup.SAVE_CUR_SETTINGS_AS_A_NEW_TEMPLATE, "The settings have been successfully saved");
                }
            } else if (actionCommand.equals(Primer3PanelPopup.CMD.SAVE_TO_MOL.name())) {
                Primer3Panel primer3Panel = ref.get();
                primer3Panel.updateUserInputFromUI();
                MolPane molPane = UIUtil.getParent(primer3Panel, MolPane.class);
                AnnotatedSeq as = molPane.getAs();
                serviceAs.merge(as);
                DialogUtil.showAutoCloseMessagePane(Primer3PanelPopup.SAVE_CUR_SETTINGS_TO_MOL, "The settings have been successfully saved");
            }
        }
    }

    static class LoadListener implements ActionListener {

        private IUserInputService userInputSvc = Lookup.getDefault().lookup(IUserInputService.class);
        private IAnnotatedSeqService serviceAs = Lookup.getDefault().lookup(IAnnotatedSeqService.class);
        private WeakReference<Primer3Panel> panelRef;

        LoadListener(Primer3Panel panel) {
            this.panelRef = new WeakReference<Primer3Panel>(panel);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            if (cmd.equals(Primer3PanelPopup.CMD.LOAD_FROM_A_TEMPLATE.name())) {
                SelectTemplatePanel selectTemplatePanel = new SelectTemplatePanel();
                DialogDescriptor dd = new DialogDescriptor(selectTemplatePanel, Primer3PanelPopup.LOAD_SETTINGS_FROM_A_TEMPLATE);
                selectTemplatePanel.setDialogDescriptor(dd);
                selectTemplatePanel.validateInput();
                Object answer = DialogDisplayer.getDefault().notify(dd);
                if (answer.equals(DialogDescriptor.OK_OPTION)) {
                    UserInput userInput = selectTemplatePanel.getSelectedUserInput();
                    panelRef.get().getP3output().setUserInput(userInput);
                    panelRef.get().populateUI();

                    MolPane molPane = UIUtil.getParent(panelRef.get(), MolPane.class);
                    AnnotatedSeq as = molPane.getAs();
                    serviceAs.merge(as);
                    DialogUtil.showAutoCloseMessagePane(Primer3PanelPopup.LOAD_SETTINGS_FROM_A_TEMPLATE, "The settings have been successfully loaded");
                }
            } else if (cmd.equals(Primer3PanelPopup.CMD.LOAD_FROM_A_FILE.name())) {
                JFileChooser fc = new JFileChooser();
                int answer = UIUtil.showDialog(fc, null);

                if (answer == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    UserInput userInput = userInputSvc.importFromFile(file);
                    panelRef.get().getP3output().setUserInput(userInput);
                    panelRef.get().populateUI();
                    MolPane molPane = UIUtil.getParent(panelRef.get(), MolPane.class);
                    AnnotatedSeq as = molPane.getAs();
                    as.getP3output().setUserInput(userInput);
                    serviceAs.merge(as);
                    DialogUtil.showAutoCloseMessagePane(Primer3PanelPopup.LOAD_SETTINGS_FROM_A_FILE, "The settings have been successfully loaded");
                }
            }
        }
    }
}