/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gas.msa.ui.alignment.ctrl.param;

import com.gas.common.ui.button.FlatBtn;
import com.gas.common.ui.image.ImageHelper;
import com.gas.common.ui.image.ImageNames;
import com.gas.common.ui.jcomp.TitledPanel;
import com.gas.domain.core.msa.clustalw.IClustalWUI;
import com.gas.domain.core.msa.clustalw.IClustalWUIFactory;
import com.gas.domain.core.msa.clustalw.ClustalwParam;
import com.gas.domain.core.msa.muscle.IMuscleUI;
import com.gas.domain.core.msa.muscle.IMuscleUIFactory;
import com.gas.domain.core.msa.muscle.MuscleParam;
import com.gas.domain.core.msa.vfmsa.IVfMsaUI;
import com.gas.domain.core.msa.vfmsa.IVfMsaUIFactory;
import com.gas.domain.core.msa.vfmsa.VfMsaParam;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import org.openide.util.Lookup;

/**
 *
 * @author dq
 */
public class ParamsPanel extends JPanel {

    IVfMsaUI vfMsaUI;
    IClustalWUI clustalwUI;
    IMuscleUI muscleUI;
    FlatBtn playBtn;
    TitledPanel titledPanel;

    public ParamsPanel() {
        initComponents();
        hookupListeners();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        titledPanel = new TitledPanel("Parameters");   
        playBtn = new FlatBtn(ImageHelper.createImageIcon(ImageNames.PLAY_16));
        titledPanel.setRightDecoration(playBtn);
        add(titledPanel, BorderLayout.CENTER);
        titledPanel.getContentPane().setLayout(new BorderLayout());
    }

    private void hookupListeners() {
        playBtn.addActionListener(new ParamsPanelListeners.PlayListener());
    }

    public void setTitle(String title) {
        titledPanel.setTitle(title);
    }
    
    public void createUI(VfMsaParam vfMsaParam, boolean isAminoAcid){
        IVfMsaUIFactory factory = Lookup.getDefault().lookup(IVfMsaUIFactory.class);
        vfMsaUI = factory.create();
        vfMsaUI.setAminoAcids(isAminoAcid);
        vfMsaUI.setVfMsaParam(vfMsaParam);
        titledPanel.getContentPane().add((Component)vfMsaUI, BorderLayout.CENTER);
    }

    public void createUI(ClustalwParam clustalwParam) {
        IClustalWUIFactory factory = Lookup.getDefault().lookup(IClustalWUIFactory.class);
        clustalwUI = factory.create(true, null, null);

        JScrollPane scrollPane = new JScrollPane((Component) clustalwUI);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        titledPanel.getContentPane().add(scrollPane, BorderLayout.CENTER);
        clustalwUI.setMsaParam(clustalwParam);
        titledPanel.revalidate();
    }

    public void createUI(MuscleParam param) {
        IMuscleUIFactory factory = Lookup.getDefault().lookup(IMuscleUIFactory.class);
        muscleUI = factory.create(null, null);

        JScrollPane scrollPane = new JScrollPane((Component) muscleUI);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        titledPanel.getContentPane().add(scrollPane, BorderLayout.CENTER);
        muscleUI.setMuscleParam(param);
        titledPanel.revalidate();
    }

    public ImageIcon getImageIcon() {
        return ImageHelper.createImageIcon(ImageNames.PLAY_16);
    }
}
