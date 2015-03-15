/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.tigr.core.ui.ctrl.settings;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.domain.core.tigr.TIGRSettings;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author dq
 */
public class SettingsPanel extends JPanel {

    JButton assemblyBtn;
    TitledPanel titledPanel;
    SettingsContentPanel settingsContentPanel;

    public SettingsPanel() {
        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        titledPanel = new TitledPanel("LIGR Assembler Parameters");
        assemblyBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.PLAY_16));
        titledPanel.setRightDecoration(assemblyBtn);

        settingsContentPanel = new SettingsContentPanel();
        titledPanel.getContentPane().setLayout(new BorderLayout());
        titledPanel.getContentPane().add(settingsContentPanel, BorderLayout.CENTER);

        add(titledPanel, BorderLayout.CENTER);
    }
    
    private void hookupListeners(){
        assemblyBtn.addActionListener(new SettingsPanelListeners.AssemblyListener());
    }
    
    public TIGRSettings getSettings(){
        return settingsContentPanel.getSettingsFromUI();
    }

    public ImageIcon getImageIcon() {
        return ImageHelper.createImageIcon(ImageNames.GEAR_16);
    }
    
    public void setTigrSettings(TIGRSettings settings){
        settingsContentPanel.setTigrSettings(settings);
    }
}
