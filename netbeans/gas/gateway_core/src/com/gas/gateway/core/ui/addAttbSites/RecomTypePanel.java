/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.gateway.core.ui.addAttbSites;

import com.gas.common.ui.util.Unicodes;
import com.gas.gateway.core.service.api.RecomType;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author dq
 */
public class RecomTypePanel extends JPanel {        
    
    private JRadioButton oneFragBtn;
    private JRadioButton pro2Btn;
    private JRadioButton pro3Btn;
    private JRadioButton pro4Btn;
    private JRadioButton threeBtn;
    
    public RecomTypePanel(){
        TitledBorder border = BorderFactory.createTitledBorder(new LineBorder(Color.GRAY), "Recombination Type", TitledBorder.LEFT, TitledBorder.TOP);
        setBorder(border);
        
        LayoutManager layout = null;
        layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints c = null;
        
        ButtonGroup btnGroup = new ButtonGroup();
        
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        oneFragBtn = new JRadioButton(RecomType.GW.getDesc());
        oneFragBtn.setActionCommand(RecomType.GW.getDesc());
        add(oneFragBtn, c);
        btnGroup.add(oneFragBtn);
        
        c = new GridBagConstraints();               
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        pro2Btn = new JRadioButton(RecomType.Pro2.getDesc());
        pro2Btn.setActionCommand(RecomType.Pro2.getDesc());
        add(pro2Btn, c);
        btnGroup.add(pro2Btn);
        
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        pro3Btn = new JRadioButton(RecomType.Pro3.getDesc());
        pro3Btn.setActionCommand(RecomType.Pro3.getDesc());
        add(pro3Btn, c);
        btnGroup.add(pro3Btn);
        
        c = new GridBagConstraints();
        c.gridx = GridBagConstraints.RELATIVE;
        c.gridy = 0;
        c.anchor = GridBagConstraints.WEST;
        pro4Btn = new JRadioButton(RecomType.Pro4.getDesc());
        pro4Btn.setActionCommand(RecomType.Pro4.getDesc());
        add(pro4Btn, c);
        btnGroup.add(pro4Btn);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 3;
        
        threeBtn = new JRadioButton(RecomType.ThreeFrag.getDesc());
        threeBtn.setActionCommand(RecomType.ThreeFrag.getDesc());
        add(threeBtn, c);
        btnGroup.add(threeBtn);        
        
        hookupListeners();
    }
    
    private void hookupListeners(){
        oneFragBtn.addActionListener(new RecomTypePanelListeners.BtnListener());
        pro2Btn.addActionListener(new RecomTypePanelListeners.BtnListener());
        pro3Btn.addActionListener(new RecomTypePanelListeners.BtnListener());
        pro4Btn.addActionListener(new RecomTypePanelListeners.BtnListener());
        threeBtn.addActionListener(new RecomTypePanelListeners.BtnListener());
    }
    
    protected void setRecombType(RecomType type){
        if(type == RecomType.GW){
            oneFragBtn.setSelected(true);
        }else if(type == RecomType.Pro2){
            pro2Btn.setSelected(true);
        }else if(type == RecomType.Pro3){
            pro3Btn.setSelected(true);
        }else if(type == RecomType.Pro4){
            pro4Btn.setSelected(true);
        }else if(type == RecomType.ThreeFrag){
            threeBtn.setSelected(true);
        }
    }

    public JRadioButton getFourFragBtn() {
        return pro4Btn;
    }

    public JRadioButton getOneFragBtn() {
        return oneFragBtn;
    }

    public JRadioButton getThreeFragBtn() {
        return pro3Btn;
    }

    public JRadioButton getTwoFragBtn() {
        return pro2Btn;
    }
    
}
