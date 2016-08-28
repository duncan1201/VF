/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.UserInput;
import com.gas.main.ui.molpane.sitepanel.primer3.InternalOligoTmCalSettingsPanel;
import com.gas.main.ui.molpane.sitepanel.primer3.PrimerTmCalSettingsPanel;
import com.gas.main.ui.molpane.sitepanel.primer3.TmCalSettingsPanel;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author dq
 */
class AddPrimerPanel extends JPanel {
    
    UserInput userInput;
    JRadioButton forward;
    JRadioButton probe;
    JRadioButton reverse;
    JPanel tmSettingsPanel;
    TmCalSettingsPanel primerSettingsPanel;
    TmCalSettingsPanel probeSettings;
    String PROBE_SETTINGS_PANEL = "probeSettings";
    String PRIMER_SETTINGS_PANEL = "primerSettings";
    
    AddPrimerPanel(UserInput userInput){
        this.userInput = userInput;
        UIUtil.setDefaultBorder(this);        
        setLayout(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel p = create();
        add(p, c);
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        tmSettingsPanel = createTmCalculationPanel();
        add(tmSettingsPanel, c);
        
        hookupListeners();
    }
    
    void showProbeTmSettings(){
        CardLayout l = (CardLayout)tmSettingsPanel.getLayout();
        l.show(tmSettingsPanel, PROBE_SETTINGS_PANEL);
    }
    
    void showPrimersTmSettings(){
        CardLayout l = (CardLayout)tmSettingsPanel.getLayout();
        l.show(tmSettingsPanel, PRIMER_SETTINGS_PANEL);
    }    
    
    private void hookupListeners(){        
        forward.addActionListener(new AddPrimerPanelListeners.RadioBtnsListener());
        probe.addActionListener(new AddPrimerPanelListeners.RadioBtnsListener());
        reverse.addActionListener(new AddPrimerPanelListeners.RadioBtnsListener());
    }
    
    private JPanel createTmCalculationPanel(){
        JPanel ret = new JPanel(new CardLayout());
        ret.setBorder(BorderFactory.createTitledBorder( //
                new LineBorder(Color.GRAY), //
                "Tm Settings", //
                TitledBorder.LEFT, // 
                TitledBorder.TOP, //
                UIManager.getFont("TitledBorder.font")));
        primerSettingsPanel = new PrimerTmCalSettingsPanel();
        primerSettingsPanel.populateUI(userInput);
        ret.add(primerSettingsPanel, PRIMER_SETTINGS_PANEL);
        
        probeSettings = new InternalOligoTmCalSettingsPanel();
        probeSettings.populateUI(userInput);
        ret.add(probeSettings, PROBE_SETTINGS_PANEL);
        return ret;
    }
    
    UserInput updateUserInputFromUI(){
        if(isForward() || isReverse()){
            primerSettingsPanel.updateUserInputFromUI(userInput);
        }else{
            probeSettings.updateUserInputFromUI(userInput);
        }
        return userInput;
    }
    
    private JPanel create(){
        
        ButtonGroup bGroup = new ButtonGroup();
        
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c;
        
        c = new GridBagConstraints();
        forward = new JRadioButton("Forward Primer");
        forward.setActionCommand("forward");
        ret.add(forward, c);
        
        c = new GridBagConstraints();
        probe = new JRadioButton("DNA Probe");
        probe.setActionCommand("probe");
        ret.add(probe, c);
        
        c = new GridBagConstraints();
        reverse = new JRadioButton("Reverse Primer");
        reverse.setActionCommand("reverse");
        ret.add(reverse, c);
        
        bGroup.add(forward);
        bGroup.add(probe);
        bGroup.add(reverse);
        
        forward.setSelected(true);
        
        return ret;
    }
    
    boolean isForward(){
        return forward.isSelected();
    }
    
    boolean isDNAProbe(){
        return probe.isSelected();
    }    
    
    boolean isReverse(){
        return reverse.isSelected();
    }
}
