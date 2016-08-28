/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.main.ui.molpane;

import com.gas.common.ui.util.CommonUtil;
import com.gas.common.ui.util.UIUtil;
import com.gas.domain.core.primer3.UserInput;
import com.gas.main.ui.molpane.sitepanel.primer3.InternalOligoTmCalSettingsPanel;
import com.gas.main.ui.molpane.sitepanel.primer3.PrimerTmCalSettingsPanel;
import com.gas.main.ui.molpane.sitepanel.primer3.TmCalSettingsPanel;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import org.jdesktop.swingx.JXHyperlink;

/**
 *
 * @author dq
 */
public class Convert2OligoPanel extends JPanel {
    
    private UserInput userInput;
    
    private JRadioButton forwardBtn;
    private JRadioButton probeBtn;
    private JRadioButton reverseBtn;
    private JPanel contentPanel;
    private InternalOligoTmCalSettingsPanel probeSettingsPanel;
    private PrimerTmCalSettingsPanel primerSettingsPanel;
    
    public Convert2OligoPanel(UserInput userInput){
        UIUtil.setDefaultBorder(this);
        this.userInput = userInput;
        initComponents();
        
        hookupListeners();
    }
    
    private void initComponents(){
        setLayout(new GridBagLayout());
        GridBagConstraints c ;
        int gridy = 0;
        
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.anchor = GridBagConstraints.WEST;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel b = createPanel();
        add(b, c);
        
        c = new GridBagConstraints();    
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        JPanel tmCalculationPanels = createTmCalculationPanels();
        add(tmCalculationPanels, c);        
        
        JPanel citationPanel = createCitationPanel();
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        add(citationPanel, c);
    }
    
    private JPanel createCitationPanel(){
        JPanel ret = new JPanel();
        JLabel label = new JLabel();
        final String url = "http://primer3.wi.mit.edu/primer3web_help.htm#citationRequest";
        label.setForeground(Color.GRAY);
        label.setText("Please cite ");
        JXHyperlink link = new JXHyperlink(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CommonUtil.browse(url);
            }
        });
        link.setText("Primer3");
        link.setFocusable(false);
        
        ret.add(label);
        ret.add(link);
        return ret;
    }
    
    private void hookupListeners(){
        forwardBtn.addActionListener(new Convert2OligoListeners.OligoTypeListener());
        probeBtn.addActionListener(new Convert2OligoListeners.OligoTypeListener());
        reverseBtn.addActionListener(new Convert2OligoListeners.OligoTypeListener());
    }
    
    private JPanel createPanel(){
        JPanel ret = new JPanel(new GridBagLayout());
        GridBagConstraints c = null;
        int gridx = 0;
        int gridy = 0;
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        ret.add(new JLabel("Type:"), c);
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        forwardBtn = new JRadioButton("Forward", true);      
        ret.add(forwardBtn, c);
        
        c = new GridBagConstraints();
        c.gridy = gridy;
        c.anchor = GridBagConstraints.WEST;
        probeBtn = new JRadioButton("Probe");
        ret.add(probeBtn, c);
        
        c = new GridBagConstraints();
        c.gridy = gridy++;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.anchor = GridBagConstraints.WEST;
        reverseBtn = new JRadioButton("Reverse");
        ret.add(reverseBtn, c);     
                
        ButtonGroup bGroup = new ButtonGroup();
        bGroup.add(forwardBtn);
        bGroup.add(probeBtn);
        bGroup.add(reverseBtn);
        
        return ret;
    }
    
    void show(Class clazz){
        CardLayout layout = (CardLayout)contentPanel.getLayout();
        layout.show(contentPanel, clazz.getSimpleName());
    }
    
    public UserInput getUserInputFromUI(){
        if(probeSettingsPanel.isShowing()){
            probeSettingsPanel.updateUserInputFromUI(userInput);
        }else if(primerSettingsPanel.isShowing()){
            primerSettingsPanel.updateUserInputFromUI(userInput);
        }        
        return userInput;
    }
    
    public boolean isForward(){
        return forwardBtn.isSelected();
    }
    
    public boolean isReverse(){
        return reverseBtn.isSelected();
    }
    
    public boolean isProbe(){
        return probeBtn.isSelected();
    }
    
    private JPanel createTmCalculationPanels(){
        JPanel ret = new JPanel(new BorderLayout());
        ret.setBorder(BorderFactory.createTitledBorder( //
                new LineBorder(Color.GRAY), //
                "Tm Calculation Settings", //
                TitledBorder.LEFT, //
                TitledBorder.TOP, //
                UIManager.getFont("TitledBorder.font")));
        
        contentPanel = new JPanel(new CardLayout());
        primerSettingsPanel = new PrimerTmCalSettingsPanel();
        primerSettingsPanel.populateUI(userInput);
        contentPanel.add(primerSettingsPanel, PrimerTmCalSettingsPanel.class.getSimpleName());
        
        probeSettingsPanel = new InternalOligoTmCalSettingsPanel();    
        probeSettingsPanel.populateUI(userInput);
        contentPanel.add(probeSettingsPanel, InternalOligoTmCalSettingsPanel.class.getSimpleName());
        
        ret.add(contentPanel, BorderLayout.CENTER);
        return ret;
    }
}
