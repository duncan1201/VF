/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.misc.InputPanel;
import com.gas.common.ui.progress.ProgRunnable;
import com.gas.common.ui.progress.ProgressHandle;
import com.gas.common.ui.progress.ProgressHelper;
import com.gas.common.ui.util.ReflectHelper;
import com.gas.common.ui.util.UIUtil;
import com.gas.database.core.as.service.api.IAnnotatedSeqService;
import com.gas.database.core.primer.service.api.IUserInputService;
import com.gas.domain.core.as.AnnotatedSeq;
import com.gas.domain.core.primer3.IPrimer3Service;
import com.gas.domain.core.primer3.IUserInputFactory;
import com.gas.domain.core.primer3.P3Output;
import com.gas.domain.core.primer3.UserInput;
import com.gas.domain.core.primer3.UserInputList;
import com.gas.main.ui.editor.as.ASEditor;
import com.gas.main.ui.molpane.MolPane;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.Lookup;
import org.openide.windows.WindowManager;

/**
 *
 * @author dq
 */
public class Primer3PanelListeners {

    static class LeftBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();
            ActionListener action = (ActionListener) ReflectHelper.newInstance(cmd, this.getClass().getClassLoader());

            action.actionPerformed(e);
        }
    }

    public static class BackListener implements ActionListener {

        public BackListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton src = (JButton) e.getSource();
            Primer3Panel primer3panel = UIUtil.getParent(src, Primer3Panel.class);
            primer3panel.setPage(Primer3Panel.PAGE.INPUT);
        }
    }

    public static class SettingsListener implements ActionListener {

        private JPopupMenu popup = null;
        private JButton btn;

        public SettingsListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton btn = (JButton) e.getSource();
            this.btn = btn;
            final Dimension sizeBtn = btn.getPreferredSize();
            JPopupMenu p = getPopupMenu();
            final Dimension sizePopup = p.getPreferredSize();
            int x = 0;            
            getPopupMenu().show(btn, x, sizeBtn.height);
        }

        private Primer3Panel getPrimer3Panel() {
            Primer3Panel primer3Panel = UIUtil.getParent(btn, Primer3Panel.class);
            return primer3Panel;
        }

        private JPopupMenu getPopupMenu() {
            if (popup == null) {
                popup = new Primer3PanelPopup(getPrimer3Panel());
                /*
                JMenuItem merge = new JMenuItem("Save Current Settings to Molecule");
                merge.addActionListener(new SaveSettingToMoleculeListener(getPrimer3Panel()));
                popup.add(merge);
                
                JMenuItem saveToNewTemplate = new JMenuItem("Save Current Settings as a Template...");
                saveToNewTemplate.addActionListener(new SaveSettingsToNewTemplateListener(getPrimer3Panel()));
                popup.add(saveToNewTemplate);                       
                                
                JMenuItem loadFromATemplate = new JMenuItem("Load Settings From a Template");
                loadFromATemplate.addActionListener(new LoadFromATemplateListener());
                popup.add(loadFromATemplate);
                
                JMenuItem loadFromAFile = new JMenuItem("Load Settings From a File");
                loadFromAFile.addActionListener(new LoadFromAFileListener());
                popup.add(loadFromAFile);                

                popup.addSeparator();
                
                JMenuItem manage = new JMenuItem("Manage Setting Templates...", ImageHelper.createImageIcon(ImageNames.EMPTY_16));
                manage.addActionListener(new ManageSettingBtnListener());
                popup.add(manage);

                JMenuItem reset = new JMenuItem("Reset to p3web v3.0.0 Default", ImageHelper.createImageIcon(ImageNames.EMPTY_16));
                reset.addActionListener(new ResetBtnListener(getPrimer3Panel()));
                popup.add(reset);
*/
            }
            return popup;
        }
    }    

    private static class PopupListener implements PopupMenuListener {

        private IUserInputService service = Lookup.getDefault().lookup(IUserInputService.class);
        private WeakReference<Primer3Panel> panelRef;

        private PopupListener(Primer3Panel panel) {
            panelRef = new WeakReference<Primer3Panel>(panel);
        }

        @Override
        public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            JPopupMenu src = (JPopupMenu) e.getSource();
            List<UserInput> userInputs = service.getAll(false);
            for (UserInput userInput : userInputs) {
                final String name = userInput.getName();
                JMenuItem item = new JMenuItem(name);
                item.setActionCommand("userInput");
                item.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        UserInput userInput = service.getFullByName(name);
                        UserInput userInputCloned = userInput.clone();
                        Primer3Panel primer3Panel = UIUtil.getParent(panelRef.get(), Primer3Panel.class);
                        primer3Panel.getP3output().setUserInput(userInputCloned); 
                        primer3Panel.populateUI();
                        service.setFavorite(userInput);
                    }
                });
                if (userInput.isFavorite()) {
                    item.setIcon(ImageHelper.createImageIcon(ImageNames.CHECK_16));
                }
                src.add(item);
            }
        }

        @Override
        public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            List<JMenuItem> toBeRemoved = new ArrayList<JMenuItem>();
            JPopupMenu src = (JPopupMenu) e.getSource();
            for (int i = 0; i < src.getComponentCount(); i++) {
                Component comp = src.getComponent(i);
                if (comp instanceof JMenuItem) {
                    JMenuItem item = (JMenuItem) comp;
                    if (item.getActionCommand().equals("userInput")) {
                        toBeRemoved.add(item);
                    }
                }
            }
            for (JMenuItem removed : toBeRemoved) {
                src.remove(removed);
            }
        }

        @Override
        public void popupMenuCanceled(PopupMenuEvent e) {
        }
    }

    static class ManageSettingBtnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            ManageSettingsPanel panel = new ManageSettingsPanel();
            DialogDescriptor dd = new DialogDescriptor(panel, "Manage Setting Templates");
            panel.setDialogDescriptor(dd);

            Object answer = DialogDisplayer.getDefault().notify(dd);
            if (answer.equals(DialogDescriptor.OK_OPTION)) {
                List<UserInput> userInputs = panel.getUserInputs();

                IUserInputService service = Lookup.getDefault().lookup(IUserInputService.class);
                service.merge(userInputs);
            }
        }
    }

    static class ResetBtnListener implements ActionListener {

        private WeakReference<Primer3Panel> primer3PanelRef;

        public ResetBtnListener(Primer3Panel panel) {
            primer3PanelRef = new WeakReference<Primer3Panel>(panel);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Primer3Panel primer3Panel = primer3PanelRef.get();
            UserInput userInput = primer3Panel.getP3output().getUserInput();
            IUserInputFactory userInputFactory = Lookup.getDefault().lookup(IUserInputFactory.class);
            UserInput defaultUserInput = userInputFactory.getP3WEB_V_3_0_0();
            userInput.resetData(defaultUserInput.getData());

            primer3Panel.populateUI();
        }
    }

    static class PtyListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Primer3Panel src = (Primer3Panel) evt.getSource();
            String name = evt.getPropertyName();
            Object v = evt.getNewValue();
            if (name.equals("p3output")) {
                P3Output p = (P3Output) v;
                if (p != null && p.getUserInput() == null) {
                    IUserInputService userInputService = Lookup.getDefault().lookup(IUserInputService.class);
                    UserInput userInput = userInputService.getFavorite();
                    UserInput userInputCloned = userInput.clone();
                    p.setUserInput(userInputCloned);
                }
                src.populateUI();
                if (p != null && !p.getOligos().isEmpty()) {
                    src.setPage(Primer3Panel.PAGE.OUTPUT);
                }
            } else if (name.equals("page")) {
                CardLayout layout = (CardLayout) src.titledPanel.getContentPane().getLayout();
                if (src.page == Primer3Panel.PAGE.INPUT) {
                    layout.show(src.titledPanel.getContentPane(), src.page.toString());
                    src.leftBtn.setIcon(ImageHelper.createImageIcon(ImageNames.GEAR_16));
                    src.leftBtn.setActionCommand(Primer3PanelListeners.SettingsListener.class.getName());

                    src.rightBtn.setIcon(ImageHelper.createImageIcon(ImageNames.PLAY_16));
                    src.rightBtn.setEnabled(true);
                } else if (src.page == Primer3Panel.PAGE.OUTPUT) {
                    layout.show(src.titledPanel.getContentPane(), src.page.toString());

                    src.leftBtn.setIcon(ImageHelper.createImageIcon(ImageNames.BR_PREV_16));
                    src.leftBtn.setActionCommand(Primer3PanelListeners.BackListener.class.getName());
                    
                    src.rightBtn.setIcon(ImageHelper.createImageIcon(ImageNames.EMPTY_16));
                    src.rightBtn.setEnabled(false);
                }
            }
        }
    }

    static class PickPrimerBtnListener implements ActionListener {

        private MolPane molPane;
        private ASEditor editor;

        public PickPrimerBtnListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final JButton src = (JButton) e.getSource();
            if (molPane == null) {
                molPane = UIUtil.getParent(src, MolPane.class);
            }
            if (editor == null) {
                editor = UIUtil.getParent(src, ASEditor.class);
            }
            final AnnotatedSeq as = molPane.getAs();
            String seq = as.getSiquence().getData();

            final Primer3Panel primer3Panel = UIUtil.getParent(src, Primer3Panel.class);
            List<String> errorMsg = primer3Panel.validateInput();
            if (!errorMsg.isEmpty()) {
                DialogDescriptor.Message me = new DialogDescriptor.Message(errorMsg.get(0), DialogDescriptor.INFORMATION_MESSAGE);
                me.setTitle("Cannot perform Primer3");
                DialogDisplayer.getDefault().notify(me);
                return;
            }
            primer3Panel.updateUserInputFromUI();
            final UserInput userInput = primer3Panel.getP3output().getUserInput();
            if (userInput == null) {
                return;
            }
            userInput.set("SEQUENCE_TEMPLATE", seq);

            Frame frame = WindowManager.getDefault().getMainWindow();

            ProgRunnable runnable = new ProgRunnable() {
                P3Output p3output;

                @Override
                public void run(ProgressHandle handle) {
                    handle.setIndeterminate(true);
                    handle.progress("Running Primer 3...");
                    IPrimer3Service primer3Service = Lookup.getDefault().lookup(IPrimer3Service.class);

                    primer3Service.setFormatOutput(false);

                    p3output = primer3Service.execute(userInput, P3Output.class);
                    if(p3output == null){
                        return;
                    }
                }

                @Override
                public void done(ProgressHandle handle) {                    
                    if(p3output == null){
                        return;
                    }else if (p3output.getOligoSize() == 0) {
                        String explainsHtml = p3output.getExplainsInHtml("No Primers Found!");
                        DialogDescriptor.Message m = new DialogDescriptor.Message(explainsHtml, DialogDescriptor.PLAIN_MESSAGE);
                        m.setTitle("Primer3");
                        DialogDisplayer.getDefault().notify(m);
                        return;
                    }

                    primer3Panel.leftBtn.setEnabled(true);
                    primer3Panel.setPage(Primer3Panel.PAGE.OUTPUT);
                    primer3Panel.outPanel.setP3output(p3output);
                    as.setP3output(p3output);

                    molPane.refresh();
                    editor.setCanSave();
                }
            };

            ProgressHelper.showProgressDialogAndRun(frame, runnable, "Primer3");
        }
    }
}
