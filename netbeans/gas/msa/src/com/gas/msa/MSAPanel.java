/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa;

import com.gas.domain.core.msa.clustalw.IClustalWUI;
import com.gas.domain.core.msa.clustalw.IClustalWUIFactory;
import com.gas.domain.core.msa.muscle.IMuscleUI;
import com.gas.domain.core.msa.muscle.IMuscleUIFactory;
import com.gas.domain.core.msa.vfmsa.IVfMsaUI;
import com.gas.domain.core.msa.vfmsa.IVfMsaUIFactory;
import com.gas.msa.common.ExecutableName;
 import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class MSAPanel extends JPanel {

    private JTabbedPane tabbedPane;
    private IVfMsaUI vfMsaUI;
    private IMuscleUI muscleUI;
    private IClustalWUI clustalWUI;
    private Component selected;
 
    public MSAPanel(String profile1, String profile2) {
        LayoutManager layout = new GridBagLayout();
        setLayout(layout);
        createComponents(profile1, profile2);
        hookupListeners();
    }

    public Component getSelected() {
        if(selected == null){
            selected = tabbedPane.getSelectedComponent();
        }
        return selected;
    }

    public void setSelected(Component selected) {
        this.selected = selected;
        this.tabbedPane.setSelectedComponent(selected);
    }        

    private void createComponents(String profile1, String profile2) {
        GridBagConstraints c;

        tabbedPane = new JTabbedPane();
        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        add(tabbedPane, c);

        if(profile1 == null && profile2 == null){
            IVfMsaUIFactory factoryVfMsaUI = Lookup.getDefault().lookup(IVfMsaUIFactory.class);
            vfMsaUI = factoryVfMsaUI.create();
            tabbedPane.add((JComponent) vfMsaUI, "VectorFriends Aligner");
        }
        IClustalWUIFactory facotryClustalwUI = Lookup.getDefault().lookup(IClustalWUIFactory.class);
        clustalWUI = facotryClustalwUI.create(false, profile1, profile2);
        tabbedPane.add((JComponent) clustalWUI, "ClustalW");

        IMuscleUIFactory factoryMuscle = Lookup.getDefault().lookup(IMuscleUIFactory.class);
        muscleUI = factoryMuscle.create(true, profile1, profile2);
        tabbedPane.add((JComponent) muscleUI, "Muscle");
                
    }

    public String getProfile1() {
        Component _selected = tabbedPane.getSelectedComponent();
        if (_selected == muscleUI) {
            return muscleUI.getProfile1();
        } else if (_selected == clustalWUI) {
            return clustalWUI.getProfile1();
        }
        return null;
    }
    
    public String getProfile2() {
        Component _selected = tabbedPane.getSelectedComponent();
        if (_selected == muscleUI) {
            return muscleUI.getProfile2();
        } else if (_selected == clustalWUI) {
            return clustalWUI.getProfile2();
        }
        return null;
    }    

    private void hookupListeners() {
        tabbedPane.addChangeListener(new MSAPanelListeners.TabbedPaneListener());
    }

    public IClustalWUI getClustalWUI() {
        return clustalWUI;
    }

    public IMuscleUI getMuscleUI() {
        return muscleUI;
    }
    
    public IVfMsaUI getVfMsaUI(){
        return vfMsaUI;
    }
}
