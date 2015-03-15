/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane.sitepanel.primer3;

import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 *
 * @author dq
 */
public class Primer3PanelPopup extends JPopupMenu {

    public final static String SAVE_CUR_SETTINGS_TO_MOL = "Save Current Settings to Molecule";
    public final static String SAVE_CUR_SETTINGS_TO_EXISTING_TEMPLATE = "Save Current Settings to template";
    public final static String SAVE_CUR_SETTINGS_AS_A_NEW_TEMPLATE = "Save Current Settings as a New Template";
    public final static String LOAD_SETTINGS_FROM_A_TEMPLATE = "Load Settings From a Template";
    public final static String LOAD_SETTINGS_FROM_A_FILE = "Load Settings From a File";
    
    // commands
    public enum CMD{SAVE_TO_MOL, SAVE_AS_NEW_TEMPLATE_CMD, SAVE_TO_A_TEMPLATE_CMD, LOAD_FROM_A_FILE, LOAD_FROM_A_TEMPLATE};
    
    Primer3PanelPopup(Primer3Panel primer3Panel) {
        ActionListener listener = new Primer3PanelPopupListeners.SaveListener(primer3Panel);
        
        JMenuItem merge = new JMenuItem(SAVE_CUR_SETTINGS_TO_MOL);
        merge.setActionCommand(CMD.SAVE_TO_MOL.name());
        merge.addActionListener(listener);
        add(merge);

        JMenuItem saveToNewTemplate = new JMenuItem(SAVE_CUR_SETTINGS_AS_A_NEW_TEMPLATE + "...");
        saveToNewTemplate.setActionCommand(CMD.SAVE_AS_NEW_TEMPLATE_CMD.name());
        saveToNewTemplate.addActionListener(listener);
        add(saveToNewTemplate);

        JMenuItem saveToATemplate = new JMenuItem(SAVE_CUR_SETTINGS_TO_EXISTING_TEMPLATE + "...");
        saveToATemplate.addActionListener(listener);
        add(saveToATemplate);
        
        ActionListener loadListener = new Primer3PanelPopupListeners.LoadListener(primer3Panel);
        JMenuItem loadFromATemplate = new JMenuItem(LOAD_SETTINGS_FROM_A_TEMPLATE + "...");
        loadFromATemplate.setActionCommand(CMD.LOAD_FROM_A_TEMPLATE.name());
        loadFromATemplate.addActionListener(loadListener);
        add(loadFromATemplate);

        JMenuItem loadFromAFile = new JMenuItem(LOAD_SETTINGS_FROM_A_FILE);
        loadFromAFile.setActionCommand(CMD.LOAD_FROM_A_FILE.name());
        loadFromAFile.addActionListener(loadListener);
        add(loadFromAFile);

        addSeparator();

        JMenuItem manage = new JMenuItem("Manage Setting Templates...", ImageHelper.createImageIcon(ImageNames.EMPTY_16));
        manage.addActionListener(new Primer3PanelListeners.ManageSettingBtnListener());
        add(manage);

        JMenuItem reset = new JMenuItem("Reset to p3web v3.0.0 Default", ImageHelper.createImageIcon(ImageNames.EMPTY_16));
        reset.addActionListener(new Primer3PanelListeners.ResetBtnListener(primer3Panel));
        add(reset);

        //popup.addPopupMenuListener(new PopupListener(getPrimer3Panel()));    
    }
}
